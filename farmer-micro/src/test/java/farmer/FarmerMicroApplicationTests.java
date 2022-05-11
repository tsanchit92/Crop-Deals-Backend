package farmer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import farmer.model.Address;
import farmer.model.BankAccountDeatil;
import farmer.model.Crop;
import farmer.model.FarmerModel;
import farmer.repopsitory.AddressRepository;
import farmer.repopsitory.BankAccountRepository;
import farmer.repopsitory.CropRespository;
import farmer.repopsitory.FarmerRepository;
import farmer.serviceInterface.FarmerServiceInterface;

@SpringBootTest
class FarmerMicroApplicationTests {

	@Autowired
	FarmerServiceInterface farmerService;

	@Autowired
	CropRespository cropRepository;
	
	@Autowired
	AddressRepository addrepo;
	
	@Autowired
	BankAccountRepository bankRepo;

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
	
	@Test
	public void Footest1(String id) throws Exception
	{
		FarmerModel farmerModel = repo.findById(id).get();
		Address address = farmerModel.getAddress();
		assertEquals(farmerModel.getAddress(), address);
	}
	
	@Test
	public void FooTest2(String id)
	{
		FarmerModel farmerModel = repo.findById(id).get();
		BankAccountDeatil bankDetail = farmerModel.getBankAccountDeatil();
		assertEquals(farmerModel.getBankAccountDeatil(), bankDetail);
	}

	

}
