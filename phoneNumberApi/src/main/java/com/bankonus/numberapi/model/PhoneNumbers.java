package com.bankonus.numberapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PhoneNumbers {
	
	@Id
	@GeneratedValue
	private int phoneId;
	private int customerID;
	private int phoneNumber;
	private String status;
	
	public int getPhoneId() {
		return phoneId;
	}
	public void setPhoneId(int phoneId) {
		this.phoneId = phoneId;
	}
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	public int getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "PhoneNumbers [phoneId=" + phoneId + ", customerID=" + customerID + ", phoneNumber=" + phoneNumber
				+ ", status=" + status + "]";
	}
	public PhoneNumbers(int phoneId, int customerID, int phoneNumber, String status) {
		super();
		this.phoneId = phoneId;
		this.customerID = customerID;
		this.phoneNumber = phoneNumber;
		this.status = status;
	}
	public PhoneNumbers() {
		super();
	}
	
}
