package org.example.mymallapplication.dal.vo.request;

import lombok.Data;
import org.example.mymallapplication.dal.enums.Gender;
import org.example.mymallapplication.dal.enums.Location;

@Data
public class UserRegisterRequest {
    private String username;
    private String password;
    private Gender gender;
    private String nickname;
    private Location location;
    private String phone;
    private String email;
}
