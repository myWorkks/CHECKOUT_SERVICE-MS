package com.bharath.checkout.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
public class ShippingAddressDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	private Long userId;
}
