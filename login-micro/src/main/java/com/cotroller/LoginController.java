package com.cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.Service.MyUserDetailService;
import com.auth.AuthenticationRequest;
import com.auth.AuthenticationResponse;
import com.model.UserLogin;
import com.repository.LoginRepository;
import com.util.JwtUtil;

@RestController
@CrossOrigin("*")
public class LoginController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailService userDetailsService;
	
	@Autowired
	LoginRepository repo;
	
	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	private JwtUtil jwtTokenUtil;
	@GetMapping("/get")
	public String check()
	{
		return "checked";
	}
	
	@GetMapping("/add")
	public void add()
	{
		repo.save(new UserLogin("Sanchit","AA","admin"));
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUserName(), authenticationRequest.getPassword()));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);

		}

		
		  final UserDetails userDetails =
		  userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
		  
		  final String token = jwtTokenUtil.generateToken(userDetails);
		  
		  return ResponseEntity.ok(new AuthenticationResponse(token));
		   
	}
	@GetMapping("/validate")
	public Boolean validateToken(ServerHttpRequest request)
	{
		String authorizationHeader = request.getHeaders().getFirst("Authorization");
		 
			String username = null;
			String jwt = null;

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring("Bearer ".length());
				username = jwtUtil.extractUsername(jwt);
			}

			if (username != null) {

				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

				if (jwtUtil.validateToken(jwt, userDetails)) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					
					return true;
					
					
				}
				else 
					return false;
			}
			else
				return false;
	}

}
