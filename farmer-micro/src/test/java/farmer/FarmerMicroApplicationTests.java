package farmer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import farmer.model.Address;
import farmer.model.Crop;
import farmer.model.FarmerModel;
import farmer.repopsitory.CropRespository;
import farmer.repopsitory.FarmerRepository;
import farmer.service.FarmerService;

@SpringBootTest
class FarmerMicroApplicationTests {

	@Autowired
	FarmerService farmerService;

	@Autowired
	CropRespository cropRepository;

	@Autowired
	FarmerRepository repo;

	@Test
	void contextLoads() {
	}

	@Test
	public void FooTest() throws Exception {
		List<Crop> f = farmerService.getFarmerCrops();
		assertEquals(cropRepository.findAll(), f);
	}

	/*
	 * @Test public void FooTest1() throws Exception {
	 * 
	 * Address f = farmerService.getAddress(id); FarmerModel farmerModel =
	 * repo.findById(id).get(); assertEquals(farmerModel.getAddress(), f); }
	 */

}
