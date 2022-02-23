package com.cogent.fooddeliveryapp.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AuthEntryPointJwt
 *
 * @author bryan
 * @date Feb 23, 2022-3:52:08 PM
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
	private static final Logger sLogger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		sLogger.error("Unauthorized error: {}", authException.getMessage());
		
		// Response content will be a JSON object
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		// HTTP Status Code: 401 (Unauthorized)
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		final Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		bodyMap.put("error", "Unauthorized");
		bodyMap.put("message", authException.getMessage());
		bodyMap.put("path", request.getServletPath());
		
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), bodyMap);
	}
}
