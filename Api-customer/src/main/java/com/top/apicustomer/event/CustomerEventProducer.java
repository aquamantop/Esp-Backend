package com.top.apicustomer.event;

import com.top.apicustomer.config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public CustomerEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCustomerEvent(Data msg){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME
                , RabbitMQConfig.TOPIC_CUSTOMER, msg);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private String docType;
        private String docNum;
    }

}
