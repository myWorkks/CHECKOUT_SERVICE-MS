package com.bharath.checkout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bharath.checkout.entity.ShippingAddressDetails;

public interface ShippingAddressDetailsRepository  extends JpaRepository<ShippingAddressDetails, Long>{

}
