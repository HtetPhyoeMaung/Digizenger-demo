package com.edusn.Digizenger.Demo.dashboard.admin.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.dashboard.admin.service.impl.AdminDashBoardServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard/admin")
@RequiredArgsConstructor
public class DashBoardController {

    private final AdminDashBoardServiceImpl adminDashBoardService;

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



}
