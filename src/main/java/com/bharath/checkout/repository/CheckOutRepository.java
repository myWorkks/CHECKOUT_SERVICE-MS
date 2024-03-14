package com.bharath.checkout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bharath.checkout.entity.CheckoutDetails;

public interface CheckOutRepository extends JpaRepository<CheckoutDetails, Long> {

}
