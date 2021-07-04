package com.cosmin.rest.webservices.restfulwebservices;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cosmin.rest.webservices.restfulwebservices.todo.Account;
import com.cosmin.rest.webservices.restfulwebservices.todo.AccountRepository;
import com.cosmin.rest.webservices.restfulwebservices.todo.AccountService;

@SpringBootTest
public class ServiceMockitoTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks // auto inject helloRepository
    private AccountService service = new AccountService();

    @BeforeEach
    void setMockOutput() {
    }

    @DisplayName("Test Mock")
    @Test
    void testGet() {
    	java.util.List<Account> list = new ArrayList<Account>();
    	when(repository.findAll()).thenReturn(list);
        assertEquals(list, service.findAll());
    }

}