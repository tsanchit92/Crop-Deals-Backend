package admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import admin.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	public AdminService adminService;

	@DeleteMapping("/deleteFarmer/{userName}")
	public void deleteFarmer(@PathVariable String userName) {
		adminService.deleteFarmer(userName);
	}

	@DeleteMapping("/deleteFarm/{farmId}")
	public void deleteFarm(@PathVariable int farmId) {
		adminService.deleteFarm(farmId);
	}
}
