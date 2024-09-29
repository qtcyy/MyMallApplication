package org.example.mymallapplication.dal.dao.service.person;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.person.AdminBalance;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-28
 */
public interface IAdminBalanceService extends IService<AdminBalance> {

    AdminBalance getAdminBalance(String adminId);
}
