package org.example.mymallapplication.dal.dao.entity.person;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.example.mymallapplication.dal.enums.Gender;
import org.example.mymallapplication.dal.enums.Location;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-18
 */
@TableName("frontend_users")
@Schema(name = "FrontendUsers对象", description = "")
public class FrontendUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String nickname;

    @TableField("gender")
    private Gender gender;

    @TableField("location")
    private Location location;

    private String phone;

    private String email;

    private String address;

    @Getter
    @Setter
    private Integer point;

    @Getter
    @Setter
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Getter
    @Setter
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Getter
    @Setter
    @TableLogic
    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "FrontendUsers{" +
                "id = " + id +
                ", username = " + username +
                ", password = " + password +
                ", nickname = " + nickname +
                ", gender = " + gender +
                ", location = " + location +
                ", phone = " + phone +
                ", email = " + email +
                ", address = " + address +
                "}";
    }
}
