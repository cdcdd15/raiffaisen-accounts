package com.cosmin.rffsn;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.cosmin.rffsn.accounts.Account;
import com.cosmin.rffsn.accounts.AccountRepository;
import com.cosmin.rffsn.accounts.AccountService;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ServiceMockitoTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service = new AccountService();

//    @BeforeEach
//    void setMockOutput() {
//    }

    @DisplayName("Test Mock")
    @Test
    void testGet() {
    	java.util.List<Account> list = new ArrayList<Account>();
    	when(repository.findAll()).thenReturn(list);
        assertEquals(list, service.findAll());
    }

}