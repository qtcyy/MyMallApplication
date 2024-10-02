package org.example.mymallapplication.dal.service;

import org.example.mymallapplication.dal.config.RabbitMQConfig;
import org.example.mymallapplication.dal.dao.entity.product.Orders;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Async;

/**
 * @author chengyiyang
 */
public interface ScheduledService {

    void processDelayedOrders();

    void processLandingOrders();

    @RabbitListener(queues = RabbitMQConfig.ORDER_CONFIRM_QUEUE_NAME)
    @Async
    void processOrderConfirm(Orders order, @Header(value = "handle-confirm", required = false) String headerKey);

    @Async
    @RabbitListener(queues = RabbitMQConfig.DELAYED_CANCEL_QUEUE_NAME)
    void processOrderDelayedCancel(Orders order);
}
