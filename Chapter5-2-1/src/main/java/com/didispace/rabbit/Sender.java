package com.didispace.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class Sender implements RabbitTemplate.ConfirmCallback{
	@Autowired
	private RabbitTemplate rabbitTemplate;

	private static int index = 1;
	public String send() {
		CorrelationData correlationId = new CorrelationData(String.valueOf(index));
		String context = index++ + " hello " + new Date();
		System.out.println("Sender : " + context);
		this.rabbitTemplate.setConfirmCallback(this);
		MessageProperties msgProperties = new MessageProperties();
		Message message = new Message(context.getBytes(), msgProperties);
		Message msgResponse = this.rabbitTemplate.sendAndReceive("exchange.apollo","queue.apollo", message, correlationId);
		System.out.println("msgResponse : " + msgResponse);
		//Object object = this.rabbitTemplate.convertSendAndReceive("exchange.apollo","queue.apollo", context, correlationId);
		return "SUCCESS";
	}

	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		System.out.println("confirm : " + correlationData.getId() + "; ack : "+ ack + "; cause : " + cause);
	}
}