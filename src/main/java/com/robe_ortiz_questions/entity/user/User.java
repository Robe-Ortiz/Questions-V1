package com.robe_ortiz_questions.entity.user;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String profilePicture;
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;
    @CreationTimestamp
    private LocalDate registrationDate;
	
    public User() {
	}

	public User(String email, String name, String profilePicture) {
		this.email = email;
		this.name = name;
		this.profilePicture = profilePicture;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
	
	public String getProfilePicture() {
		return profilePicture;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String nombre) {
		this.name = nombre;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	
	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole rolUsuario) {
		this.userRole = rolUsuario;
	}
        
}
