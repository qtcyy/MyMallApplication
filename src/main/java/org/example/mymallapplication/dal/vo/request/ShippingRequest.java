package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * @author chengyiyang
 */
@Data
public class ShippingRequest {
    @NotBlank(message = "订单ID不能为空")
    List<String> orderIds;
}
