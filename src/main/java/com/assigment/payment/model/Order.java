package com.assigment.payment.model;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    private String id;
    private String orderUUID;
    private String cartUUID;
    private String userUUID;
    private OrderStatus status;
    private Date createDate;
}
