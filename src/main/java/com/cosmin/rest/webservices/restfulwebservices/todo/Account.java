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
	private String currency;
	private Date targetDate;
	private Integer balance;

	
//	private String username;
//	private String description;
//	private boolean isDone;
	
	public Account() {
		
	}

	public Account(long id, String iban, Date targetDate, Integer balance, String currency) {
		super();
		this.id = id;
		this.iban = iban;
		this.targetDate = targetDate;
		this.balance = balance;
		this.currency = currency;
//		this.description = description;
//		this.isDone = isDone;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
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

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	
}