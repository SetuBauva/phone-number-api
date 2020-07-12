package com.bankonus.numberapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bankonus.numberapi.model.PhoneNumbers;
@Repository
public interface PhoneNumberDao extends JpaRepository<PhoneNumbers, Integer>{

	public List<PhoneNumbers> findByCustomerID(int customerID);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "Update PHONE_NUMBERS \n" + 
			"Set status = ?1\n" + 
			"where customerId = ?2\n" + 
			"and phone_number = ?3", nativeQuery = true)
	public int updateStatus(String status,int customerId,int phone_number);

}
