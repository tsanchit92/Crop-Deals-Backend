package farmer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

	@Mock
	FarmerServiceInterface farmerService;

	@Mock
	CropRespository cropRepository;
	
	@Mock
	AddressRepository addrepo;
	
	@Mock
	BankAccountRepository bankRepo;

	@Mock
	FarmerRepository repo;

	@Test
	void contextLoads() {
	}

	@Test
	public void FooTest() throws Exception {
		List<Crop> f = farmerService.getFarmerCrops();
		assertEquals(cropRepository.findAll(), f);
	}
	

	


	

}
