package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author chengyiyang
 */
@Data
public class OrderCancelRequest {
    @NotBlank(message = "订单ID不能为空")
    private String orderId;
    @NotBlank(message = "原因ID不能为空")
    private String reason;
}
