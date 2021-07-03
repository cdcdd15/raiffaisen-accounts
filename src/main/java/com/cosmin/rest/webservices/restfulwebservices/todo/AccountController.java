package com.cosmin.rest.webservices.restfulwebservices.todo;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/accounts")
public class TodoJpaResource {
	
	@Autowired
	private TodoJpaRepository todoJpaRepository;

	@Autowired
	private AccountService serv;
	
	@GetMapping("/evict")
	public ResponseEntity<Void> evict(){
		this.serv.evictAllCacheValues();
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/acc")
	public List<Account> getAllAcc(){
		return this.serv.get();
	}
	
	@GetMapping
	public List<Account> getAllTodos(){
		return todoJpaRepository.findAll();
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

		todoJpaRepository.deleteAll();

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
	public ResponseEntity<Void> createTodo(@RequestBody Account todo){
		
//		todo.setId(-1L);
				
		Account createdTodo = todoJpaRepository.save(todo);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(createdTodo.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
		
}
