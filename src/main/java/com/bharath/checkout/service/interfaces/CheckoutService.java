package com.bharath.checkout.service.interfaces;

import java.util.List;

import com.bharath.checkout.exception.CheckOutServiceException;
import com.bharath.checkout.model.BillingAddressRequest;
import com.bharath.checkout.model.BillingAddressViewResponse;
import com.bharath.checkout.model.CartCheckoutRequest;
import com.bharath.checkout.model.ShippingAddressRequest;
import com.bharath.checkout.model.ShippingAddressViewResponse;
import com.bharath.checkout.model.ViewCheckOutResponse;

public interface CheckoutService {

	void cartCheckout(CartCheckoutRequest checkoutRequests, Long userId) throws CheckOutServiceException;

	List<ShippingAddressViewResponse> viewShippingAddress(Long userId, List<Long> shippingAddressId)
			throws CheckOutServiceException;

	BillingAddressViewResponse viewBillingAddress(Long userId, Long billingAddressId) throws CheckOutServiceException;

	List<ShippingAddressViewResponse> viewAllShippingAddress(Long userId) throws CheckOutServiceException;

	List<BillingAddressViewResponse> viewAllBillingAddress(Long userId) throws CheckOutServiceException;

	List<BillingAddressViewResponse> addBillingAddress(BillingAddressRequest billingAddressRequest, Long userId)
			throws CheckOutServiceException;

	List<ShippingAddressViewResponse> addShippingAddress(ShippingAddressRequest shippingAddressRequest, Long userId)
			throws CheckOutServiceException;

	List<ViewCheckOutResponse> viewCheckOutWithCartProductIds(List<Long> cartProductIds);

}
