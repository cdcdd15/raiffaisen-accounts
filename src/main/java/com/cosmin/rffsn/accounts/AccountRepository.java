package com.cosmin.rffsn.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
//	List<Account> findByUsername(String username);
	Account findByIban(String iban);
}