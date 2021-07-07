package com.cosmin.rffsn.accounts;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
	
	private AccountRepository repository;
	
	@Autowired
	private RatesService ratesService;

	public AccountService() {
	}
	
	public AccountService(RatesService ratesService) {
		this.ratesService = ratesService;
	}

	public java.util.List<Account> findAll() {
		return this.repository.findAll();
	}

	@Autowired
	public void setRepository(AccountRepository repository) {
		this.repository = repository;
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

	public Account save(Account acc) {
		return this.repository.save(acc);
	}

	public Account findByIbanAndModifyBalance(String iban) {
		Account account = this.repository.findByIban(iban);
		if (account == null)
			throw new AccountNotFoundException();
		Double balanceInRon = account.getBalance();
		Map<String, String> rates = this.ratesService.exchageRates();
		Double rate = Double.parseDouble(rates.get("RON"));
		Double balanceInEur = balanceInRon / rate;
		account.setBalance(balanceInEur);
		account.setCurrency("EUR");
		return account;
	}
}
