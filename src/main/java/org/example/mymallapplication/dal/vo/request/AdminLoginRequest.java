package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author chengyiyang
 */
@Data
public class AdminLoginRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}
