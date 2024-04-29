package com.lordgasmic.wineservice.service;

import com.lordgasmic.wineservice.models.WineImageTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${lordgasmic.rabbitmq.exchange}")
    private String exchange;

    @Value("${lordgasmic.rabbitmq.routingKey}")
    private String routingKey;

    @Value("${lordgasmic.rabbitmq.routingKey2}")
    private String routingKey2;

    public void send(final WineImageTransfer transfer) {
        rabbitTemplate.convertAndSend(exchange, routingKey, transfer);
        rabbitTemplate.convertAndSend(exchange, routingKey2, mutate(transfer));
        log.info("Send msg = " + transfer);
    }

    private static WineImageTransfer mutate(final WineImageTransfer transfer) {
        final WineImageTransfer x2 = new WineImageTransfer();
        x2.setData(transfer.getData() + " key2");
        x2.setName(transfer.getName() + " name 222");
        return x2;
    }
}
