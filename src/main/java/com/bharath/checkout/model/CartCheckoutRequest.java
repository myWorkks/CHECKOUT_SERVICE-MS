package com.bharath.checkout.model;



import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartCheckoutRequest {

	
	
	private Long billingAddressId;
	private Long shippingAddressId;
}
