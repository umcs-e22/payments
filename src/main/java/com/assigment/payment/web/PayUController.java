package com.assigment.payment.web;

import com.assigment.payment.model.*;
import com.assigment.payment.service.PayUOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import static com.assigment.payment.model.OrderCreateResponse.Status.STATUS_CODE_SUCCESS;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PayUController {

    public static final String URL_PAYU_DEMO_INDEX = "/v1/payments";

    private final PayUOrderService orderService;

    @PostMapping(URL_PAYU_DEMO_INDEX)
    public ResponseEntity<?> handleCheckout(
            @RequestBody RequestPayment order,
            @RequestHeader("X-auth-user-id") String userUUID,
            HttpServletRequest request) {
        log.info(order.toString());

        final OrderCreateRequest orderRequest = prepareOrderCreateRequest(order, request);

        log.info("Order request = {}", orderRequest);

        final OrderCreateResponse orderResponse = orderService.order(orderRequest);

        if (!orderResponse.getStatus().getStatusCode().equals(STATUS_CODE_SUCCESS)) {
            throw new RuntimeException("Payment failed! ");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-auth-user-id", userUUID);

        RestTemplate restTemplate = new RestTemplate();
        String bookResourceUrl = "http://localhost:8921/v1/orders";
        restTemplate.postForObject(bookResourceUrl, new HttpEntity<>(new CartUUIDDTO(order.getCartUUID()), headers), Order.class);

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    private OrderCreateRequest prepareOrderCreateRequest(RequestPayment pay, final HttpServletRequest request) {
        OrderCreateRequest order = new OrderCreateRequest();
        order.setTotalAmount(pay.getTotalAmount());
        order.setCurrencyCode("PLN");
        order.setDescription("Opis zapłaty");
        order.setCustomerIp(request.getRemoteAddr());
        order.setMerchantPosId("145227");
        order.setBuyer(pay.getBuyer());
        order.setProducts(pay.getProducts());
        return order;
    }
}
