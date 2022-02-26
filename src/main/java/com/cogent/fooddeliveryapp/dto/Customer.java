package com.cogent.fooddeliveryapp.dto;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Customer
 *
 * @author bryan
 * @date Feb 18, 2022-2:49:13 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = { "addresses", "roles" })
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank
	@Email
	@Column(unique = true)
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String name;
	@NotNull
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate doj;

	@NotNull
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private Set<Address> addresses;

	@ManyToMany
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "customer_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	@JsonIgnore
	private Set<Role> roles;
	
	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
	@JsonIgnore
	private CustomerCart customerCart = new CustomerCart(this);
}
