package com.bharath.checkout.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bharath.checkout.entity.CheckoutDetails;

public interface CheckOutRepository extends JpaRepository<CheckoutDetails, Long> {

	Optional<CheckoutDetails> findByCartProductId(Long cartProductId);

	List<CheckoutDetails> findByCartProductIdIn(List<Long> cartProductIds);

}
