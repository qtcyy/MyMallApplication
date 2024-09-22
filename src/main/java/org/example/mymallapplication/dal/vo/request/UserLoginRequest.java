package org.example.mymallapplication.dal.vo.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;
}
