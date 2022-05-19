package farm;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import farm.model.CartItem;
import farm.model.FarmModel;
import farm.model.OrderModel;
import farm.repository.FarmRepository;

@SpringBootTest
@AutoConfigureMockMvc
class FarmMicroApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@MockBean
	FarmRepository repository;

	List<CartItem> cart = new ArrayList<>();
	List<OrderModel> order = new ArrayList<>();
	FarmModel farm0 = new FarmModel("San", "san12", "san123", 123456789, "tsan92@gmail.com", 0, "Active", cart, order);
	FarmModel farm1 = new FarmModel("Tan", "Tan12", "Tan123", 1234567891, "ttan92@gmail.com", 0, "Active", cart, order);
	FarmModel farm2 = new FarmModel("Van", "Van12", "Van123", 1234567893, "tvan92@gmail.com", 0, "Active", cart, order);

	@Test
	void getAllDealer() throws Exception {
		List<FarmModel> farm = new ArrayList<>();
		farm.add(farm1);
		farm.add(farm0);
		farm.add(farm2);

		repository.save(farm0);
		repository.save(farm1);
		repository.save(farm2);

		Mockito.when(repository.findAll()).thenReturn(farm);
		mockMvc.perform(MockMvcRequestBuilders.get("/farm/getAll").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$[2].name", is("Van")));
	}

	@Test
	void getFarmbyId() throws Exception {
		List<FarmModel> farm = new ArrayList<>();
		farm.add(farm1);
		farm.add(farm0);
		farm.add(farm2);

		repository.save(farm0);
		repository.save(farm1);
		repository.save(farm2);

		Mockito.when(repository.findById(farm1.getUserName())).thenReturn(java.util.Optional.of(farm1));
		mockMvc.perform(MockMvcRequestBuilders.get("/farm/getDetails/Tan12").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Tan")));
	}

	@Test
	public void register() throws Exception {

		FarmModel farm12 = new FarmModel("San", "san12", "san123", 123456789, "tsan92@gmail.com", 0, "Active", cart,
				order);

		Mockito.when(repository.save(farm12)).thenReturn(farm12);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/farm/register")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(farm12));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void editProfile() throws Exception {

		FarmModel farm12 = new FarmModel("San", "san12", "san123", 123456789, "tsan92@gmail.com", 0, "Active", cart,
				order);

		Mockito.when(repository.findById(farm12.getUserName())).thenReturn(Optional.of(farm12));
		Mockito.when(repository.save(farm12)).thenReturn(farm12);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/farm/editProfile")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(farm12));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}
	
	@Test
	public void deleteFarm() throws Exception {
		FarmModel farm12 = new FarmModel("San", "san12", "san123", 123456789, "tsan92@gmail.com", 0, "Active", cart,
				order);
		repository.save(farm12);
	    Mockito.when(repository.findById(farm12.getUserName())).thenReturn(Optional.of(farm12));

	    mockMvc.perform(MockMvcRequestBuilders
	            .delete("/farm/removeFarm/san12")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk());
	}

}
