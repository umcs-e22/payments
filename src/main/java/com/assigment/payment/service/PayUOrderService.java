package com.assigment.payment.service;

import javax.annotation.Resource;

import com.assigment.payment.web.PayUController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.assigment.payment.model.OrderCreateRequest;
import com.assigment.payment.model.OrderCreateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayUOrderService {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Resource(name = "payuApiRestTemplate")
  private RestTemplate restTemplate;

  @Value("${server.addr}")
  private String serverAddress;


  @SneakyThrows
  public OrderCreateResponse order(final OrderCreateRequest orderCreateRequest) {
    orderCreateRequest.setContinueUrl(chooseCallbackUrl());
    log.info(orderCreateRequest.toString());

    final ResponseEntity<String> jsonResponse = restTemplate.postForEntity("https://secure.payu.com/api/v2_1/orders", orderCreateRequest, String.class);

    log.info("Response as String = {}", jsonResponse.getBody());

    return objectMapper.readValue(jsonResponse.getBody(), OrderCreateResponse.class);
  }

  private String chooseCallbackUrl() {
    return (serverAddress) + "/payu-callback";
  }
}
