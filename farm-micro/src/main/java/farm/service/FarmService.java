package farm.service;

import java.util.ArrayList;
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

import farm.dto.EditDto;
import farm.dto.OrderDto;
import farm.dto.RatingDto;
import farm.model.Address;
import farm.model.BankAccountDeatil;
import farm.model.Crop;
import farm.model.FarmModel;
import farm.model.OrderModel;
import farm.model.UserLogin;
import farm.repository.FarmRepository;
import farm.repository.OrderRepository;
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

	@Autowired
	public OrderRepository orderRepo;

	public boolean register(FarmModel farmModel) {
		farmModel.setStatus("Active");
		farmRepo.save(farmModel);

		UserLogin login = new UserLogin(farmModel.getUserName(), farmModel.getPassword(), "dealer");
		rabbitTemplate.convertAndSend(exchange, routingkey, login);
		System.out.println("Send msg = " + login);

		ArrayList<String> Emails = new ArrayList<>();
		Emails.add(farmModel.getEmail());

		webClientBuilder.build().post().uri("http://localhost:8000/farmer/EmailList")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(Emails), ArrayList.class).accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToMono(boolean.class).block();

		return true;
	}

	public List<Crop> getFarmers() {
		List<Crop> response = webClientBuilder.build().get().uri("http://Farmer/farmer/getCrops")
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(new ParameterizedTypeReference<List<Crop>>() {
				}).block();

		return response;
	}

	public void removeDealer(String userName) {
		FarmModel farmModel = farmRepo.findById(userName).get();
		farmModel.setStatus("Inactive");
		farmRepo.save(farmModel);
	}

	public boolean editProfile(EditDto farm) {
		FarmModel farmModel = farmRepo.findById(farm.getUserName()).get();
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
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(dto), RatingDto.class).accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToMono(boolean.class).block();

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

	public FarmModel getDetails(String userName) {
		FarmModel farmModel = farmRepo.findById(userName).get();
		return farmModel;
	}

	public List<FarmModel> getAllFarm() {

		return farmRepo.findAll();
	}

	public List<OrderDto> getOrders() {
		List<OrderModel>orders=orderRepo.findAll();
		List<OrderDto> ordersDto=new ArrayList<>();
		for(OrderModel i:orders)
		{
			ordersDto.add(new OrderDto(i.getFarmModel().getUserName(),i.getOrderId(),i.getDate(),
					i.getCropName(),i.getCropType(),i.getCropPrice(),i.getCropId(),i.getQuantity(),i.getCost()));
		}
		return ordersDto;
		
	}

	public Address getAddress(int id) {

		return webClientBuilder.build().get().uri("http://Farmer/farmer/getAddress/" + id)
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Address.class).block();
	}

	public BankAccountDeatil getbankDetails(int id) {

		return webClientBuilder.build().get().uri("http://Farmer/farmer/getbankDetails/" + id)
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(BankAccountDeatil.class).block();
	}

}
