package farmer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import farmer.dto.FarmerDto;
import farmer.model.Address;
import farmer.model.BankAccountDeatil;
import farmer.model.Crop;
import farmer.model.FarmerModel;
import farmer.repopsitory.AddressRepository;
import farmer.repopsitory.BankAccountRepository;
import farmer.repopsitory.CropRespository;
import farmer.repopsitory.FarmerRepository;

@SpringBootTest
@AutoConfigureMockMvc
class FarmerMicroApplicationTests {

	@Mock
	BankAccountRepository bankRepo;

	@MockBean
	FarmerRepository repo;

	@MockBean
	AddressRepository addrepo;

	@MockBean
	CropRespository cropRepo;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;

	@Test
	void getAllFarmer() throws Exception {
		FarmerDto farmerDto = new FarmerDto("San", "Tri", "tsan92@gmail.com", 12345678909L, "san12", "san987", 0, "123",
				"bbbd", "bbd", "bd", "d", 2234, 12, "rawa", "raw", 120, 120, 87654333221L, "ascv990", "San Tri");

		Address address = new Address(farmerDto.getHouseNo(), farmerDto.getLocality(), farmerDto.getTown(),
				farmerDto.getDistrict(), farmerDto.getState(), farmerDto.getPostalCode(), null);
		addrepo.save(address);

		BankAccountDeatil bankAccountDeatil = new BankAccountDeatil(farmerDto.getBankAccountHolderName(),
				farmerDto.getBankAccountNo(), farmerDto.getIfscCode());
		bankRepo.save(bankAccountDeatil);

		Crop crops = new Crop(farmerDto.getCropName(), farmerDto.getCropType(), farmerDto.getCropQuantity(),
				farmerDto.getPrice(), null);
		cropRepo.save(crops);

		FarmerModel farmerModel = new FarmerModel(farmerDto.getFirstName(), farmerDto.getLastName(),
			farmerDto.getEmail(), farmerDto.getContact(), "Active", farmerDto.getUserName(),
		farmerDto.getPassword(), address, new ArrayList<>(), bankAccountDeatil, farmerDto.getRating());
		
		List<FarmerModel> farmer= new ArrayList<>();
		farmer.add(farmerModel);

		Mockito.when(repo.findAll()).thenReturn(farmer);
		mockMvc.perform(MockMvcRequestBuilders.get("/farmer/getAllFarmer").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.firstName", is("San")));
	}

}
