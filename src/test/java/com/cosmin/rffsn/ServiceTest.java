package com.cosmin.rffsn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cosmin.rffsn.accounts.Account;
import com.cosmin.rffsn.accounts.AccountRepository;
import com.cosmin.rffsn.accounts.AccountService;
import com.cosmin.rffsn.accounts.RatesService;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ServiceTest {
	
	private AccountService accountService;
	
	@Mock
	private RatesService ratesService;
	
	@Autowired
	private AccountRepository repository;

	@BeforeEach
	public void setup() {
		this.accountService = new AccountService(this.ratesService);
		this.accountService.setRepository(this.repository);
	}
	
	@Test
	public void testServ() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("RON", "4.92");
		Mockito.when(this.ratesService.exchageRates()).thenReturn(map);
		String iban = "xsgs2";
		Account account = new Account();
		account.setBalance(20.20);
		account.setCurrency("RON");
		account.setIban(iban);
		assertEquals(this.repository.findAll().size(), 0);
		this.accountService.save(account);
		assertEquals(this.repository.findAll().size(), 1);
		account = this.accountService.findByIbanAndModifyBalance(iban);
		assertEquals(account.getCurrency(), "EUR");
		assertEquals(account.getBalance(), 4.105691056910569);
		System.out.println("account.getBalance()=" + account.getBalance());
	}

}
