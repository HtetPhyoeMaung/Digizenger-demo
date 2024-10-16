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
import com.edusn.Digizenger.Demo.post.repo.LikeRepository;
import com.edusn.Digizenger.Demo.post.repo.ViewRepository;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ProfileDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.UserForProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.ProfileService;
import com.edusn.Digizenger.Demo.security.JWTService;
import com.edusn.Digizenger.Demo.security.UserDetailServiceForUser;
import com.edusn.Digizenger.Demo.auth.service.AuthService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.CheckEmailOrPhoneUtil;
import com.edusn.Digizenger.Demo.utilis.MailUtil;
import com.edusn.Digizenger.Demo.utilis.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j

public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private UserDetailServiceForUser userDetailServiceForUser;
    @Autowired
    private CheckEmailOrPhoneUtil checkEmailOrPhoneUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StorageService storageService;
    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Value("${app.profileUrl}")
    private String baseProfileUrl;

    private static final long OTP_VALIDITY_DURATION_SECONDS = 60;


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
        if (user.getOtp().equals(otp)&& Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds()<(1*60)){
            user.setActivated(true);

            user.setCreatedDate(LocalDateTime.now());
            if(user.getRole().equals(Role.ADMIN.name())){
                user.setVerified(true);
            }else{
                user.setVerified(false);
            }
            userRepository.save(user);

            /* Create user's profile */
            profileService.createUserProfile(user);


            // check (isActivated)
            UserDetails userDetails;
            if (!checkUser.isActivated()){
                throw new UnverifiedException("Email was not verified. So please verified your email!");
            }

            userDetails = userDetailServiceForUser.loadUserByUsername(checkUser.getEmail().isEmpty()?user.getPhone():user.getEmail());
            String token = jwtService.generateToken(userDetails);
            Response response = Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .token(token)
                    .expirationDate("7days")
                    .message("Successfully Registered!")
                    .statusCode(HttpStatus.CREATED.value())
                    .build();
            return new ResponseEntity<>(response,HttpStatus.CREATED);
        }
        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OTP was invalided! Please try again.")
                .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
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

        /** To gave profile with login token **/

        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User can't found by "+email));
        Profile profile = profileRepository.findByUser(user);

        /* Last Login Time */
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        if(profile.getUsername() != null){
            profile.setProfileLinkUrl(baseProfileUrl+profile.getUsername());
            profileRepository.save(profile);
        }

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




}
