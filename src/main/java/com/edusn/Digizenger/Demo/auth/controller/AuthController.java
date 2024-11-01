package com.edusn.Digizenger.Demo.auth.controller;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.service.ForgotPasswordService;
import com.edusn.Digizenger.Demo.exception.ApiValidationException;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.edusn.Digizenger.Demo.auth.dto.request.LoginRequest;
import com.edusn.Digizenger.Demo.auth.dto.request.RegisterRequest;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.service.AuthService;
import jakarta.mail.MessagingException;

@Slf4j
@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private GetUserByRequest getUserByRequest;
    @Autowired ForgotPasswordService forgotPasswordService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody @Validated RegisterRequest request,BindingResult result) throws MessagingException {
        log.info("Reach Register");
        return authService.register(request);
    }


    @PutMapping("/verify-account")
    public ResponseEntity<Response> verifyAccount(@RequestParam @NotBlank(message = "EmailOrPhone cannot be blank") String emailOrPhone, @RequestParam @NotBlank(message = "Opt cannot be blank") String otp) {
        return authService.verifyAccount(emailOrPhone, otp);
    }


    @PutMapping("/resend-code")
    public ResponseEntity<Response> resendCode(@RequestParam @NotBlank(message = "EmailOrPhone cannot be blank") String emailOrPhone) throws MessagingException {
        return authService.resendCode(emailOrPhone);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody @Validated LoginRequest request,BindingResult result) throws ApiValidationException {
        return authService.login(request);
    }

    @PutMapping("/disconnect")
    public ResponseEntity<Response> disconnectSocket(HttpServletRequest request) {
        User user=getUserByRequest.getUser(request);
        return authService.disconnect(user);
    }

    @GetMapping("/forgot/check-user")
    public ResponseEntity<Response> checkUserByEmailOrPhoneOrUsername(@RequestParam @NotBlank(message = "EmailOrPhoneOrUsername should not be blank") String emailOrPhoneOrUsername){
        return forgotPasswordService.checkUserByEmailOrPhoneOrUsername(emailOrPhoneOrUsername);
    }

    @PutMapping("/forgot/reset-password")
    public ResponseEntity<Response> resetPassword(HttpServletRequest request,
                                                  @RequestParam @NotBlank(message = "New password should not be blank.") String newPassword,
                                                  @RequestParam @NotBlank(message = "Confirm password should not be blank") String confirmPassword){

        return forgotPasswordService.resetPassword(request, newPassword, confirmPassword);
    }
}
