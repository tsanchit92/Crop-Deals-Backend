package com.component;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.UserLogin;
import com.repository.LoginRepository;

@Component
public class RabbitMQConsumer {

	@Autowired
	LoginRepository repo;

	@Bean
	public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
		return new Jackson2JsonMessageConverter(objectMapper);
	}

	@RabbitListener(queues = "${tsanchit92.rabbitmq.queue}")
	public void recievedFarmerMessage(UserLogin login) {

		repo.save(login);
		System.out.println("Recieved Message From RabbitMQ: " + login);
	}

	@RabbitListener(queues = "${tsanchit92.farm.queue}")
	public void recievedFarmMessage(UserLogin login) {

		repo.save(login);
		System.out.println("Recieved Message From RabbitMQ: " + login);
	}
}
