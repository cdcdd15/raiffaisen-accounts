package com.cosmin.rffsn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cosmin.rffsn.accounts.Account;
import com.cosmin.rffsn.accounts.AccountRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RepoTest 
{
	@Autowired
	private AccountRepository repository;
	
	@Test
	public void testRepositoryInsert() 
	{
		String iban = "xsgs2";
		Account account = new Account();
		account.setBalance(6542.6246);
		account.setCurrency("RON");
		account.setIban(iban);
		repository.save(account);
		System.out.println("account in repo test insert=" + account);
		assertNotNull(account.getId());
		
		account = this.repository.findByIban(iban);
		System.out.println("account in repo test get=" + account);
		assertNotNull(account.getId());
		
		account = this.repository.findByIban("");
		assertEquals(null, account);
	}
}