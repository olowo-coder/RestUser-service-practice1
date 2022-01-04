package com.example.user.controller;

import com.example.user.VO.ResponseTemplateVO;
import com.example.user.entity.Users;
import com.example.user.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RefreshScope
@RestController
@RequestMapping("/users")
@Slf4j
public class UsersController {

    private final UsersService usersService;

    @Value("${lanre.check}")
    private String testValue;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/add")
    public Users addingUser(@RequestBody Users user){
        log.info("Inside save users method of userController");
        return usersService.saveUser(user);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUserWithDepartment(@PathVariable("id") Long userId){
        log.info("Inside getting user with department method of userController");
        return usersService.getUserWithDepartment(userId);
    }

    @GetMapping("/two/{id}")
    public ResponseEntity<?> getUserWithDepartmentWebclient(@PathVariable("id") Long userId){
        log.info("Inside getting user with department method of userController but Weclient");
        return usersService.getUserDepartmentWithWebClient(userId);
    }


    @GetMapping("/check")
    public String getTest(){
        return this.testValue;
    }
}