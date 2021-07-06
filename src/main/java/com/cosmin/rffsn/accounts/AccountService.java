package com.cosmin.rffsn.accounts;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
	
	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private RatesService ratesService;
	
	public java.util.List<Account> findAll() {
		return this.repository.findAll();
	}

	public String hello() {
        return "Hello from Service.";
    }

	public void evictAllCacheValues() {
		this.ratesService.evictAllCacheValues();
		
	}

	public Map<String, String> exchageRates() {
		return this.ratesService.exchageRates();
	}
}
