package com.bharath.checkout.service.interfaces;

import java.util.List;

import com.bharath.checkout.exception.CheckOutServiceException;
import com.bharath.checkout.model.BillingAddressViewResponse;
import com.bharath.checkout.model.CartCheckoutRequest;
import com.bharath.checkout.model.CheckOutResponse;
import com.bharath.checkout.model.ShippingAddressViewResponse;

public interface CheckoutService {

	List<CheckOutResponse> cartCheckout(List<CartCheckoutRequest> checkoutRequests,Long userId) throws CheckOutServiceException;

	ShippingAddressViewResponse viewShippingAddress(Long userId,Long shippingAddressId) throws CheckOutServiceException;

	BillingAddressViewResponse viewBillingAddress(Long userId,Long billingAddressId) throws CheckOutServiceException;

}
