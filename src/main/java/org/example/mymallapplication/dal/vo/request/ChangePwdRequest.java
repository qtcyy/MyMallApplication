package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author chengyiyang
 */
@Data
public class ChangePwdRequest {
    @NotBlank(message = "密码不能为空")
    private String password;
}
