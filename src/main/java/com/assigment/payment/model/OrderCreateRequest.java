package com.assigment.payment.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCreateRequest {
  private String continueUrl;
  private String customerIp;
  private String merchantPosId;
  private String description;
  private String currencyCode;
  private String totalAmount;
  private Buyer buyer;
  private List<Product> products;
}
