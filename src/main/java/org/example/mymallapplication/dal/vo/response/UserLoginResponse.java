package org.example.mymallapplication.dal.vo.response;

import lombok.Data;

@Data
public class UserLoginResponse {
    private String token;

    public UserLoginResponse(String token) {
        this.token = token;
    }
}
