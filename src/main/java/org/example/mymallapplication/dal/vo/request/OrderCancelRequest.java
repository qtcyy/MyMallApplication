package org.example.mymallapplication.dal.vo.request;

import lombok.Data;

@Data
public class OrderCancelRequest {
    private Long orderId;
    private String reason;
}
