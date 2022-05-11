package com.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.model.UserLogin;
import com.repository.LoginRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class MyUserDetailService implements UserDetailsService {

	@Autowired
	LoginRepository repo;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		UserLogin user = repo.findById(userName).get();
		log.info(user.getPassword());
				/*.orElseThrow(() -> new UsernameNotFoundException("Not Found: 	" + userName));*/

		List<SimpleGrantedAuthority> roles = Arrays.stream(user.getRoles().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		return new User(user.getUserName(), user.getPassword(), roles);

	}
	

}
