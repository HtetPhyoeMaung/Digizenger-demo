package com.edusn.Digizenger.Demo.exception;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.edusn.Digizenger.Demo.auth.dto.response.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LoginNameExistException.class)
    public ResponseEntity<CustomErrorResponse> emailExistHandler(LoginNameExistException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.ALREADY_REPORTED.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
        .build();
        return new ResponseEntity<>(response,HttpStatus.ALREADY_REPORTED);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> emailNotFoundHandler(UserNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UnverifiedException.class)
    public ResponseEntity<CustomErrorResponse> unverifiedExceptionHandler(UnverifiedException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_ACCEPTABLE.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> postNotFoundHandler(PostNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> customNotFoundHandler(CustomNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> likeNotFoundHandler(LikeNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> profileNotFoundHandler(ProfileNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProfileImageNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> profileImageNotFoundHandler(ProfileImageNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CoverImageNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> coverImageNotFoundException(CoverImageNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BioNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> BioNotFoundHandler(BioNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CareerNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> CareerNotFoundHandler(CareerNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CareerHistoryNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> CareerHistoryNotFoundHandler(CareerHistoryNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceProvidedNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> ServiceProvidedNotFoundHandler(ServiceProvidedNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FollowerNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> followerNotFoundHandler(FollowingNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FollowingNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> followingNotFoundHandler(FollowingNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NeighborNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> neighborNotFoundHandler(NeighborNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CannotFollowException.class)
    public ResponseEntity<CustomErrorResponse> cannotFollowHandler(CannotFollowException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_ACCEPTABLE.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(CannotUnfollowException.class)
    public ResponseEntity<CustomErrorResponse> cannotUnfollowHandler(CannotUnfollowException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_ACCEPTABLE.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> badRequestHandler(Exception ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> alreadyExistsHandler(AlreadyExistsException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.ALREADY_REPORTED.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
    }
}
