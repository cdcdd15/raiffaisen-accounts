package com.cosmin.rffsn.accounts;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	private AccountRepository repository;

	@Autowired
	private AccountService serv;

	@GetMapping("/evict")
	public ResponseEntity<Void> evict() {
		this.serv.evictAllCacheValues();
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/exc")
	public ResponseEntity<Map<String, String>> exc() {
		Map<String, String> rates = this.serv.exchageRates();
		return new ResponseEntity<Map<String, String>>(rates, HttpStatus.OK);
	}

//	@GetMapping("/acc")
//	public List<Account> getAllAcc(){
//		return this.serv.get();
//	}

	@GetMapping
	public List<Account> getAllTodos() {
		return repository.findAll();
	}

	@GetMapping("/{iban}")
	public ResponseEntity<Account> getByIban(@PathVariable String iban) {
		Account account = this.repository.findByIban(iban);
		if (account == null)
			throw new AccountNotFoundException();
		Double balanceInRon = account.getBalance();
		Map<String, String> rates = this.serv.exchageRates();
		Double rate = Double.parseDouble(rates.get("RON"));
		Double balanceInEur = balanceInRon / rate;
		account.setBalance(balanceInEur);
		account.setCurrency("EUR");
		return new ResponseEntity<Account>(account, HttpStatus.OK);
	}

//	@GetMapping("/jpa/users/{username}/todos/{id}")
//	public Account getTodo(@PathVariable String username, @PathVariable long id){
//		return todoJpaRepository.findById(id).get();
//	}

//	@DeleteMapping("/jpa/users/{username}/todos/{id}")
//	public ResponseEntity<Void> deleteTodo(
//			@PathVariable String username, @PathVariable long id) {
//
//		todoJpaRepository.deleteById(id);
//
//		return ResponseEntity.noContent().build();
//	}

	@DeleteMapping
	public ResponseEntity<Void> deleteAll() {

		repository.deleteAll();

		return ResponseEntity.noContent().build();
	}

//	@PutMapping("/jpa/users/{username}/todos/{id}")
//	public ResponseEntity<Account> updateTodo(
//			@PathVariable String username,
//			@PathVariable long id, @RequestBody Account todo){

//		todo.setUsername(username);

//		Account todoUpdated = todoJpaRepository.save(todo);

//		return new ResponseEntity<Account>(todoUpdated, HttpStatus.OK);
//	}

	@PostMapping
	public ResponseEntity<String> createTodo(@RequestBody Account acc) {
		Account account = this.repository.findByIban(acc.getIban());
		if (account != null) {
			return new ResponseEntity<String>(
					"Record with this iban field already exists, change the iban and insert it again",
					HttpStatus.CREATED);
		}
		account = repository.save(acc);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(account.getId())
				.toUri();
//		return ResponseEntity.created(uri).build();
		return new ResponseEntity<String>("Record created, " + uri, HttpStatus.CREATED);
	}

	@ResponseBody
	@GetMapping("/hello")
	public String hello() {
		return this.serv.hello();
	}

}
