package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.Repositories.UserRepository;
import com.example.demo.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository repo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
	//fetching user from database	
	User user=repo.getUserByusername(username);
	if(user==null)
	{
		throw new UsernameNotFoundException("Bhai User Nhi mill Rha h...");
	}
	
	CustomerUserDetails CustomerUserDetails= new CustomerUserDetails(user);
		return CustomerUserDetails;
	}

}
