package farmer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import farmer.com.util.JwtUtil;
import farmer.dto.CropDto;
import farmer.dto.EditDto;
import farmer.dto.FarmerDto;
import farmer.dto.RatingDto;
import farmer.exception.FarmerException;
import farmer.model.Address;
import farmer.model.BankAccountDeatil;
import farmer.model.Crop;
import farmer.model.FarmEmails;
import farmer.model.FarmerModel;
import farmer.model.SoldCrops;
import farmer.model.UserLogin;
import farmer.repopsitory.AddressRepository;
import farmer.repopsitory.BankAccountRepository;
import farmer.repopsitory.CropRespository;
import farmer.repopsitory.FarmEmailRepository;
import farmer.repopsitory.FarmerRepository;
import farmer.repopsitory.SoldCropsRepo;
import farmer.serviceInterface.FarmerServiceInterface;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class FarmerService implements FarmerServiceInterface {
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
	@Autowired
	private SoldCropsRepo soldRepo;

	@Override
	public boolean register(FarmerDto farmerDto) throws FarmerException{
		//used farmer dto for getting data from ui and then use set and get methods of different model clasess to set data up
			try {
		Address address = new Address(farmerDto.getHouseNo(), farmerDto.getLocality(), farmerDto.getTown(),
				farmerDto.getDistrict(), farmerDto.getState(), farmerDto.getPostalCode(), null);
		addressRepo.save(address);

		BankAccountDeatil bankAccountDeatil = new BankAccountDeatil(farmerDto.getBankAccountHolderName(),
				farmerDto.getBankAccountNo(), farmerDto.getIfscCode());
		bankRepo.save(bankAccountDeatil);

		Crop crops = new Crop(farmerDto.getCropName(), farmerDto.getCropType(),
				farmerDto.getCropQuantity(), farmerDto.getPrice(), null);
		cropRepo.save(crops);

		FarmerModel farmerModel = new FarmerModel(farmerDto.getFirstName(), farmerDto.getLastName(),
				farmerDto.getEmail(), farmerDto.getContact(), "Active", farmerDto.getUserName(),
				farmerDto.getPassword(), address, new ArrayList<>(), bankAccountDeatil, farmerDto.getRating());

		farmerModel.getCrops().add(crops);
		crops.setFarmer(farmerModel);
		address.setFarmer(farmerModel);
		cropRepo.save(crops);
		addressRepo.save(address);
		repo.save(farmerModel);
			}
			catch (FarmerException farmerException) {
				log.info("userName already taken");
			}
//use rabbit mq sender option to send login credentials of farmer when he/she registered to the site 
		UserLogin login = new UserLogin(farmerDto.getUserName(), farmerDto.getPassword(), "farmer","Active");
		rabbitTemplate.convertAndSend(exchange, routingkey, login);
		log.info("Send msg = " + login);
		//used spring mail service fr sending mails to all registered dealers to check new crops on our site.
		SimpleMailMessage msg = new SimpleMailMessage();
		List<FarmEmails> emails = emailRepo.findAll();
		for (FarmEmails i : emails) 
		{
			msg.setSubject("Farmer-Dealer-Web-Service");
			msg.setText("Checkout new crops freshly from the farms of farmer registered on our site.");
			msg.setTo(i.getEmails());
			javaMailSender.send(msg);
				log.info("mail sent");
		}

		return true;

	}
//this post method is created for a farmer to add crops after registering
//themselves with one crop and also here have use validate token method validate jwt token passed within headers
	@Override
	public boolean addCrop(CropDto crop) {
			//send mails to the registered dealers to checkut nw crops.
			SimpleMailMessage msg = new SimpleMailMessage();
			List<FarmEmails> emails = emailRepo.findAll();
			for (FarmEmails i : emails) 
			{
				msg.setSubject("Farmer-Dealer-Web-Service");
				msg.setText("Checkout new crops freshly from the farms of farmer registered on our site.");
				msg.setTo(i.getEmails());
				javaMailSender.send(msg);
				log.info("mail sent");
			}
				
				FarmerModel farmer = repo.getById(crop.getUserName());
				Crop crops = new Crop(crop.getCropName(), crop.getCropType(), crop.getCropQuantity(),
						crop.getPrice(), farmer);
				
				farmer.getCrops().add(crops);
				cropRepo.save(crops);
				repo.save(farmer);

			return true;
		}

	@Override
	public boolean removeCrop(String userName, Integer id) {
//use this deletes method to remove crop from the list of farmer if he wants to
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

	@Override
	public List<Crop> getFarmerCrops() {
		//use this method here so that it can be called using web client to get list of registered crops from farmer service.
		List<Crop> finalCrop = new ArrayList<>();
		List<Crop> crop = cropRepo.findAll();
		for (Crop i : crop) {
			if (i.getCropQuantity() > 0) {
				finalCrop.add(i);
			}
		}
		return finalCrop;

	}

	@Override
	public void removeFamer(String userName) {
//used this method to provide an option to admin if he want to deactivate any user.
		FarmerModel farmerModel = repo.findById(userName).get();
		farmerModel.setStatus("Inactive");
		repo.save(farmerModel);

	}

	@Override
	public boolean rateFarmer(RatingDto dto) {
//used this to provide rting facility to dealers to rate farmers.
		FarmerModel farmer;
		Crop crop = cropRepo.findById(dto.getCropId()).get();
		farmer = repo.findById(crop.getFarmer().getUserName()).get();
		farmer.setRating(dto.getRating());
		repo.save(farmer);
		return true;

	}

	@Override
	public boolean editProfile(EditDto dto) {
//used this to provide editing profile facility to farmers .
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

	@Override
	public Address getAddress(int id) {
//called by dealer to gte address of farmer of a particular crop.
		Crop crop = cropRepo.findById(id).get();
		return crop.getFarmer().getAddress();

	}

	@Override
	public BankAccountDeatil getbankDetails(int id) {

		Crop crop = cropRepo.findById(id).get();
		return crop.getFarmer().getBankAccountDeatil();
	}

	@Override
	public Boolean quantityManagement(HashMap<Integer, Integer> CropIds) {
// this method is called parallely once the success payment is done to deduct the quantity of crops buyed and also to empty cart and it also saves sold orders for farmers . 
		Iterator<Map.Entry<Integer, Integer>> itr = CropIds.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<Integer, Integer> entry = itr.next();

			Crop crop = cropRepo.findById(entry.getKey()).get();
			FarmerModel farmerModel =repo.findById(crop.getFarmer().getUserName()).get();
			/*
			 * // FarmerModel farmerModel
			 * =repo.findById(crop.getFarmer().getUserName()).orElseThrow( // ()-> new
			 * FarmerException() );
			 */
			crop.setCropQuantity(crop.getCropQuantity() - entry.getValue());
			int cost = entry.getValue()*crop.getPrice();
			SoldCrops soldCrops=new SoldCrops(entry.getValue(),crop.getCropName(),crop.getCropType(),crop.getPrice(),cost,farmerModel);
//			farmerModel.getSoldCrops().add(soldCrops);
			repo.save(farmerModel);
			soldRepo.save(soldCrops);
			cropRepo.save(crop);

		}
		return null;
	}

	@Override
	public Boolean saveFarmEmail(String Emails) {
//this is used to save the emails of newly registered dealers on site so they can be emailed.
		FarmEmails farmEmails = new FarmEmails(Emails);
		emailRepo.save(farmEmails);

		return true;
	}

	@Override
	public FarmerDto getFarmerDetails(String userName) {
		//this method isused to get farmer details for editing purpose
			FarmerModel farmerModel = repo.findById(userName).get();
			FarmerDto farmerDto = new FarmerDto(farmerModel.getFirstName(), farmerModel.getLastName(),
					farmerModel.getEmail(), farmerModel.getContact(), farmerModel.getUserName(),
					farmerModel.getPassword(), farmerModel.getRating(), farmerModel.getAddress().getHouseNo(),
					farmerModel.getAddress().getLocality(), farmerModel.getAddress().getTown(),
					farmerModel.getAddress().getDistrict(), farmerModel.getAddress().getState(),
					farmerModel.getAddress().getPostalCode(), 0, null, null, 0, 0,
					farmerModel.getBankAccountDeatil().getBankAccountNo(),
					farmerModel.getBankAccountDeatil().getIfscCode(),
					farmerModel.getBankAccountDeatil().getBankAccountHolderName());

			return farmerDto;
		}

	@Override
	public List<Crop> getFarmerCrops(String userName) {
		//to get crops registerd by a particular farmer .
			FarmerModel farmerModel = repo.findById(userName).get();
			return farmerModel.getCrops();
		}

	@Override
	public List<FarmerModel> getFarmers() {
//called by admin to get list of registered farmers on site.
		return repo.findAll();
	}
	public List<SoldCrops> getsoldCrops(String userName)
	{
		//for getiing info about particaule farmer crops that are sold till now
		FarmerModel farmerModel=repo.findById(userName).orElseThrow(
				() -> new FarmerException()
				);
		return farmerModel.getSoldCrops();
	
	}

}
