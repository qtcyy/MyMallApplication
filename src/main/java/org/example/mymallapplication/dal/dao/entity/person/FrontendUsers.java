package org.example.mymallapplication.dal.dao.entity.person;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.example.mymallapplication.dal.dao.entity.info.UpdateInfo;
import org.example.mymallapplication.dal.enums.Gender;
import org.example.mymallapplication.dal.enums.Location;

import java.io.Serializable;

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
@Getter
@Setter
public class FrontendUsers extends UpdateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

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

    private Integer point;

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
