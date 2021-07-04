package com.cosmin.rffsn;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cosmin.rffsn.todo.AccountService;

@SpringBootTest
public class ServiceAutowiredTest {

    @Autowired
    private AccountService service;

    @DisplayName("Test Spring @Autowired Integration")
    @Test
    void testGet() {
    	System.out.println("test");
    	System.out.println("service=" + service);
    	assertNotNull(this.service);
    }
    
}