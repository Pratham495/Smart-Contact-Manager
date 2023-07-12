package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Contact;
import com.example.demo.entities.User;

public interface contactRepository extends JpaRepository<Contact, Integer> {
	
	@Query("FROM Contact AS c WHERE c.user.id = :userId")
	public List<Contact> findContactsByUser(@Param("userId") int userId);
}
