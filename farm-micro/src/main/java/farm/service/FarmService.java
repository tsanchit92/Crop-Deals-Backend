package farm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.common.net.HttpHeaders;

import farm.com.util.JwtUtil;
import farm.dto.EditDto;
import farm.dto.OrderDto;
import farm.dto.RatingDto;
import farm.exception.FarmException;
import farm.model.Address;
import farm.model.BankAccountDeatil;
import farm.model.Crop;
import farm.model.FarmModel;
import farm.model.OrderModel;
import farm.model.UserLogin;
import farm.repository.FarmRepository;
import farm.repository.OrderRepository;
import farm.serviceInterface.FarmServiceInterface;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class FarmService implements FarmServiceInterface {

	@Autowired
	private AmqpTemplate rabbitTemplate;
	@Autowired
	private JavaMailSender javaMailSender;

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

	@Autowired
	public JwtUtil jwtUtil;

	@Override
	public boolean register(FarmModel farmModel) {
		// Dealer registration
		farmModel.setStatus("Active");
		farmRepo.save(farmModel);
		

		// sending registered mail to farmer microservice everytim new dealer is
		// enrolled.
		webClientBuilder.build().post().uri("http://Farmer/farmer/EmailList")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(farmModel.getEmail()), String.class).accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToMono(boolean.class).block();


		// sending data to login microservice having userName password and role with it
		UserLogin login = new UserLogin(farmModel.getUserName(), farmModel.getPassword(), "dealer");
		rabbitTemplate.convertAndSend(exchange, routingkey, login);
		log.info("Send msg = " + login);

		return true;
	}

	@Override
	public List<Crop> getFarmers() {
//calling farmer microservice for getting all the regstered crops 
		List<Crop> response = webClientBuilder.build().get().uri("http://Farmer/farmer/getCrops")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve().bodyToMono(new ParameterizedTypeReference<List<Crop>>() {
				}).block();
		return response;
	}

	@Override
	public void removeDealer(String userName) {
		// code writte that would be called by admin microservice when he wants to
		// deacticate dealer.
		FarmModel farmModel = farmRepo.findById(userName)
		.orElseThrow( ()->  new FarmException("Dealer not found",HttpStatus.BAD_REQUEST));
		farmModel.setStatus("Inactive");
		farmRepo.save(farmModel);
	}

	@Override
	public boolean editProfile(EditDto farm) {

		//called by the dealer it self to edit his feeded data
		FarmModel farmModel = farmRepo.findById(farm.getUserName())
		.orElseThrow( ()->  new FarmException("Dealer not found",HttpStatus.BAD_REQUEST));
		farmModel.setName(farm.getName());
		farmModel.setUserName(farm.getUserName());
		farmModel.setPassword(farm.getPassword());
		farmModel.setEmail(farm.getEmail());
		farmModel.setContact(farm.getContact());
		farmRepo.save(farmModel);

		return true;

	}

	@Override
	public boolean rateFarmer(RatingDto dto) {

		//called to rate farmers by dealer who have visited their crops
		return webClientBuilder.build().post().uri("http://Farmer/farmer/rating")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(dto), RatingDto.class).accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToMono(boolean.class).block();

	}



	@Override
	public void sendEmail() {
		//mail service used for testing purpose
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("singhaayush357@gmail.com");

		msg.setSubject("Testing from Spring Boot");
		msg.setText("Hello World \n Spring Boot Email");

		javaMailSender.send(msg);

	}

	@Override
	public FarmModel getDetails(String userName) {
		//called from ui to prefill data to make easy for dealer to edit it
		FarmModel farmModel = farmRepo.findById(userName)
	.orElseThrow( ()->  new FarmException("Dealer not found",HttpStatus.BAD_REQUEST));
		return farmModel;

	}

	@Override
	public List<FarmModel> getAllFarm() {
		//called by admin to see the dealers registered on the site
		return farmRepo.findAll();
	}

	@Override
	public List<OrderDto> getOrders() {
		List<OrderModel> orders = orderRepo.findAll();
		List<OrderDto> ordersDto = new ArrayList<>();
		for (OrderModel i : orders) {
			ordersDto.add(new OrderDto(i.getFarmModel().getUserName(), i.getOrderId(), i.getDate(), i.getCropName(),
					i.getCropType(), i.getCropPrice(), i.getCropId(), i.getQuantity(), i.getCost()));
		}
		return ordersDto;

	}

	@Override
	public Address getAddress(int id) {

		return webClientBuilder.build().get().uri("http://Farmer/farmer/getAddress/" + id)
		.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Address.class).block();

	}

	@Override
	public BankAccountDeatil getbankDetails(int id) {

		return webClientBuilder.build().get().uri("http://Farmer/farmer/getbankDetails/" + id)
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(BankAccountDeatil.class).block();
	}
	
	@Override
	public List<OrderModel> getFarmOrders(String userName)
	{
		FarmModel farmModel =farmRepo.findById(userName)
		.orElseThrow( ()->  new FarmException("Dealer not found",HttpStatus.BAD_REQUEST));
		return farmModel.getOrder();
	}

	@Override
	public Boolean validateToken(ServerHttpRequest request) {

		String authorizationHeader = request.getHeaders().getFirst("Authorization");

		String username = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring("Bearer ".length());
			username = jwtUtil.extractUsername(jwt);
		} else
			return false;

		if (username != null) {

			FarmModel farmModel = farmRepo.findById(username).get();
			if (farmModel != null) {
				if (jwtUtil.validateToken(jwt, farmModel.getUserName())) {

					return true;

				} else
					return false;
			} else
				return false;
		} else
			return false;
	}

}
