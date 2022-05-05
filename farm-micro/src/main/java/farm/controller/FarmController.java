package farm.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import farm.dto.EditDto;
import farm.dto.OrderDto;
import farm.dto.RatingDto;
import farm.model.Address;
import farm.model.BankAccountDeatil;
import farm.model.Crop;
import farm.model.FarmModel;
import farm.service.FarmService;

@CrossOrigin
@RestController
@RequestMapping("/farm")
public class FarmController {


	@Autowired
	public FarmService service;

	@GetMapping("/getAll")
	public List<FarmModel> getAllFarm (){
		return service.getAllFarm(); 
	}
	
	@GetMapping("/getDetails/{userName}")
	public FarmModel getDetails(@PathVariable String userName){
		return service.getDetails(userName);
	}

	@PostMapping("/register")
	public boolean register(@RequestBody FarmModel farmModel) {
		return service.register(farmModel);
	}

	@GetMapping("/getCrops")
	public List<Crop> getFarmers() {
		return service.getFarmers();
	}

	@DeleteMapping("/removeFarm/{userName}")
	public void removeFarm(@PathVariable String userName) {
		service.removeDealer(userName);
	}

	@PutMapping("/editProfile")
	public boolean editProfile(@RequestBody EditDto farm) {
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
	@GetMapping("/getOrders")
	public List<OrderDto> Orders()
	{
		return service.getOrders();
	}
	@GetMapping("/getAddress/{id}")
	public Address getAddress(@PathVariable int id)
	{
		return service.getAddress(id);
	}
	@GetMapping("/getBankAccountDetails/{id}")
	public BankAccountDeatil getBankDetails(@PathVariable int id)
	{
		return service.getbankDetails(id);
	}

}
