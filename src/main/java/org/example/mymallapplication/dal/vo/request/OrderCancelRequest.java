package org.example.mymallapplication.dal.vo.request;

import lombok.Data;

@Data
public class OrderCancelRequest {
    private String orderId;
    private String reason;
}
