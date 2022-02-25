package com.cogent.fooddeliveryapp.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * AuthTokenFilter:
 *
 * Filter which validates the authentication token (once per request)
 * 
 * @author bryan
 * @date Feb 23, 2022-2:39:47 PM
 */
public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	private static final Logger sLogger = LoggerFactory.getLogger(AuthTokenFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			// Extract the token
			String jwtToken = parseJwt(request);
			
			// Validate it
			if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
				String username = jwtUtils.getUsernameFromJwtToken(jwtToken);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
				UsernamePasswordAuthenticationToken upAuthoken = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // Credentials not needed after successful authentication
				upAuthoken.setDetails(new WebAuthenticationDetails(request)); // WebAuthDetails: handles the authentication
				
				SecurityContextHolder.getContext().setAuthentication(upAuthoken);
			}
			
			// Continues down the filter chain
			filterChain.doFilter(request, response); // or goes to DispatcherServlet
		} catch (Exception e) {
			sLogger.error("Error: {}", e.getMessage());
		}
	}
	
	private String parseJwt(HttpServletRequest request) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7, authHeader.length());
		}
		
		return null;
	}
}
