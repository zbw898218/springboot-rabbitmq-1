package com.didispace.rabbit;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * @author 翟永超
 * @create 2016/9/25.
 * @blog http://blog.didispace.com
 */
@Configuration
public class RabbitConfig {
    @Autowired
    private ConnectionFactory factory;

    @Value("${spring.apollo.exchange.name}")
    private String exchangeName;
    @Value("${spring.apollo.queue.name}")
    private String queueName;

    @Bean
    public TopicExchange mqExchange() {
        return new TopicExchange(exchangeName) {};
    }

    @Bean
    public Queue queueMessage() {
        return new Queue(queueName, true, false, true);
    }

    @Bean
    public Binding bindingExchangeMessage(Queue queueMessage, TopicExchange mqExchange) {
        return BindingBuilder.bind(queueMessage).to(mqExchange).with("#.apollo");
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer1(){
        SimpleMessageListenerContainer smlContainer = new SimpleMessageListenerContainer(factory);
        smlContainer.setQueues(queueMessage());
        smlContainer.setExposeListenerChannel(true);
        smlContainer.setMaxConcurrentConsumers(1);
        smlContainer.setConcurrentConsumers(1);
        smlContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        smlContainer.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                byte[] body = message.getBody();
                System.out.println("receive msg 2 :" + new String(body));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        });

        return smlContainer;
    }
}
