package com.cogent.fooddeliveryapp.security.services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cogent.fooddeliveryapp.dto.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserDetailsImpl
 *
 * @author bryan
 * @date Feb 23, 2022-11:06:18 AM
 */
@Data
@EqualsAndHashCode(exclude = { "username", "password", "authorities" })
public class UserDetailsImpl implements UserDetails {
	private int id;
	private String username;
	@JsonIgnore
	private String password;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	public static UserDetailsImpl build(Customer user) {
		List<GrantedAuthority> authorities = user.getRoles().parallelStream().map(role -> {
			return new SimpleGrantedAuthority(role.getRoleName().name());
		}).collect(Collectors.toList());
		
		return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword(), authorities);
	}
	
	private UserDetailsImpl(int id, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// Ex) temporary account?
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// Example impl: login attempts
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// Password expired; user should reset password
		return true;
	}

	@Override
	public boolean isEnabled() {
		// Whether user is enabled/active;
		// Ex.) if user left organization user should be disabled
		return true;
	}
}
