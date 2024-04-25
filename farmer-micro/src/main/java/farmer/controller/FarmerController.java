package farmer.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import farmer.dto.CropDto;
import farmer.dto.EditDto;
import farmer.dto.FarmerDto;
import farmer.dto.RatingDto;
import farmer.model.Address;
import farmer.model.BankAccountDeatil;
import farmer.model.Crop;
import farmer.model.FarmerModel;
import farmer.model.SoldCrops;
import farmer.serviceInterface.FarmerServiceInterface;

@RestController
@RequestMapping("/farmer")
@CrossOrigin("*")
public class FarmerController {

	@Autowired
	public FarmerServiceInterface farmerService;
	
	@GetMapping("/getAllFarmer")
	public List<FarmerModel> getFarmers()
	{
		return farmerService.getFarmers();
	}
	

	@GetMapping("/getCrops")
	public List<Crop> getFarmerCrops() {
		return farmerService.getFarmerCrops();
	}

	@DeleteMapping("/deletefarmer/{userName}")
	public void deleteFarmer(@PathVariable String userName) {
		farmerService.removeFamer(userName);
	}


	@PostMapping("/register")
	public boolean register(@RequestBody FarmerDto farmerModel) {
		return farmerService.register(farmerModel);
	}

	@PostMapping("/addCrop")
	public boolean addCrop(@RequestBody CropDto crop,ServerHttpRequest request) {
		return farmerService.addCrop(crop,request);
	}

	@DeleteMapping("/removeCrop/{userName}/{id}")
	public Boolean removeCrop(@PathVariable String userName ,@PathVariable Integer id,ServerHttpRequest request) {
		return farmerService.removeCrop(userName,id,request);
	}

	@PostMapping("/rating")
	public boolean rateFarmer(@RequestBody RatingDto dto) {
	 return	farmerService.rateFarmer(dto);
	}
	
	@PutMapping("/editProfile")
	public boolean editProfile(@RequestBody EditDto dto,ServerHttpRequest request)
	{
		return farmerService.editProfile(dto,request);
	}
	
	@GetMapping("/getAddress/{id}")
	public Address getAddress(@PathVariable int id)
	{
		return farmerService.getAddress(id);
	}

	@GetMapping("/getbankDetails/{id}")
	public BankAccountDeatil getbaknDetails(@PathVariable int id)
	{
		return farmerService.getbankDetails(id);
	}
	
	@PostMapping("/quantityManagement")
	public Boolean quantityManagement(@RequestBody HashMap<Integer, Integer> CropIds)
	{
		return farmerService.quantityManagement(CropIds);
	}
	
	@PostMapping("/EmailList")
	public Boolean SendEmail( @RequestBody String Emails) throws AddressException, MessagingException, IOException 
	{
		return farmerService.saveFarmEmail(Emails);
	}
	
	@GetMapping("/getFarmerDetails/{userName}")
	public FarmerDto getDetails(@PathVariable String userName,ServerHttpRequest request)
	{
		return farmerService.getFarmerDetails(userName,request);
	}
	
	@GetMapping("/getFarmerCrops/{userName}")
	public List<Crop> getFarmerCrops(@PathVariable  String userName,ServerHttpRequest request)
	{
		return farmerService.getFarmerCrops(userName, request);
	}
	@GetMapping("/getSoldCrops/{userName}")
	public List<SoldCrops> getSoldCrops(@PathVariable String userName,ServerHttpRequest request)
	{
		return farmerService.getsoldCrops(userName,request);
	}
	
}
