package com.assigment.payment.model;

import lombok.Data;
import java.util.List;

@Data
public class RequestPayment {
    private String totalAmount;
    private Buyer buyer;
    private List<Product> products;
    private String cartUUID;
}
