package com.edusn.Digizenger.Demo.auth.service.impl;
import com.edusn.Digizenger.Demo.auth.dto.request.LoginRequest;
import com.edusn.Digizenger.Demo.auth.dto.request.RegisterRequest;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.Address;
import com.edusn.Digizenger.Demo.auth.entity.Role;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.LoginNameExistException;
import com.edusn.Digizenger.Demo.exception.UnverifiedException;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.exception.UserNotFoundException;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ProfileDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.UserForProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.ProfileService;
import com.edusn.Digizenger.Demo.security.JWTService;
import com.edusn.Digizenger.Demo.auth.service.AuthService;
import com.edusn.Digizenger.Demo.security.UserDetailServiceForUser;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.CheckEmailOrPhoneUtil;
import com.edusn.Digizenger.Demo.utilis.DateUtil;
import com.edusn.Digizenger.Demo.utilis.MailUtil;
import com.edusn.Digizenger.Demo.utilis.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;


    private final AuthenticationManager authenticationManager;


    private final MailUtil mailUtil;


    private final JWTService jwtService;


    private final OtpUtil otpUtil;

    private final DateUtil dateUtil;

    private final UserDetailServiceForUser userDetailServiceForUser;

    private final CheckEmailOrPhoneUtil checkEmailOrPhoneUtil;

    private final PasswordEncoder passwordEncoder;

    private final ProfileService profileService;

    private final ProfileRepository profileRepository;

    private final ModelMapper modelMapper;

    private final StorageService storageService;


    private static final int OTP_VALIDITY_DURATION_SECONDS = 60;


    @Override
    public ResponseEntity<Response> register(RegisterRequest request) throws MessagingException {
        String otp;
        if(!request.getEmail().isEmpty()){
            Optional<User> checkUserEmail = userRepository.findByEmail(request.getEmail());
            if (checkUserEmail.isPresent()){
                throw new LoginNameExistException("Email's already exist!");
            }
             otp = otpUtil.generateOtp();
            String fullName = request.getFirstName()+" "+request.getLastName();
            mailUtil.sendOtpEmail(fullName,request.getEmail(),otp);

        }else{
            Optional<User> checkUserPhone = userRepository.findByPhone(request.getPhone());
            if (checkUserPhone.isPresent()){
                throw new LoginNameExistException("Phone's already exist!");
            }
            otp = otpUtil.generateOtp();

        }

        Address address = new Address();
        address.setCountry(request.getCountry());
        address.setCity(request.getCity());
        User user = new User();
                user.setFirstName(request.getFirstName());
                user.setLastName(request.getLastName());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setDateOfBirth(request.getDateOfBirth());
                user.setOtp(otp);
                user.setCreatedDate(LocalDateTime.now());
                user.setPhone(request.getPhone());
                user.setOtpGeneratedTime(LocalDateTime.now());
                user.setAddress(address);
                user.setGender(User.Gender.valueOf(request.getGender()));
        if(request.getRole() != null){
            user.setRole(request.getRole());
        }else {
            user.setRole(Role.USER.name());
        }
        userRepository.save(user);
        String result = request.getEmail().isEmpty()?request.getPhone():request.getEmail();
        Response response = Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("OTP was sent to "+result+".")
                .otp(otp)
                .build();

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }


    public ResponseEntity<Response> verifyAccount(String emailOrPhone, String otp) {
        User user=checkEmailOrPhoneUtil.checkEmailOrPhone(emailOrPhone);
        if (user.getOtp().equals(otp)&& Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds()<(60)){

            //For old user,Forgot password
            if(user.isActivated()){
                UserDetails userDetails;

                userDetails = userDetailServiceForUser.loadUserByUsername(user.getEmail().isEmpty()?user.getPhone():user.getEmail());
                String token = jwtService.generateToken(userDetails);

                Response response = Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .token(token)
                        .expirationDate("7 days.")
                        .message("successfully verified.")
                        .build();
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            // For new user, to register.
            else {
                user.setActivated(true);

                user.setCreatedDate(LocalDateTime.now());
                if(user.getRole().equals(Role.ADMIN.name())){
                    user.setVerified(true);
                }else{
                    user.setVerified(false);
                }

                user.setLastLoginTime(LocalDateTime.now());
                user.setStatus(User.Status.ONLINE);
                userRepository.save(user);

                /* Create user's profile */
                profileService.createUserProfile(user);

                UserDetails userDetails;

                userDetails = userDetailServiceForUser.loadUserByUsername(user.getEmail().isEmpty()?user.getPhone():user.getEmail());
                String token = jwtService.generateToken(userDetails);

                Profile profile = profileRepository.findByUser(user);

                ProfileDto profileDto = modelMapper.map(profile, ProfileDto.class);
                UserForProfileDto userForProfileDto = modelMapper.map(profile.getUser(), UserForProfileDto.class);


                profileDto.setUserForProfileDto(userForProfileDto);
                if(profileDto.getProfileImageName() != null){
                    profileDto.setProfileImageUrl(
                            storageService.getImageByName(profileDto.getProfileImageName())
                    );
                }
                if(profileDto.getCoverImageName() != null){
                    profileDto.setCoverImageUrl(
                            storageService.getImageByName(profileDto.getCoverImageName())
                    );
                }

                Response response = Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .token(token)
                        .message("Successfully Registered!")
                        .expirationDate("7days")
                        .userDto(UserDto.builder()
                                .status(profile.getUser().getStatus())
                                .lastLoginTime(dateUtil.formattedDate(profile.getUser().getLastLoginTime()))
                                .build())
                        .profileDto(profileDto)
                        .statusCode(HttpStatus.CREATED.value())
                        .build();
                return new ResponseEntity<>(response,HttpStatus.CREATED);
            }
        }
        Response response = Response.builder()
                .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                .message("OTP was invalided! Please try again.")
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_ACCEPTABLE);

    }
    public ResponseEntity<Response> resendCode(String emailOrPhone) throws MessagingException {
        // check
        String otp;

        User user=   checkEmailOrPhoneUtil.checkEmailOrPhone(emailOrPhone);
        String fullName = user.getFirstName()+" "+user.getLastName();
        otp = otpUtil.generateOtp();
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        mailUtil.sendOtpEmail(fullName,emailOrPhone,otp);
        userRepository.save(user);
        // response

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OTP was Sent!")
                .otp(otp)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Response> login(LoginRequest request) {
        User checkUser=checkEmailOrPhoneUtil.checkEmailOrPhone(request.getEmailOrPhone());


       // check (isActivated)
        UserDetails userDetails;
        if (!checkUser.isActivated()){
         throw new UnverifiedException("Email was not verified. So please verified your email!");
        }

        // authentication

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmailOrPhone(),request.getPassword()));
        userDetails = userDetailServiceForUser.loadUserByUsername(request.getEmailOrPhone());

        String token = jwtService.generateToken(userDetails);

        /* To gave profile with login token */
        User user;
        String emailOrPhone = jwtService.extractUsername(token);
        if (emailOrPhone.matches(".*@.*")) {
             user = userRepository.findByEmail(emailOrPhone)
                    .orElseThrow(() -> new UserNotFoundException("User can't found by "+emailOrPhone));
        }else {
            user = userRepository.findByPhone(emailOrPhone)
                    .orElseThrow(() -> new UserNotFoundException("User can't found by "+emailOrPhone));
        }

        Profile profile = profileRepository.findByUser(user);

        /* Last Login Time */
        user.setLastLoginTime(LocalDateTime.now());
        user.setStatus(User.Status.ONLINE);
        userRepository.save(user);


        ProfileDto existProfileDto = modelMapper.map(profile, ProfileDto.class);
        UserForProfileDto userForProfileDto = modelMapper.map(profile.getUser(), UserForProfileDto.class);


        existProfileDto.setUserForProfileDto(userForProfileDto);
        if(existProfileDto.getProfileImageName() != null){
            existProfileDto.setProfileImageUrl(
                    storageService.getImageByName(existProfileDto.getProfileImageName())
            );
        }
        if(existProfileDto.getCoverImageName() != null){
            existProfileDto.setCoverImageUrl(
                    storageService.getImageByName(existProfileDto.getCoverImageName())
            );
        }



       Response response = Response.builder()
               .statusCode(HttpStatus.OK.value())
               .message("Login Success!")
               .userDto(UserDto.builder()
                       .lastLoginTime(dateUtil.formattedDate(user.getLastLoginTime()))
                       .status(user.getStatus())
                       .build())
               .token(token)
               .profileDto(existProfileDto)
               .expirationDate("7days")
               .build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Override
    public Optional<User> findById(Long recipientId) {
        return userRepository.findById(recipientId);
    }

    @Override
    public ResponseEntity<Response> disconnect(User user) {
        user.setStatus(User.Status.OFFLINE);
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);
        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("disconnect success!")
                .userDto(UserDto.builder()
                        .lastLoginTime(dateUtil.formattedDate(user.getLastLoginTime()))
                        .status(user.getStatus())
                        .build())
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}
