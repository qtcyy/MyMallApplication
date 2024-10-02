package org.example.mymallapplication.dal.vo.response;

import lombok.Data;

/**
 * @author chengyiyang
 */
@Data
public class AdminLoginResponse {
    private String token;

    public AdminLoginResponse(String token) {
        this.token = token;
    }
}
