package com.cosmin.rffsn.accounts;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cosmin.rffsn.accounts.util.RatesUtil;
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
	
//	private static final String CACHE_NAME = "addresses";

	private static final String CACHE_NAME_RATES = "rates";
	
//	@Cacheable(CACHE_NAME)
//	public List<Account> get() {
//		System.out.println("inside service get() method.");
//		return repository.findAll();
//	}
	
	public java.util.List<Account> findAll() {
		return this.repository.findAll();
	}

	public void evictAllCacheValues() {
		System.out.println("inside service evictAllCacheValues() method.");
		cacheManager.getCache(CACHE_NAME_RATES).clear();
	}

	@CircuitBreaker(name = BREAKER_NAME, fallbackMethod = "fallBck")
	@Cacheable(CACHE_NAME_RATES)	
	public Map<String, String> exchageRates() {
//	public ResponseEntity<String> exchageRates() {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = "http://api.exchangeratesapi.io/v1/latest?access_key=5f96ff228e05bda6d5ee91b330c231a6&format=1";
		ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
		System.out.println("Status code=" + response.getStatusCode());
		Map<String, String> rates = RatesUtil.fromHttpBodyToRateList(response);
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode root;
//		String rates = null;;
//		try {
//			root = mapper.readTree(response.getBody());
//			rates = root.get("rates").asText();
//			System.out.println(root.get("rates").get("RON"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return rates;
//		return response;
	}
	
	private String rates(String rates) {
		return rates;
	}
	
	public Map<String, String> fallBck(Exception exception) {
		exception.printStackTrace();
		Map<String, String> map = new HashMap<>();
		map.put("error", "Could not get exchange rates from endpoint. Response from circuit breaker fallback method.");
		return map;
	}
	
	public String hello() {
        return "Hello from Service.";
    }
}
