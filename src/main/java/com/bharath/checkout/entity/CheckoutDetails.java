package com.bharath.checkout.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "shipping_address_id")
	private ShippingAddressDetails shippingAddress;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "billing_address_id")
	private BillingAddressDetails billingAddress;

}
