package com.cosmin.rffsn.accounts.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RatesUtil {
	private static ObjectMapper mapper = new ObjectMapper();

	public static Map<String, String> fromHttpBodyToRateList(ResponseEntity<String> response) {
		JsonNode root;
		Map<String, String> rates = new HashMap<>();
		try {
			root = mapper.readTree(response.getBody());
			System.out.println("In rates util.");
			System.out.println(root.get("rates").get("RON"));
			rates.put("RON", root.get("rates").get("RON").asText());
			rates.put("USD", root.get("rates").get("USD").asText());
			rates.put("CZK", root.get("rates").get("CZK").asText());
			rates.put("CHF", root.get("rates").get("CHF").asText());
			System.out.println("In rates util. rates=" + rates);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rates;
	}
}
