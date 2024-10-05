package org.example.mymallapplication.dal.dao.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mymallapplication.dal.dao.entity.product.Advertisements;
import org.example.mymallapplication.dal.dao.mapper.product.AdvertisementsMapper;
import org.example.mymallapplication.dal.dao.service.product.IAdvertisementsService;
import org.example.mymallapplication.dal.enums.AdStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qtcyy
 * @since 2024-10-05
 */
@Service
public class AdvertisementsServiceImpl extends ServiceImpl<AdvertisementsMapper, Advertisements> implements IAdvertisementsService {

    /**
     * 获取当前可用的广告
     *
     * @return 广告列表
     */
    @Override
    public List<Advertisements> getAvailableAdvertisements() {
        LambdaQueryWrapper<Advertisements> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Advertisements::getStartTime, LocalDateTime.now())
                .le(Advertisements::getEndTime, LocalDateTime.now())
                .eq(Advertisements::getStatus, AdStatus.NORMAL)
                .orderBy(true, false, Advertisements::getPriority);
        return list(queryWrapper);
    }

    /**
     * 获取未过期的广告（用于管理员curl）
     *
     * @return 广告列表
     */
    @Override
    public List<Advertisements> getUnexpiredAds() {
        LambdaQueryWrapper<Advertisements> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Advertisements::getEndTime, LocalDateTime.now())
                .orderBy(true, false, Advertisements::getPriority);
        return list(queryWrapper);
    }

    /**
     * 获取未过期的广告（用于管理员curl）分页
     *
     * @param page 页面信息
     * @return 广告
     */
    @Override
    public IPage<Advertisements> getUnexpiredAdsPage(Page<Advertisements> page) {
        LambdaQueryWrapper<Advertisements> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Advertisements::getEndTime, LocalDateTime.now())
                .orderBy(true, false, Advertisements::getPriority);
        return page(page, queryWrapper);
    }

    /**
     * 获取广告
     *
     * @param page 分页信息
     * @param mode 模式
     * @return 广告
     */
    @Override
    public IPage<Advertisements> getAdsPage(Page<Advertisements> page, String title, String mode) {
        LambdaQueryWrapper<Advertisements> queryWrapper = new LambdaQueryWrapper<>();

        switch (mode) {
            case "all":
                queryWrapper.like(Advertisements::getTitle, title)
                        .orderBy(true, true, Advertisements::getStartTime);
                break;
            case "unexpired":
                queryWrapper.like(Advertisements::getTitle, title)
                        .ge(Advertisements::getEndTime, LocalDateTime.now())
                        .orderBy(true, true, Advertisements::getStartTime);
                break;
            default:
                break;
        }

        return page(page, queryWrapper);
    }
}
