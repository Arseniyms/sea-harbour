package com.harbourSpring.Service2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

@SpringBootApplication
public class Service2Application {

	public static void main(String[] args) {
		SpringApplication.run(Service2Application.class, args);

		Scanner scanner = new Scanner(System.in);
		String jsonName = scanner.nextLine();
		while(!jsonName.isEmpty()) {
			String url = "http://localhost:8082/service2/postReport";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> request = new HttpEntity<>(jsonName, headers);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
			System.out.println(responseEntity.getBody());
			jsonName = scanner.nextLine();
		}
	}

}
