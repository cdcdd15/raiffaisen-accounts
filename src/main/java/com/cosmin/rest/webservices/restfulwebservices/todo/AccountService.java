package com.cosmin.rest.webservices.restfulwebservices.todo;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class AccountService {
	@Autowired
	private AccountRepository repository;

	@Autowired
	private CacheManager cacheManager;

	private static final String BREAKER_NAME = "accn-serv";
	
	private static final String CACHE_NAME = "addresses";

	@Cacheable(CACHE_NAME)
	public List<Account> get() {
		System.out.println("inside service get() method.");
		return repository.findAll();
	}

	public void evictAllCacheValues() {
		System.out.println("inside service evictAllCacheValues() method.");
		cacheManager.getCache(CACHE_NAME).clear();
	}

	@CircuitBreaker(name = BREAKER_NAME, fallbackMethod = "fallBck")
	public ResponseEntity<String> exchageRates() {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = "http://api.exchangeratesapi.io/v1/latest?access_key=5f96ff228e05bda6d5ee91b330c231a6&format=1";
		ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
		System.out.println("Status code=" + response.getStatusCode());
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;
		try {
			root = mapper.readTree(response.getBody());
			System.out.println(root.get("rates").get("RON"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public ResponseEntity<String> fallBck(Exception exception) {
		return new ResponseEntity<String>("Response from circuit breaker fallback method.", HttpStatus.OK);
	}
}
