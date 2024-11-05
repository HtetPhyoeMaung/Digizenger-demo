package com.edusn.Digizenger.Demo.dashboard.service;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface AdminDashBoardService  {

    ResponseEntity<Response> showAdminDashBoard(HttpServletRequest request,int _page, int _limit);

    ResponseEntity<Response> showUserData(HttpServletRequest request, Long id);

    ResponseEntity<Response> getNewUserIn30Days(int page, int limit);

    ResponseEntity<Response> getVerifiedUsers(int page, int limit);

    Map<String ,Object>  getSuspendedUsers (int page , int limit , int days);

}
