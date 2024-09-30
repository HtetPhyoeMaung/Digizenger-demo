package com.edusn.Digizenger.Demo;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
   @GetMapping("/serverTest")
    public ResponseEntity<Response> getServerTest()  {
       Response response = Response.builder()
               .statusCode(HttpStatus.OK.value())
               .message("Test Success")
               .build();
       return new ResponseEntity<>(response, HttpStatus.OK);
   }

}
