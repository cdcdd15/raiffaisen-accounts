package com.cosmin.rest.webservices.restfulwebservices.todo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
	@Autowired
	private TodoJpaRepository repository;
	
	@Autowired
	private CacheManager cacheManager;
	
	private static final String NAME = "addresses";
	
	@Cacheable(NAME)
	public List<Account> get() {
		System.out.println("inside service get() method.");
		return repository.findAll();
	}
	
	public void evictAllCacheValues() {
		System.out.println("inside service evictAllCacheValues() method.");
		cacheManager.getCache(NAME).clear();
	}
}
