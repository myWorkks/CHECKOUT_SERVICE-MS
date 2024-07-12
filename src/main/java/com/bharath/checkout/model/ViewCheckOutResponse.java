package com.bharath.checkout.model;

import lombok.Data;

@Data
public class ViewCheckOutResponse {
	private Long cartProductId;
	private Long shippingAddressId;
	private Long billingAddressId;
}
