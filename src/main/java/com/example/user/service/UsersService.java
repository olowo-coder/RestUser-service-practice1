package com.example.user.service;

import com.example.user.VO.ResponseTemplateVO;
import com.example.user.entity.Users;
import org.springframework.http.ResponseEntity;

public interface UsersService {
    Users saveUser(Users user);

    ResponseEntity<?> getUserWithDepartment(Long userId);

    ResponseEntity<?> getUserDepartmentWithWebClient(Long userId);
}
