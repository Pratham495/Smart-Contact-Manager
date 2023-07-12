package com.example.demo.entities;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
@Table(name="USER")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column
	@NotBlank(message="Name should not be blank")
	@Size(min=2,max=20, message="Minimum 2 and Maximum 20 Characters Required")
	private String name;
	@Column(unique = true)
	@NotBlank(message="Email should not be blank")
	//@Pattern(regexp = "/.*@[a-z0-9.-]*/i")
	private String email;
	@Column
	@NotNull
	//@Pattern(regexp = ("?=.*[0-9])" )
	private String password;
	@Column
	private String role;
	@Column
	private boolean enabled;
	@Column
	private String imageUrl;
	@Column(length=1000)
	@NotBlank(message="Please Writen  4 to 5 line About Yourself!!")
	private String about;
	
	@OneToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY, mappedBy="user",orphanRemoval = true)
	
	
	private List<Contact>contacts = new ArrayList<>();
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public  User(int id, String name, String email, String password, String role, boolean enabled, String imageUrl,
			String about) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
		this.imageUrl = imageUrl;
		this.about = about;
	}
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", about=" + about + ", contacts=" + contacts
				+ "]";
	}
	
	
	
}