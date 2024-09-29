package org.example.mymallapplication.dal.dao.service.person;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.person.Balance;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-25
 */
public interface IBalanceService extends IService<Balance> {

    Balance getBalanceByUserId(String userId);

    List<Balance> getBalance(List<String> adminIds);
}
