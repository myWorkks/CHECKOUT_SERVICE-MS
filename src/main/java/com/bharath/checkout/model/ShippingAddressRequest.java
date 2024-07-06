package com.bharath.checkout.model;

import com.bharath.checkout.entity.AddressType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShippingAddressRequest {
	private Long shippingAddressId;

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
	private Boolean isDefault;
	private AddressType addressType;
}
