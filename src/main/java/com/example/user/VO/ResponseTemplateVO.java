package com.example.user.VO;


import com.example.user.entity.Users;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ResponseTemplateVO {

    private Users users;
    private Department department;
    private Object departmentWebclient;
}
