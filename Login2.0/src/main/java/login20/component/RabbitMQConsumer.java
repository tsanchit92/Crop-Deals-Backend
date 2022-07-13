package login20.component;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import login20.model.LoginUser;
import login20.repo.Repository;

@Component
public class RabbitMQConsumer {

	@Autowired
	Repository repo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
		return new Jackson2JsonMessageConverter(objectMapper);
	}

	@RabbitListener(queues = "${tsanchit92.rabbitmq.queue}")
	public void recievedFarmerMessage(LoginUser login) {
		
		login.setPassword(passwordEncoder.encode(login.getPassword()));
		repo.save(login);
		System.out.println("Recieved Message From RabbitMQ: " + login);
	}

	@RabbitListener(queues = "${tsanchit92.farm.queue}")
	public void recievedFarmMessage(LoginUser login) {

		login.setPassword(passwordEncoder.encode(login.getPassword()));
		repo.save(login);
		System.out.println("Recieved Message From RabbitMQ: " + login);
	}
}
