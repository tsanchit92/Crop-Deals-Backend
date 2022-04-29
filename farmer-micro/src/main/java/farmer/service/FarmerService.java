package farmer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import farmer.dto.CropDto;
import farmer.dto.FarmerDto;
import farmer.dto.RatingDto;
import farmer.model.Address;
import farmer.model.BankAccountDeatil;
import farmer.model.Crop;
import farmer.model.FarmerModel;
import farmer.model.UserLogin;
import farmer.repopsitory.AddressRepository;
import farmer.repopsitory.BankAccountRepository;
import farmer.repopsitory.CropRespository;
import farmer.repopsitory.FarmerRepository;

@Service
public class FarmerService {
	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Value("${tsanchit92.rabbitmq.exchange}")
	private String exchange;

	@Value("${tsanchit92.rabbitmq.routingkey}")
	private String routingkey;

	@Autowired
	public CropRespository cropRepo;
	@Autowired
	public BankAccountRepository bankRepo;
	@Autowired
	public AddressRepository addressRepo;
	@Autowired
	public FarmerRepository repo;

	public boolean register(FarmerDto farmerDto) {

		Address address = new Address(farmerDto.getHouseNo(), farmerDto.getLocality(), farmerDto.getTown(),
				farmerDto.getDistrict(), farmerDto.getState(), farmerDto.getPostalCode(), null);
		addressRepo.save(address);

		BankAccountDeatil bankAccountDeatil = new BankAccountDeatil(farmerDto.getBankAccountHolderName(),
				farmerDto.getBankAccountNo(), farmerDto.getIfscCode());
		bankRepo.save(bankAccountDeatil);

		Crop crops = new Crop(farmerDto.getId(), farmerDto.getCropName(), farmerDto.getCropType(),
				farmerDto.getCropQuantity(), farmerDto.getPrice(), null);
		cropRepo.save(crops);

		FarmerModel farmerModel = new FarmerModel(farmerDto.getFirstName(), farmerDto.getLastName(),
				farmerDto.getEmail(), farmerDto.getContact(), farmerDto.getUserName(), farmerDto.getPassword(), address,
				new ArrayList<>(), bankAccountDeatil, farmerDto.getRating());

		farmerModel.getCrops().add(crops);
		crops.setFarmer(farmerModel);
		address.setFarmer(farmerModel);
		cropRepo.save(crops);
		addressRepo.save(address);
		repo.save(farmerModel);

		UserLogin login = new UserLogin(farmerDto.getUserName(), farmerDto.getPassword(), "farmer");
		rabbitTemplate.convertAndSend(exchange, routingkey, login);
		System.out.println("Send msg = " + login);

		return true;

	}

	public boolean addCrop(CropDto crop) {
		FarmerModel farmer = repo.getById(crop.getUserName());
		Crop crops = new Crop(crop.getId(), crop.getCropName(), crop.getCropType(), crop.getCropQuantity(),
				crop.getPrice(), farmer);
		cropRepo.save(crops);
		farmer.getCrops().add(crops);
		cropRepo.save(crops);
		return true;
	}

	public boolean removeCrop(CropDto cropdto) {

		FarmerModel farmerModel = repo.findById(cropdto.getUserName()).get();
		for (Crop crop : farmerModel.getCrops()) {
			if (crop.getId() == cropdto.getId()) {

				farmerModel.getCrops().remove(crop);
				cropRepo.delete(crop);
				repo.save(farmerModel);

			}
		}

		return true;
	}

	public List<Crop> getFarmerCrops() {
		List<Crop> finalCrop= new ArrayList<>();
		List<Crop> crop = cropRepo.findAll();
		for(Crop i:crop)
		{
			if(i.getCropQuantity()>0)
			{
				finalCrop.add(i);
			}
		}
		return finalCrop;

	}

	public void removeFamer(String userName) {

		repo.deleteById(userName);

	}

	public boolean rateFarmer(RatingDto dto) {

		FarmerModel farmer;
		farmer = repo.getById(dto.getUserId());
		farmer.setRating(dto.getRating());
		repo.save(farmer);
		return true;

	}

	public boolean editProfile(FarmerDto dto) {
		FarmerModel farmer = new FarmerModel();
		farmer = repo.getById(dto.getUserName());
		farmer.setFirstName(dto.getFirstName());
		farmer.setLastName(dto.getLastName());
		farmer.setEmail(dto.getEmail());
		farmer.setContact(dto.getContact());

		Address address = new Address(dto.getHouseNo(), dto.getLocality(), dto.getTown(), dto.getDistrict(),
				dto.getState(), dto.getPostalCode(), null);
		farmer.setAddress(address);

		BankAccountDeatil bankAccountDeatil = new BankAccountDeatil(dto.getBankAccountHolderName(),
				dto.getBankAccountNo(), dto.getIfscCode());

		farmer.setBankAccountDeatil(bankAccountDeatil);
		repo.save(farmer);

		return true;
	}

	public Address getAddress(String id) {

		FarmerModel farmerModel = repo.findById(id).get();
		return farmerModel.getAddress();

	}

	public BankAccountDeatil getbankDetails(String id) {

		FarmerModel farmerModel = repo.findById(id).get();
		return farmerModel.getBankAccountDeatil();
	}

	public Boolean quantityManagement(HashMap<Integer, Integer> CropIds) {
		Iterator<Map.Entry<Integer, Integer>> itr = CropIds.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<Integer, Integer> entry = itr.next();

			Crop crop = cropRepo.findById(entry.getKey()).get();
			crop.setCropQuantity(crop.getCropQuantity() - entry.getValue());
			;
		}
		return null;
	}

}
