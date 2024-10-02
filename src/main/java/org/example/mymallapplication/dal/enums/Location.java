package org.example.mymallapplication.dal.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * @author chengyiyang
 */

public enum Location implements IEnum<Integer> {
    // 直辖市
    BEIJING(1, "北京市"),
    TIANJIN(2, "天津市"),
    SHANGHAI(3, "上海市"),
    CHONGQING(4, "重庆市"),

    // 省
    HEBEI(5, "河北省"),
    SHANXI(6, "山西省"),
    LIAONING(7, "辽宁省"),
    JILIN(8, "吉林省"),
    HEILONGJIANG(9, "黑龙江省"),
    JIANGSU(10, "江苏省"),
    ZHEJIANG(11, "浙江省"),
    ANHUI(12, "安徽省"),
    FUJIAN(13, "福建省"),
    JIANGXI(14, "江西省"),
    SHANDONG(15, "山东省"),
    HENAN(16, "河南省"),
    HUBEI(17, "湖北省"),
    HUNAN(18, "湖南省"),
    GUANGDONG(19, "广东省"),
    HAINAN(20, "海南省"),
    SICHUAN(21, "四川省"),
    GUIZHOU(22, "贵州省"),
    YUNNAN(23, "云南省"),
    SHAANXI(24, "陕西省"),
    GANSU(25, "甘肃省"),
    QINGHAI(26, "青海省"),

    // 自治区
    NEIMENGGU(27, "内蒙古自治区"),
    GUANGXI(28, "广西壮族自治区"),
    XIZANG(29, "西藏自治区"),
    XINJIANG(30, "新疆维吾尔自治区"),
    NINGXIA(31, "宁夏回族自治区"),

    // 特别行政区
    HONGKONG(32, "香港特别行政区"),
    MACAO(33, "澳门特别行政区"),

    // 台湾省
    TAIWAN(34, "台湾省");

    private final Integer value;

    @Getter
    private final String description;

    Location(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}

