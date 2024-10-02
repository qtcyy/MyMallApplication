package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author chengyiyang
 */
@Data
public class AddCartRequest {
    @NotBlank(message = "商品ID不能为空")
    private String productId;

    @NotBlank(message = "商品数量不能为空")
    @Max(value = 50L)
    @Min(value = 1)
    private Integer quantity;
}
