package com.top.apiwallet.event;

import com.top.apiwallet.config.RabbitMQConfig;
import com.top.apiwallet.exceptions.WalletExceptions;
import com.top.apiwallet.model.Currency;
import com.top.apiwallet.model.Wallet;
import com.top.apiwallet.services.WalletService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventConsumer {

    @Autowired
    private WalletService walletService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CUSTOMER)
    public void listen(Data msg) throws WalletExceptions {

        Wallet wallet = new Wallet(msg.docType, msg.docNum,
                new Currency("BTC", null), 1.0);

        walletService.add(wallet);

        System.out.println("Creando wallet con: doctype: " + msg.docType + " - Docnumber: "  + msg.docNum);
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
