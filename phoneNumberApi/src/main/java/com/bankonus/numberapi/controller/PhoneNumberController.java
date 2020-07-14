package com.bankonus.numberapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bankonus.numberapi.dao.PhoneNumberDao;
import com.bankonus.numberapi.model.PhoneNumbers;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Controller for the Rest API's
 */
@RestController
@JsonInclude(Include.NON_EMPTY)
public class PhoneNumberController {

	@Autowired
	PhoneNumberDao repo;

	@GetMapping("/getAllPhoneNumbers")
	public List<PhoneNumbers> getAllPhoneNumbers() {
		return repo.findAll();
	}

	@GetMapping("/getPhoneNumbers/{user}")
	public List<PhoneNumbers> getAllPhoneNumbers(@PathVariable("user") int user) {
		return repo.findByCustomerID(user);
	}

	@PatchMapping("/activatePhoneNumbers")
	public ResponseEntity<PhoneNumbers> updatePhoneNumber(@RequestBody PhoneNumbers pn) {
		if (repo.updateStatus(pn.getStatus(), pn.getCustomerID(), pn.getPhoneNumber()) > 0)
			return new ResponseEntity<PhoneNumbers>(pn, HttpStatus.OK);
		else
			return new ResponseEntity<PhoneNumbers>(HttpStatus.NOT_FOUND);
	}

}
