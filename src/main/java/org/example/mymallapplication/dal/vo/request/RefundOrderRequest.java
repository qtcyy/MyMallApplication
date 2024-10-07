package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author chengyiyang
 */
@Data
public class RefundOrderRequest {
    @NotBlank(message = "订单号不能为空")
    private String orderId;
    @NotBlank(message = "退款原因不能为空")
    private String reason;
}
