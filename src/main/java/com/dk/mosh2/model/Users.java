package com.dk.mosh2.model;

import com.dk.mosh2.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Users {
	
	@Id

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;


	private String username;
	
	
	private String password;
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(name = "roles")
	private Roles roles;

}
