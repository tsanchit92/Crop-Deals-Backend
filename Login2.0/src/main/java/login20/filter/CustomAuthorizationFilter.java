package login20.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;


public class CustomAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(request.getServletPath().equals("/authenticate") || request.getServletPath().equals("/refreshToken") )
		{
			filterChain.doFilter(request, response);
		}
		else
		{
			String authorizationHeader =request.getHeader("AUTHORIZATION");
			if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer "))
			{
				try {
				 String jwt = authorizationHeader.substring("Bearer ".length());
				 Algorithm algorithm =Algorithm.HMAC256("secret".getBytes());
				 JWTVerifier verifier= JWT.require(algorithm).build();
				 DecodedJWT decodedJwt =verifier.verify(jwt);
				 String userName=decodedJwt.getSubject();
				 String roles =decodedJwt.getClaim("roles").toString();
				 Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
				 authorities.add(new SimpleGrantedAuthority(roles));
				 UsernamePasswordAuthenticationToken authenticationToken =
						 new UsernamePasswordAuthenticationToken(userName,null,authorities);
				 SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				 
				 filterChain.doFilter(request, response);
				 
				}
				catch(Exception exception)
				{
					response.setHeader("error", exception.getMessage());
					response.setStatus(HttpStatus.FORBIDDEN.value());
					Map<String,String> error = new HashMap<>();
					error.put("error_message", exception.getMessage());
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					new ObjectMapper().writeValue(response.getOutputStream(),error);
				}
			}
			
		}
	}

}
