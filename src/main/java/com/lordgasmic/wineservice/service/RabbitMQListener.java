package com.lordgasmic.wineservice.service;

import com.lordgasmic.wineservice.models.WineImageTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQListener {

    @RabbitListener(queues = "${lordgasmic.rabbitmq.queue}")
    public void recievedMessage(final WineImageTransfer transfer) {
        log.info("Recieved Message From RabbitMQ " + transfer);
    }

    @RabbitListener(queues = "${lordgasmic.rabbitmq.queue2}")
    public void recievedMessage2(final WineImageTransfer transfer) {
        log.info("Queue 2 From RabbitMQ " + transfer);
    }
}
