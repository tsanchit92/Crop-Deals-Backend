package admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import admin.model.FarmModel;
import admin.model.FarmerModel;
import admin.model.OrderModel;
import admin.serviceInterface.AdminServiceInterface;
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	public AdminServiceInterface adminService;

	@DeleteMapping("/deleteFarmer/{userName}")
	public void deleteFarmer(@PathVariable String userName) {
		adminService.deleteFarmer(userName);
	}

	@DeleteMapping("/deleteFarm/{userName}")
	public void deleteFarm(@PathVariable String userName) {
		adminService.deleteFarm(userName);
	}
	@GetMapping("/getAllFarmer")
	public List<FarmerModel> getAllFarmers()
	{
		return adminService.getAllFarmers();
	}
	@GetMapping("/getAllFarm")
	public List<FarmModel> getAllFarm()
	{
		return adminService.getAllFarm();
	}
	@GetMapping("/getAllOrders")
	public List<OrderModel> getAllOrders()
	{
		
		return adminService.getAllOrders();
	}
}
