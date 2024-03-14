package com.bharath.checkout.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingAddressRequest {
	private Long billingAddressId;

	private String firstName;

	private String lastName;

	private String addressLine1;

	private String addressLine2;

	private String city;

	private String state;

	private String postalCode;

	private String country;

	private String phoneNumber;

	private String email;

	
}
