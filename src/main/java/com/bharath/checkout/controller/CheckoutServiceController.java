package com.bharath.checkout.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bharath.checkout.exception.CheckOutServiceException;
import com.bharath.checkout.model.BillingAddressViewResponse;
import com.bharath.checkout.model.CartCheckoutRequest;
import com.bharath.checkout.model.CheckOutResponse;
import com.bharath.checkout.model.ShippingAddressViewResponse;
import com.bharath.checkout.service.interfaces.CheckoutService;

@RestController
@CrossOrigin
@Validated
public class CheckoutServiceController {
	@Autowired
	private CheckoutService checkoutService;

	@PostMapping(value = "checkout")
	public ResponseEntity<List<CheckOutResponse>> cartCheckOut(@RequestBody List<CartCheckoutRequest> checkoutRequest,
			@RequestParam(name = "userId", required = true) Long userId) throws CheckOutServiceException{
		

		return new ResponseEntity<List<CheckOutResponse>>(checkoutService.cartCheckout(checkoutRequest, userId), HttpStatus.CREATED);
	}
	
	@GetMapping(value="/view-shipping-address")
	public ResponseEntity<ShippingAddressViewResponse> viewShippingAddress(@RequestParam(required = true,name="shippingAddressId") Long shippingAddressId,@RequestParam(name="userId",required=true) Long userId) throws CheckOutServiceException {
		return new ResponseEntity<ShippingAddressViewResponse>(checkoutService.viewShippingAddress(userId,shippingAddressId),HttpStatus.OK);
	}
	
	@GetMapping(value="/view-billing-address")
	public ResponseEntity<BillingAddressViewResponse> viewBillingAddress(@RequestParam(required = true,name="billingAddressId") Long billingAddressId,@RequestParam(name="userId",required=true) Long userId) throws CheckOutServiceException {
		return new ResponseEntity<BillingAddressViewResponse>(checkoutService.viewBillingAddress(userId,billingAddressId),HttpStatus.OK);
	}
}
