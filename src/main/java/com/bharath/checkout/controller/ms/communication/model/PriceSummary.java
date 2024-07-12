package com.bharath.checkout.controller.ms.communication.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceSummary {
private Double totalMRP;
private Double totalDiscount;
private Double totalAmount;
private Integer noOfCheckItems;
}
