package com.cosmin.rest.webservices.restfulwebservices.todo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Account {
	@Id
	@GeneratedValue
	private Long id;
	private String iban;

	
//	private String username;
//	private String description;
//	private Date targetDate;
//	private boolean isDone;
	
	public Account() {
		
	}

	public Account(long id, String iban) {
		super();
		this.id = id;
		this.iban = iban;
//		this.description = description;
//		this.targetDate = targetDate;
//		this.isDone = isDone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	
}