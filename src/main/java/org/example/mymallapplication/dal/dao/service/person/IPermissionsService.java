package org.example.mymallapplication.dal.dao.service.person;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.person.Permissions;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-09-18
 */
public interface IPermissionsService extends IService<Permissions> {

    List<String> getPermissions(List<String> ids);
}
