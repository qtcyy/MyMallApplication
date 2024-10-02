package org.example.mymallapplication.dal.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chengyiyang
 */
@Configuration
public class RabbitMQConfig {

    // 队列名称
    public static final String DELAYED_QUEUE_NAME = "orderDelayedQueue";
    public static final String DELAYED_CANCEL_QUEUE_NAME = "orderCancelQueue";
    public static final String ORDER_CONFIRM_QUEUE_NAME = "orderConfirmQueue";
    // 交换机名称
    public static final String DELAYED_EXCHANGE_NAME = "orderDelayedExchange";
    public static final String ORDER_CONFIRM_EXCHANGE_NAME = "orderConfirmExchange";
    // 路由键
    public static final String ROUTING_KEY = "order.delayed.key";
    public static final String ORDER_CANCEL_ROUTING_KEY = "order.cancel.delay.key";
    public static final String ORDER_CONFIRM_ROUTING_KEY = "order.confirm.key";

    //序列化转换
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //创建队列
    @Bean
    public Queue delayedCancelQueue() {
        return QueueBuilder.durable(DELAYED_CANCEL_QUEUE_NAME).build();
    }

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE_NAME).build();
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(ORDER_CONFIRM_QUEUE_NAME).build();
    }

    //创建交换机
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public TopicExchange confirmExchange() {
        return new TopicExchange(ORDER_CONFIRM_EXCHANGE_NAME);
    }

    //绑定队列与交换机
    @Bean
    public Binding delayedCancelBinding(Queue delayedCancelQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedCancelQueue).to(delayedExchange).with(ORDER_CANCEL_ROUTING_KEY).noargs();
    }

    @Bean
    public Binding delyedBinding(Queue delayedQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(ROUTING_KEY).noargs();
    }

    @Bean
    public Binding confirmBinding(@Qualifier("confirmQueue") Queue confimQueue, TopicExchange confirmExchange) {
        return BindingBuilder.bind(confimQueue).to(confirmExchange).with(ORDER_CONFIRM_ROUTING_KEY);
    }

    //设置监听容器
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(converter);
        factory.setConnectionFactory(connectionFactory);
        factory.setPrefetchCount(1000);
        factory.setConcurrentConsumers(4);
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }
}
