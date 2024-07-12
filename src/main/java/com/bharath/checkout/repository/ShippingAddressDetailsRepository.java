package com.bharath.checkout.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bharath.checkout.entity.ShippingAddressDetails;

public interface ShippingAddressDetailsRepository extends JpaRepository<ShippingAddressDetails, Long> {

	Optional<ShippingAddressDetails> findByUserIdAndShippingAddressId(Long userId, Long shippingAddressId);

	List<ShippingAddressDetails> findByUserIdAndShippingAddressIdIn(Long userId, List<Long> shippingAddressId);

	List<ShippingAddressDetails> findByUserId(Long userId);

	Optional<ShippingAddressDetails> findByIsDefaultAndUserId(boolean b, Long userId);

}
