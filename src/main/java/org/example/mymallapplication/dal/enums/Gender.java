package org.example.mymallapplication.dal.enums;


import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

public enum Gender implements IEnum<Integer> {
    MALE(1, "男"),
    FEMALE(2, "女");

    private final Integer value;
    @Getter
    private final String description;

    Gender(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
