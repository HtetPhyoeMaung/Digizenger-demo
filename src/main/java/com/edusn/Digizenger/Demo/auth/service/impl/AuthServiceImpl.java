package com.edusn.Digizenger.Demo.auth.service.impl;
import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.edusn.Digizenger.Demo.auth.dto.request.LoginRequest;
import com.edusn.Digizenger.Demo.auth.dto.request.RegisterRequest;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.Address;
import com.edusn.Digizenger.Demo.auth.entity.Role;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ProfileDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.UserForProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.ProfileService;
import com.edusn.Digizenger.Demo.security.JWTService;
import com.edusn.Digizenger.Demo.security.UserDetailServiceForUser;
import com.edusn.Digizenger.Demo.auth.service.AuthService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
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
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final MailUtil mailUtil;

    private final JWTService jwtService;

    private final ModelMapper modelMapper;

    private final OtpUtil otpUtil;

    private final DateUtil dateUtil;

    private final UserDetailServiceForUser userDetailServiceForUser;

    private final CheckEmailOrPhoneUtil checkEmailOrPhoneUtil;

    private final PasswordEncoder passwordEncoder;

    private final ProfileService profileService;

    private final ProfileRepository profileRepository;

    private final StorageService storageService;

    private final MapperUtil mapperUtil;

    private static final int OTP_VALIDITY_DURATION_SECONDS = 60;


    @Override
    public ResponseEntity<Response> register(RegisterRequest request) throws MessagingException {
        String otp;
        if(!request.getEmail().isEmpty()){
            Optional<User> checkUserEmail = userRepository.findByEmail(request.getEmail());
            if (checkUserEmail.isPresent()){
                throw new AlreadyExistsException("Email's already exist!");
            }
             otp = otpUtil.generateOtp();
            String fullName = request.getFirstName()+" "+request.getLastName();
            mailUtil.sendOtpEmail(fullName,request.getEmail(),otp);
        }else{
            Optional<User> checkUserPhone = userRepository.findByPhone(request.getPhone());
            if (checkUserPhone.isPresent()){
                throw new AlreadyExistsException("Phone's already exist!");
            }
            otp = otpUtil.generateOtp();
        }
        Address address = Address.builder().country(request.getCountry())
                                           .city(request.getCity())
                                           .build();
        User user = User.builder().firstName(request.getFirstName())
                                  .lastName(request.getLastName())
                                  .email(request.getEmail())
                                  .password(passwordEncoder.encode(request.getPassword()))
                                  .dateOfBirth(request.getDateOfBirth())
                                  .otp(otp)
                                  .createdDate(LocalDateTime.now())
                                  .phone(request.getPhone())
                                  .otpGeneratedTime(LocalDateTime.now())
                                  .address(address)
                                  .gender(User.Gender.valueOf(request.getGender()))
                                  .role(request.getRole()!=null?request.getRole():Role.USER.name())
                                  .gender(User.Gender.valueOf(request.getGender()))
                                  .build();
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
                user.setVerified(user.getRole().equals(Role.ADMIN.name()));
                user.setLastLoginTime(LocalDateTime.now());
                user.setStatus(User.Status.ONLINE);
                userRepository.save(user);
                /* Create user's profile */
                profileService.createUserProfile(user);
                UserDetails userDetails;
                userDetails = userDetailServiceForUser.loadUserByUsername(user.getEmail().isEmpty()?user.getPhone():user.getEmail());
                String token = jwtService.generateToken(userDetails);
                Profile profile = user.getProfile();
                Response response = Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .token(token)
                        .message("Successfully Registered!")
                        .expirationDate("7days")
                        .userDto(mapperUtil.convertToUserDto(profile.getUser(),true))
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
         throw new CustomNotFoundException("Email was not verified. So please verified your email!");
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
                    .orElseThrow(() -> new CustomNotFoundException("User can't found by "+emailOrPhone));
        }else {
            user = userRepository.findByPhone(emailOrPhone)
                    .orElseThrow(() -> new CustomNotFoundException("User can't found by "+emailOrPhone));
        }

        Profile profile = profileRepository.findByUser(user);

        /* Last Login Time */
        user.setLastLoginTime(LocalDateTime.now());
        user.setStatus(User.Status.ONLINE);
        userRepository.save(user);


        ProfileDto existProfileDto = mapperUtil.convertToProfileDto(profile);
        //existProfileDto.setUserForProfileDto(userForProfileDto);
       Response response = Response.builder()
               .statusCode(HttpStatus.OK.value())
               .message("Login Success!")
               .userDto(mapperUtil.convertToUserDto(user,true))
               .token(token)
               .expirationDate("7days")
               .build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }



    @Override
    public ResponseEntity<Response> disconnect(User user) {
        User existUser=userRepository.findById(user.getId()).orElseThrow(()->new CustomNotFoundException("User not Found"));
        existUser.setStatus(User.Status.OFFLINE);
        existUser.setLastLoginTime(LocalDateTime.now());
        userRepository.save(existUser);
        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("disconnect success!")
                .userDto(UserDto.builder()
                        .lastLoginTime(dateUtil.formattedDate(existUser.getLastLoginTime()))
                        .status(existUser.getStatus())
                        .build())
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
