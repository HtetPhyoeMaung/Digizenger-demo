package com.edusn.Digizenger.Demo.dashboard.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.dashboard.service.AdminDashBoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashBoardController {

    private final AdminDashBoardService adminDashBoardService;

    @GetMapping("/get-users")
    public ResponseEntity<Response>  getAllUsers(HttpServletRequest  request,
                                                  @RequestParam("_page") int _page,
                                                  @RequestParam("_limit") int _limit){

        return  adminDashBoardService.showAdminDashBoard(request, _page, _limit);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Response> showUserData(HttpServletRequest request,
                                                 @PathVariable("userId") Long id){
        return adminDashBoardService.showUserData(request, id);
    }

    @GetMapping("/getNewUserIn30Days")
    public ResponseEntity<Response> getNewUserIn30Days(@RequestParam("_page") int page,
                                                       @RequestParam("_limit") int limit
                                                      ){
      return   adminDashBoardService.getNewUserIn30Days(page,limit);

    }
    @GetMapping("/getVerifiedUsers")
    public ResponseEntity<Response> getVerifiedUsers (@RequestParam ("_page") int page,
                                                      @RequestParam ("_limit") int limit){

        return adminDashBoardService.getVerifiedUsers(page , limit);

    }




}
