package login20.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import login20.model.LoginUser;
import login20.serviceIntefrace.LoginServiceInterface;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImplementation implements LoginServiceInterface, UserDetailsService {

	@Autowired
	private login20.repo.Repository repo;
	
	

	@Override
	public LoginUser getUser(String userName) {
		return repo.findById(userName)
				.orElseThrow(() -> new RuntimeException("No user found with username" + userName));
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		LoginUser user = repo.findById(userName)
				.orElseThrow(() -> new UsernameNotFoundException("No user found by userName:" + userName));
		
		List<SimpleGrantedAuthority> roles = Arrays.stream(user.getRoles().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		return new User(user.getUserName(), user.getPassword(), roles);
	}
	
	

}
