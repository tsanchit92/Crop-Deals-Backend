package farm.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import farm.dto.RatingDto;
import farm.model.Crop;
import farm.model.FarmModel;
import farm.service.FarmService;

@RestController
@RequestMapping("/farm")
public class FarmController {
	@Autowired
	private WebClient.Builder webClientBuilder;

	@Autowired
	public FarmService service;

	@GetMapping("/getStarted")
	public String getStr() {
		String response = webClientBuilder.build().get().uri("http://farmer/farmer/started")
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class).block();

		return response;
	}

	@PostMapping("/register")
	public boolean register(@RequestBody FarmModel farmModel) {
		return service.register(farmModel);
	}

	@GetMapping("/getCrops")
	public List<Crop> getFarmers() {
		return service.getFarmers();
	}

	@DeleteMapping("/removeFarm/{farmId}")
	public void removeFarm(@PathVariable int farmId) {
		service.removeDealer(farmId);
	}

	@PostMapping("/editProfile")
	public boolean editProfile(@RequestBody FarmModel farm) {
		return service.editProfile(farm);
	}

	@PostMapping("/rateFarmer")
	public boolean rateFarmer(@RequestBody RatingDto dto) {
		return service.rateFarmer(dto);
	}

	@GetMapping(value = "/sendemail")
	public String send() throws AddressException, MessagingException, IOException {

		service.sendEmail();
		return "Email sent successfully";
	}

}
