package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.mymallapplication.dal.enums.Gender;
import org.example.mymallapplication.dal.enums.Location;

/**
 * @author chengyiyang
 */
@Data
public class UserRegisterRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "性别不能为空")
    private Gender gender;
    @NotBlank(message = "昵名不能为空")
    private String nickname;
    @NotBlank(message = "省份不能为空")
    private Location location;
    @NotBlank(message = "电话不能为空")
    private String phone;
    @NotBlank(message = "邮箱不能为空")
    private String email;
}
