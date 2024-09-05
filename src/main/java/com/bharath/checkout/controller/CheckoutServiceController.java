package com.bharath.checkout.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bharath.checkout.exception.CheckOutServiceException;
import com.bharath.checkout.model.BillingAddressRequest;
import com.bharath.checkout.model.BillingAddressViewResponse;
import com.bharath.checkout.model.CartCheckoutRequest;
import com.bharath.checkout.model.CheckOutResponse;
import com.bharath.checkout.model.ShippingAddressRequest;
import com.bharath.checkout.model.ShippingAddressViewResponse;
import com.bharath.checkout.model.ViewCheckOutResponse;
import com.bharath.checkout.service.interfaces.CheckoutService;
import com.bharath.checkout.utility.CheckoutServiceConstants;

@RestController
@CrossOrigin
@Validated
public class CheckoutServiceController {
	@Autowired
	private CheckoutService checkoutService;

	@PostMapping(value = "checkout")
	public ResponseEntity<String> cartCheckOut(@RequestBody CartCheckoutRequest checkoutRequest,
			@RequestParam(name = "userId", required = true) Long userId) throws CheckOutServiceException {

		checkoutService.cartCheckout(checkoutRequest, userId);
		return new ResponseEntity<String>(CheckoutServiceConstants.CHECK_OUT_SUCCESS, HttpStatus.CREATED);
	}

	@GetMapping(value = "/view-shipping-address")
	public ResponseEntity<List<ShippingAddressViewResponse>> viewShippingAddress(
			@RequestParam(required = true, name = "shippingAddressIds") List<Long> shippingAddressIds,
			@RequestParam(name = "userId", required = true) Long userId) throws CheckOutServiceException {
		return new ResponseEntity<List<ShippingAddressViewResponse>>(
				checkoutService.viewShippingAddress(userId, shippingAddressIds), HttpStatus.OK);
	}

	@GetMapping(value = "/view-billing-address")
	public ResponseEntity<BillingAddressViewResponse> viewBillingAddress(
			@RequestParam(required = true, name = "billingAddressId") Long billingAddressId,
			@RequestParam(name = "userId", required = true) Long userId) throws CheckOutServiceException {
		return new ResponseEntity<BillingAddressViewResponse>(
				checkoutService.viewBillingAddress(userId, billingAddressId), HttpStatus.OK);
	}

	@GetMapping(value = "/view-all-shipping-address")
	public ResponseEntity<List<ShippingAddressViewResponse>> viewAllShippingAddresses(
			@RequestParam(name = "userId", required = true) Long userId) throws CheckOutServiceException {
		return new ResponseEntity<List<ShippingAddressViewResponse>>(checkoutService.viewAllShippingAddress(userId),
				HttpStatus.OK);
	}

	@GetMapping(value = "/view-all-billing-address")
	public ResponseEntity<List<BillingAddressViewResponse>> viewAllBillingAddresses(
			@RequestParam(name = "userId", required = true) Long userId) throws CheckOutServiceException {

		return new ResponseEntity<List<BillingAddressViewResponse>>(checkoutService.viewAllBillingAddress(userId),
				HttpStatus.OK);
	}

	@PostMapping(value = "/add-billing-address")
	public ResponseEntity<List<BillingAddressViewResponse>> addBillingAddress(
			@RequestBody BillingAddressRequest billingAddressRequest,
			@RequestParam(name = "userId", required = true) Long userId) throws CheckOutServiceException {
		return new ResponseEntity<List<BillingAddressViewResponse>>(
				checkoutService.addBillingAddress(billingAddressRequest, userId), HttpStatus.CREATED);
	}

	@PostMapping(value = "/add-shipping-address")
	public ResponseEntity<List<ShippingAddressViewResponse>> addShippingAddress(
			@RequestBody ShippingAddressRequest shippingAddressRequest,
			@RequestParam(name = "userId", required = true) Long userId) throws CheckOutServiceException {
		return new ResponseEntity<List<ShippingAddressViewResponse>>(
				checkoutService.addShippingAddress(shippingAddressRequest, userId), HttpStatus.CREATED);
	}

	@GetMapping("/view-checkout")
	public ResponseEntity<List<ViewCheckOutResponse>> viewCheckOutWithCartProductIds(
			@RequestParam(required = true, name = "cartProductIds") List<Long> cartProductIds)
			throws CheckOutServiceException {
		return new ResponseEntity<List<ViewCheckOutResponse>>(
				checkoutService.viewCheckOutWithCartProductIds(cartProductIds), HttpStatus.OK);

	}

	@DeleteMapping(value="delete-billing-address")
	public ResponseEntity<String> deleteBillingAddress(@RequestParam(name="userId")Long userId,@RequestParam(name="billingAddressId")Long billingAddressId) throws CheckOutServiceException{
		return new ResponseEntity<String>(CheckoutServiceConstants.BILLING_ADDRESS_DELETE_SUCCESS,HttpStatus.OK);
	}
}
