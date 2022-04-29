package farm.service;

import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.common.net.HttpHeaders;

import farm.dto.RatingDto;
import farm.model.Crop;
import farm.model.FarmModel;
import farm.model.UserLogin;
import farm.repository.FarmRepository;
import reactor.core.publisher.Mono;

@Service
public class FarmService {

	@Autowired
	private AmqpTemplate rabbitTemplate;
	
	@Value("${tsanchit92.farm.exchange}")
	private String exchange;
	
	@Value("${tsanchit92.farm.routingkey}")
	private String routingkey;
	
	@Autowired
	private WebClient.Builder webClientBuilder;

	

	@Autowired
	public FarmRepository farmRepo;

	public boolean register(FarmModel farmModel) {
		
		farmRepo.save(farmModel);
		
		UserLogin login = new UserLogin(farmModel.getUserName(),farmModel.getPassword(),"dealer");
		rabbitTemplate.convertAndSend(exchange, routingkey, login);
		System.out.println("Send msg = " + login);
		
		return true;
	}

	public List<Crop> getFarmers() {
		List<Crop> response = webClientBuilder.build().get().uri("http://farmer/farmer/getCrops")
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(new ParameterizedTypeReference<List<Crop>>() {
				}).block();

		return response;
	}

	public void removeDealer(int farmId) {
		farmRepo.deleteById(farmId);
	}

	public boolean editProfile(FarmModel farm) {
		FarmModel farmModel = farmRepo.getById(farm.getFarmId());
		farmModel.setName(farm.getName());
		farmModel.setUserName(farm.getUserName());
		farmModel.setPassword(farm.getPassword());
		farmModel.setEmail(farm.getEmail());
		farmModel.setContact(farm.getContact());
		farmRepo.save(farmModel);

		return true;
	}

	public boolean rateFarmer(RatingDto dto) {

		return webClientBuilder.build().post().uri("http://farmer/farmer/rating")
				.header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE).body(Mono.just(dto),RatingDto.class)
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(boolean.class).block();

	}
	@Autowired
    private JavaMailSender javaMailSender;

	public void sendEmail() {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("tsanchit92@gmail.com");

        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");

        javaMailSender.send(msg);

    }

}
