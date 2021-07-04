package com.cosmin.rffsn;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerTest {

    // bind the above RANDOM_PORT
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getHello() throws Exception {

        ResponseEntity<String> response = restTemplate.getForEntity(
            new URL("http://localhost:" + port + "/accounts/hello/").toString(), String.class);
        assertEquals("Hello from Service.", response.getBody());

    }

}
