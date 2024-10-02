package org.example.mymallapplication.dal.vo.request;

import lombok.Data;
import org.example.mymallapplication.dal.enums.Gender;
import org.example.mymallapplication.dal.enums.Location;

/**
 * @author chengyiyang
 */
@Data
public class UpdateUserRequest {
    private String username;
    private String password;
    private Gender gender;
    private Location location;
    private String email;
    private Integer point;
    private String phone;
    private String address;
}
