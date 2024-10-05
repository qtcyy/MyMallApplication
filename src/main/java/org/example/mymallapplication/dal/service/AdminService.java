package org.example.mymallapplication.dal.service;

import cn.dev33.satoken.util.SaResult;
import org.example.mymallapplication.dal.dao.entity.product.Advertisements;
import org.example.mymallapplication.dal.vo.request.*;

import java.util.List;

/**
 * @author chengyiyang
 */
public interface AdminService {
    SaResult adminLogin(AdminLoginRequest request);

    SaResult adminRegister(AdminRegisterRequest request);

    SaResult changePwd(ChangePwdRequest request);

    List<String> getRole();

    List<String> getPermission();

    SaResult changeUserInfo(String mode, UpdateUserRequest request);

    SaResult addAdvertisement(AddAdvertisementRequest request);

    SaResult updateAdvertisement(Advertisements request);

    SaResult getAdvertisement(int page, int size);

    SaResult getAdvertisement(String title, String mode, int page, int size);

    SaResult deleteAdvertisement(String id);
}
