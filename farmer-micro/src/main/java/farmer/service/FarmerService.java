package farmer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import farmer.dto.CropDto;
import farmer.dto.EditDto;
import farmer.dto.FarmerDto;
import farmer.dto.RatingDto;
import farmer.model.Address;
import farmer.model.BankAccountDeatil;
import farmer.model.Crop;
import farmer.model.FarmEmails;
import farmer.model.FarmerModel;
import farmer.model.UserLogin;
import farmer.repopsitory.AddressRepository;
import farmer.repopsitory.BankAccountRepository;
import farmer.repopsitory.CropRespository;
import farmer.repopsitory.FarmEmailRepository;
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
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private FarmEmailRepository emailRepo;

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
				farmerDto.getEmail(), farmerDto.getContact(),"Active", farmerDto.getUserName(), farmerDto.getPassword(), address,
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

		SimpleMailMessage msg = new SimpleMailMessage();
		List<FarmEmails> emails = emailRepo.findAll();
		for (FarmEmails i : emails) {
			msg.setTo(i.getEmails());
		}

		msg.setSubject("Farmer-Dealer-Web-Service");
		msg.setText("Checkout new crops freshly from the farms of farmer registered on our site.");

		javaMailSender.send(msg);

		return true;

	}

	public boolean addCrop(CropDto crop) {
		FarmerModel farmer = repo.getById(crop.getUserName());
		Crop crops = new Crop(crop.getId(), crop.getCropName(), crop.getCropType(), crop.getCropQuantity(),
				crop.getPrice(), farmer);
		cropRepo.save(crops);
		farmer.getCrops().add(crops);
		cropRepo.save(crops);

		SimpleMailMessage msg = new SimpleMailMessage();
		List<FarmEmails> emails = emailRepo.findAll();
		for (FarmEmails i : emails) {
			msg.setTo(i.getEmails());
		}

		msg.setSubject("Farmer-Dealer-Web-Service");
		msg.setText("Checkout new crops freshly from the farms of farmer registered on our site.");

		javaMailSender.send(msg);
		return true;
	}

	public boolean removeCrop(String userName,Integer id) {

		FarmerModel farmerModel = repo.findById(userName).get();
		for (Crop crop : farmerModel.getCrops()) {
			if (crop.getId() == id) {

				farmerModel.getCrops().remove(crop);
				cropRepo.delete(crop);
				repo.save(farmerModel);

			}
		}

		return true;
	}

	public List<Crop> getFarmerCrops() {
		List<Crop> finalCrop = new ArrayList<>();
		List<Crop> crop = cropRepo.findAll();
		for (Crop i : crop) {
			if (i.getCropQuantity() > 0) {
				finalCrop.add(i);
			}
		}
		return finalCrop;

	}

	public void removeFamer(String userName) {

		FarmerModel farmerModel=repo.findById(userName).get();
		farmerModel.setStatus("Inactive");
		repo.save(farmerModel);
		

	}

	public boolean rateFarmer(RatingDto dto) {

		FarmerModel farmer;
		farmer = repo.getById(dto.getUserId());
		farmer.setRating(dto.getRating());
		repo.save(farmer);
		return true;

	}

	public boolean editProfile(EditDto dto) {
		FarmerModel farmer = new FarmerModel();
		farmer = repo.findById(dto.getUserName()).get();
		farmer.setFirstName(dto.getFirstName());
		farmer.setLastName(dto.getLastName());
		farmer.setEmail(dto.getEmail());
		farmer.setContact(dto.getContact());
		farmer.setUserName(dto.getUserName());

		Address address = new Address(dto.getHouseNo(), dto.getLocality(), dto.getTown(), dto.getDistrict(),
				dto.getState(), dto.getPostalCode(), null);
		farmer.setAddress(address);

		BankAccountDeatil bankAccountDeatil = new BankAccountDeatil(dto.getBankAccountHolderName(),
				dto.getBankAccountNo(), dto.getIfscCode());

		bankRepo.save(bankAccountDeatil);
		farmer.setBankAccountDeatil(bankAccountDeatil);
		repo.save(farmer);
		address.setFarmer(farmer);
		addressRepo.save(address);

		return true;
	}

	public Address getAddress(int  id) {

		Crop crop =cropRepo.findById(id).get();
		return crop.getFarmer().getAddress();

	}

	public BankAccountDeatil getbankDetails(int id) {

		Crop crop =cropRepo.findById(id).get();
		return crop.getFarmer().getBankAccountDeatil();
	}

	public Boolean quantityManagement(HashMap<Integer, Integer> CropIds) {
		Iterator<Map.Entry<Integer, Integer>> itr = CropIds.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<Integer, Integer> entry = itr.next();

			Crop crop = cropRepo.findById(entry.getKey()).get();
			crop.setCropQuantity(crop.getCropQuantity() - entry.getValue());
			cropRepo.save(crop);
			
		}
		return null;
	}

	public Boolean saveFarmEmail(ArrayList<String> Emails) {

		for (String i : Emails) {
			FarmEmails farmEmails = new FarmEmails(i);
			emailRepo.save(farmEmails);

		}

		return true;
	}

	public FarmerDto getFarmerDetails(String userName) {
		FarmerModel farmerModel = repo.findById(userName).get();
		FarmerDto farmerDto = new FarmerDto(farmerModel.getFirstName(),farmerModel.getLastName(),farmerModel.getEmail(),
				farmerModel.getContact(),farmerModel.getUserName(),farmerModel.getPassword(),farmerModel.getRating(),
				farmerModel.getAddress().getHouseNo(),farmerModel.getAddress().getLocality(),farmerModel.getAddress().getTown(),farmerModel.getAddress().getDistrict(),
				farmerModel.getAddress().getState(),farmerModel.getAddress().getPostalCode(),0,null,null,0,0,farmerModel.getBankAccountDeatil().getBankAccountNo(),
				farmerModel.getBankAccountDeatil().getIfscCode(),farmerModel.getBankAccountDeatil().getBankAccountHolderName());
		
		return farmerDto;
	}
	
	public List<Crop> getFarmerCrops(String userName)
	{
		FarmerModel farmerModel =repo.findById(userName).get();
		return farmerModel.getCrops();
		
	}

	public List<FarmerModel> getFarmers() {
	
		return repo.findAll();
	}

}
