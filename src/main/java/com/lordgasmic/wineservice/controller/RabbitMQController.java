package com.lordgasmic.wineservice.controller;

import com.lordgasmic.wineservice.models.WineImageTransfer;
import com.lordgasmic.wineservice.service.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMQController {

    @Autowired
    private RabbitMQSender sender;

    @PostMapping(value = "/api/v1/rabbit")
    public Object producer(@RequestBody final WineImageTransfer transfer) {
        sender.send(transfer);
        return "success";
    }
}
