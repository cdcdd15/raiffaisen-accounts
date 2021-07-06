package com.cosmin.rffsn.accounts;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cosmin.rffsn.accounts.util.RatesUtil;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class RatesService {
	private static final String BREAKER_NAME = "accn-serv";

	private static final String CACHE_NAME_RATES = "rates";

	@Autowired
	private CacheManager cacheManager;

	public void evictAllCacheValues() {
		System.out.println("inside service evictAllCacheValues() method.");
		cacheManager.getCache(CACHE_NAME_RATES).clear();
	}

	@CircuitBreaker(name = BREAKER_NAME, fallbackMethod = "fallBck")
	@Cacheable(CACHE_NAME_RATES)
	public Map<String, String> exchageRates() {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = "http://api.exchangeratesapi.io/v1/latest?access_key=5f96ff228e05bda6d5ee91b330c231a6&format=1";
		ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
		System.out.println("Status code=" + response.getStatusCode());
		Map<String, String> rates = RatesUtil.fromHttpBodyToRateList(response);
		return rates;
	}

	public Map<String, String> fallBck(Exception exception) {
		exception.printStackTrace();
		Map<String, String> map = new HashMap<>();
		map.put("error", "Could not get exchange rates from endpoint. Response from circuit breaker fallback method.");
		return map;
	}
}
