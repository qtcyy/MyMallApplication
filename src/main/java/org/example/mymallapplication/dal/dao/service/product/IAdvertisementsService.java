package org.example.mymallapplication.dal.dao.service.product;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.mymallapplication.dal.dao.entity.product.Advertisements;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qtcyy
 * @since 2024-10-05
 */
public interface IAdvertisementsService extends IService<Advertisements> {

    List<Advertisements> getAvailableAdvertisements();

    List<Advertisements> getUnexpiredAds();

    IPage<Advertisements> getUnexpiredAdsPage(Page<Advertisements> page);

    IPage<Advertisements> getAdsPage(Page<Advertisements> page, String title, String mode);
}
