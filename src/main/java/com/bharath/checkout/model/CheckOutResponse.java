package com.bharath.checkout.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckOutResponse {
	private Long cartProductId;
	private Long shippingAddressId;
	private Long billingAddressId;
}
