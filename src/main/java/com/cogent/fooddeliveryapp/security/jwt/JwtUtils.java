package com.cogent.fooddeliveryapp.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.cogent.fooddeliveryapp.security.services.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * JwtUtils
 *
 * @author bryan
 * @date Feb 23, 2022-12:30:24 PM
 */
@Component
//@PropertySource("classpath:application.yaml")
public class JwtUtils {
	private static final Logger sLogger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${com.cogent.fooddeliveryapp.jwtSecret}")
	private String jwtSecret;

	@Value("${com.cogent.fooddeliveryapp.jwtExpirationMs}")
	private Long jwtExpirationMs;

	public String generateToken(Authentication authentication) {
		UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

		Date dateNow = new Date();
		Date expiration = new Date(dateNow.getTime() + jwtExpirationMs);

		return Jwts.builder()
				.setSubject(principal.getUsername())
				.setIssuedAt(dateNow)
				.setExpiration(expiration)
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	private Jws<Claims> parseToken(String authToken) {
		try {
			// JWT: unsigned JWT (not cryptographically signed)
			// JWS: signed JWT (cryptographically signed)
			return Jwts.parser()
					.setSigningKey(jwtSecret) // use secret key to decrypt 
					.parseClaimsJws(authToken); // parses the jwt token
		} catch (ExpiredJwtException e) {
			sLogger.error("JWT Token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			sLogger.error("JWT Token not supported: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			sLogger.error("JWT Token is not valid: {}", e.getMessage());
		} catch (SignatureException e) {
			sLogger.error("JWT Token signature is invalid: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			sLogger.error("JWT Token argument invalid: {}", e.getMessage());
		}
		
		return null;
	}

	public boolean validateJwtToken(String authToken) {
		return parseToken(authToken) != null;
	}

	// Get username from the token
	public String getUsernameFromJwtToken(String authToken) {
		Jws<Claims> jws = parseToken(authToken);
		
		if (jws != null) {
			return jws
					.getBody() // gets the body of the token
					.getSubject(); // gets the subject (username) from the token
		}
		
		return null;
	}
}
