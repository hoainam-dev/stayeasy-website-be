package com.backend.stayEasy.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "users")
public class User implements UserDetails {

	@Id
	@Column(name = "id", unique = true, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(name = "email", nullable = false, unique = true)
	String email;

	@Column(name = "password", nullable = false)
	String password;


	@Column(name = "firstName", nullable = true, columnDefinition = "NVARCHAR(255)")
	String firstName;

	@Column(name = "lastName", nullable = true, columnDefinition = "NVARCHAR(255)")


	String lastName;

	@Column(name = "phone")
	String phone;

	String avatar;

	LocalDateTime createdAt;

	LocalDateTime updatedAt;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Token> tokens;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), 
								  inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;


	@Override
	public List<SimpleGrantedAuthority> getAuthorities() {
		var authorities = getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleName()))
	            .collect(Collectors.toList());
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}