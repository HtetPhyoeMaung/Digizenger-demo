package com.edusn.Digizenger.Demo.dashboard.admin.service;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AdminDashBoardService  {

    ResponseEntity<Response> showAdminDashBoard(HttpServletRequest request,int _page, int _limit);

}
