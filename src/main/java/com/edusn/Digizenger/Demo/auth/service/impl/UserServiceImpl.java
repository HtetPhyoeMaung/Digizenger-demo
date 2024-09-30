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
import com.edusn.Digizenger.Demo.profile.service.ProfileService;
import com.edusn.Digizenger.Demo.security.JWTService;
import com.edusn.Digizenger.Demo.security.UserDetailServiceForUser;
import com.edusn.Digizenger.Demo.auth.service.UserService;
import com.edusn.Digizenger.Demo.utilis.CheckEmailOrPhoneUtil;
import com.edusn.Digizenger.Demo.utilis.MailUtil;
import com.edusn.Digizenger.Demo.utilis.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserServiceImpl implements UserService {
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
            String fullName = request.getFirstName()+request.getLastName();
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
                user.setRole(Role.USER.name());
                user.setOtpGeneratedTime(LocalDateTime.now());
                user.setAddress(address);
                user.setGender(User.Gender.valueOf(request.getGender()));
                user.setCreatedDate(LocalDateTime.now());

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

            userRepository.save(user);

            /* Create user's profile */
            profileService.createUserProfile(user);

            Response response = Response.builder()
                    .statusCode(HttpStatus.OK.value())
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
        String fullName = user.getFirstName()+user.getLastName();
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

       Response response = Response.builder()
               .statusCode(HttpStatus.OK.value())
               .message("Login Success!")
               .token(token)
               .expirationDate("7days")
               .build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
