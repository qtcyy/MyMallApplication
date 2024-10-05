package org.example.mymallapplication.dal.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

public enum AdStatus implements IEnum<Integer> {
    NORMAL(1, "正常"),
    DISABLE(2, "禁用");

    private final Integer value;
    @Getter
    private final String description;

    AdStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return value;
    }

}
