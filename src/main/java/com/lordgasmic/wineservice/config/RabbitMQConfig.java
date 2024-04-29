package com.lordgasmic.wineservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${lordgasmic.rabbitmq.queue}")
    private String queueName;

    @Value("${lordgasmic.rabbitmq.queue2}")
    private String queue2Name;

    @Value("${lordgasmic.rabbitmq.exchange}")
    private String exchange;

    @Value("${lordgasmic.rabbitmq.routingKey}")
    private String routingKey;

    @Value("${lordgasmic.rabbitmq.routingKey2}")
    private String routingKey2;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Declarables queues() {
        return new Declarables(new Queue(queueName, false), new Queue(queue2Name, false));
    }

    @Bean
    public Declarables bindings() {
        return new Declarables(new Binding(queueName, Binding.DestinationType.QUEUE, exchange, routingKey, null),
                               new Binding(queue2Name, Binding.DestinationType.QUEUE, exchange, routingKey2, null));
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
