package org.example.mymallapplication.dal.vo.request;

import lombok.Data;

@Data
public class BuyProductRequest {
    private Long productId;
    private int number;
    private String remark;
}
