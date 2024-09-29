package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BuyProductRequest {
    @NotBlank(message = "产品ID不能为空")
    private String productId;
    @NotBlank(message = "购买数量不能为空")
    private int number;
    private String remark;
}
