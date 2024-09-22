package org.example.mymallapplication.dal.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

public enum State implements IEnum<Integer> {
    UNPAID(1, "创建成功，未付款"),
    PAID(2, "已付款，待发货"),
    TRANSPORT(3, "已发货，运输中"),
    LANDING(4, "已到达，未签收"),
    END(5, "已签收"),
    DELETED(6, "已删除");

    private final Integer value;
    @Getter
    private final String description;

    State(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return value;
    }

}
