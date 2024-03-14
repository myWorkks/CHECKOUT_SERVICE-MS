package com.bharath.checkout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bharath.checkout.entity.BillingAddressDetails;

public interface BillingAddressDetailsRepository extends JpaRepository<BillingAddressDetails, Long> {

}
