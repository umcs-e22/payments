package com.assigment.payment.config;

import java.util.Collections;

import com.assigment.payment.service.auth.PayUAuthToken;
import com.assigment.payment.service.auth.PayUClientCredentialsAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Configuration
class PayUAuthConfig {

  private final PayUClientCredentialsAuthenticator payUClientCredentialsAuthenticator;

  @Bean("payuApiRestTemplate")
  public RestTemplate payuRestTemplate() {
    final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    restTemplate.setInterceptors(Collections.singletonList((httpRequest, bytes, clientHttpRequestExecution) -> {
      final PayUAuthToken payUAuthToken = payUClientCredentialsAuthenticator.authenticate();
      final HttpHeaders headers = httpRequest.getHeaders();
      headers.add("Authorization", payUAuthToken.getTokenType() + " " + payUAuthToken.getAccessToken());
      if (!headers.containsKey("Content-Type")) {
        headers.add("Content-Type", "application/json");
      }
      return clientHttpRequestExecution.execute(httpRequest, bytes);
    }));

    return restTemplate;
  }
}