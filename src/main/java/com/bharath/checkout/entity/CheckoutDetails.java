package com.bharath.checkout.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class CheckoutDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long checkOutDetailsId;
	@Column(unique = true)
	private Long cartProductId;

	private Long shippingAddressId;

	private Long billingAddressId;

}
