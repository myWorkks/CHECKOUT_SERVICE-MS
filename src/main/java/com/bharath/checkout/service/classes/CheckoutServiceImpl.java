package com.bharath.checkout.service.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.checkout.entity.BillingAddressDetails;
import com.bharath.checkout.entity.CheckoutDetails;
import com.bharath.checkout.entity.ShippingAddressDetails;
import com.bharath.checkout.exception.CheckOutServiceException;
import com.bharath.checkout.exception.CheckOutServiceExceptionMessages;
import com.bharath.checkout.model.BillingAddressRequest;
import com.bharath.checkout.model.BillingAddressViewResponse;
import com.bharath.checkout.model.CartCheckoutRequest;
import com.bharath.checkout.model.CheckOutResponse;
import com.bharath.checkout.model.ShippingAddressRequest;
import com.bharath.checkout.model.ShippingAddressViewResponse;
import com.bharath.checkout.repository.BillingAddressDetailsRepository;
import com.bharath.checkout.repository.CheckOutRepository;
import com.bharath.checkout.repository.ShippingAddressDetailsRepository;
import com.bharath.checkout.service.interfaces.CheckoutService;

@Service(value = "checkOutService")
@Transactional(rollbackForClassName = { "CheckOutServiceException", "RuntimeException" })
public class CheckoutServiceImpl implements CheckoutService {

	@Autowired
	private CheckOutRepository checkOutRepository;
	@Autowired
	private ShippingAddressDetailsRepository shippingAddressDetailsRepository;
	@Autowired
	private BillingAddressDetailsRepository billingAddressDetailsRepository;

	private BillingAddressDetails billingAddressDetails = null;

	private ShippingAddressDetails shippingAddressDetails = null;

	@Override
	@Transactional
	public List<CheckOutResponse> cartCheckout(List<CartCheckoutRequest> checkoutRequests, Long userId)
			throws CheckOutServiceException {

		if (checkoutRequests == null || checkoutRequests.isEmpty())
			throw new CheckOutServiceException(CheckOutServiceExceptionMessages.CART_PRODUCTS_IS_EMPTY);
		List<CheckOutResponse> checkOutResponseList = new ArrayList<CheckOutResponse>();
		for (CartCheckoutRequest cartCheckoutRequest : checkoutRequests) {
			Optional<CheckoutDetails> optCheckOut = checkOutRepository
					.findByCartProductId(cartCheckoutRequest.getCartProductId());
			if (optCheckOut.isPresent())
				throw new CheckOutServiceException(CheckOutServiceExceptionMessages.ALREADY_ADDRESS_SET);
			if (cartCheckoutRequest.getBillingAddress().getBillingAddressId() == null)
				billingAddressDetails = saveBillingAddress(cartCheckoutRequest.getBillingAddress(), userId);
			else {

				Optional<BillingAddressDetails> optional = billingAddressDetailsRepository
						.findById(cartCheckoutRequest.getBillingAddress().getBillingAddressId());

				billingAddressDetails = optional
						.orElseGet(() -> saveBillingAddress(cartCheckoutRequest.getBillingAddress(), userId));
			}

			if (cartCheckoutRequest.getShippingAddress().getShippingAddressId() == null)
				shippingAddressDetails = saveShippingAddress(cartCheckoutRequest.getShippingAddress(), userId);
			else {

				Optional<ShippingAddressDetails> optional = shippingAddressDetailsRepository
						.findById(cartCheckoutRequest.getShippingAddress().getShippingAddressId());

				shippingAddressDetails = optional
						.orElseGet(() -> saveShippingAddress(cartCheckoutRequest.getShippingAddress(), userId));
			}

			CheckoutDetails checkoutDetails = new CheckoutDetails();
			checkoutDetails.setBillingAddress(billingAddressDetails);
			checkoutDetails.setShippingAddress(shippingAddressDetails);
			checkoutDetails.setCartProductId(cartCheckoutRequest.getCartProductId());
			checkoutDetails = checkOutRepository.save(checkoutDetails);
			CheckOutResponse checkOutResponse = new CheckOutResponse();
			checkOutResponse.setBillingAddressId(checkoutDetails.getBillingAddress().getBillingAddressId());
			checkOutResponse.setShippingAddressId(checkoutDetails.getShippingAddress().getShippingAddressId());
			checkOutResponse.setCartProductId(checkoutDetails.getCartProductId());

			checkOutResponseList.add(checkOutResponse);
		}
		return checkOutResponseList;
	}

	private ShippingAddressDetails saveShippingAddress(ShippingAddressRequest shippingAddress, Long userId) {
		ShippingAddressDetails shippingAddressDetails = new ShippingAddressDetails();
		shippingAddressDetails.setAddressLine1(shippingAddress.getAddressLine1());
		shippingAddressDetails.setAddressLine2(shippingAddress.getAddressLine2());
		shippingAddressDetails.setCity(shippingAddress.getCity());
		shippingAddressDetails.setState(shippingAddress.getState());
		shippingAddressDetails.setPostalCode(shippingAddress.getPostalCode());
		shippingAddressDetails.setCountry(shippingAddress.getCountry());
		shippingAddressDetails.setPhoneNumber(shippingAddress.getPhoneNumber());
		shippingAddressDetails.setEmail(shippingAddress.getEmail());
		shippingAddressDetails.setUserId(userId); // Assuming userId is passed separately
		shippingAddressDetails.setFirstName(shippingAddress.getFirstName());
		shippingAddressDetails.setLastName(shippingAddress.getLastName());

		return shippingAddressDetailsRepository.save(shippingAddressDetails);
	}

	private BillingAddressDetails saveBillingAddress(BillingAddressRequest billingAddressRequest, Long userId) {
		BillingAddressDetails billingAddressDetails = new BillingAddressDetails();
		billingAddressDetails.setAddressLine1(billingAddressRequest.getAddressLine1());
		billingAddressDetails.setAddressLine2(billingAddressRequest.getAddressLine2());
		billingAddressDetails.setCity(billingAddressRequest.getCity());
		billingAddressDetails.setState(billingAddressRequest.getState());
		billingAddressDetails.setPostalCode(billingAddressRequest.getPostalCode());
		billingAddressDetails.setCountry(billingAddressRequest.getCountry());
		billingAddressDetails.setPhoneNumber(billingAddressRequest.getPhoneNumber());
		billingAddressDetails.setEmail(billingAddressRequest.getEmail());
		billingAddressDetails.setUserId(userId);
		billingAddressDetails.setFirstName(billingAddressRequest.getFirstName());
		billingAddressDetails.setLastName(billingAddressRequest.getLastName());
		return billingAddressDetailsRepository.save(billingAddressDetails);

	}

	@Override
	public ShippingAddressViewResponse viewShippingAddress(Long userId, Long shippingAddressId)
			throws CheckOutServiceException {

		Optional<ShippingAddressDetails> optionalShippingAddress = shippingAddressDetailsRepository
				.findByUserIdAndShippingAddressId(userId, shippingAddressId);

		ShippingAddressDetails shippingAddressDetails = optionalShippingAddress.orElseThrow(
				() -> new CheckOutServiceException(CheckOutServiceExceptionMessages.SHIPPING_ADDRESS_NOT_FOUND));
		return mapToShippingAddressViewResponse(shippingAddressDetails);
	}

	private ShippingAddressViewResponse mapToShippingAddressViewResponse(
			ShippingAddressDetails shippingAddressDetails) {

		ShippingAddressViewResponse shippingAddressViewResponse = new ShippingAddressViewResponse();
		shippingAddressViewResponse.setAddressLine1(shippingAddressDetails.getAddressLine1());
		shippingAddressViewResponse.setAddressLine2(shippingAddressDetails.getAddressLine2());
		shippingAddressViewResponse.setCity(shippingAddressDetails.getCity());
		shippingAddressViewResponse.setCountry(shippingAddressDetails.getCountry());
		shippingAddressViewResponse.setPostalCode(shippingAddressDetails.getPostalCode());
		shippingAddressViewResponse.setPhoneNumber(shippingAddressDetails.getPhoneNumber());
		shippingAddressViewResponse.setShippingAddressId(shippingAddressDetails.getShippingAddressId());
		shippingAddressViewResponse.setFirstName(shippingAddressDetails.getFirstName());
		shippingAddressViewResponse.setLastName(shippingAddressDetails.getLastName());
		shippingAddressViewResponse.setState(shippingAddressDetails.getState());

		shippingAddressViewResponse.setEmail(shippingAddressDetails.getEmail());
		return shippingAddressViewResponse;
	}

	@Override
	public BillingAddressViewResponse viewBillingAddress(Long userId, Long billingAddressId)
			throws CheckOutServiceException {
		Optional<BillingAddressDetails> optionalShippingAddress = billingAddressDetailsRepository
				.findByBillingAddressIdAndUserId(billingAddressId, userId);

		BillingAddressDetails billingAddressDetails = optionalShippingAddress.orElseThrow(
				() -> new CheckOutServiceException(CheckOutServiceExceptionMessages.BILLING_ADDRESS_NOT_FOUND));
		return mapToBillingAddressViewResponse(billingAddressDetails);

	}

	private BillingAddressViewResponse mapToBillingAddressViewResponse(BillingAddressDetails billingAddressDetails) {
		BillingAddressViewResponse billingAddressViewResponse = new BillingAddressViewResponse();
		billingAddressViewResponse.setAddressLine1(billingAddressDetails.getAddressLine1());
		billingAddressViewResponse.setAddressLine2(billingAddressDetails.getAddressLine2());
		billingAddressViewResponse.setCity(billingAddressDetails.getCity());
		billingAddressViewResponse.setCountry(billingAddressDetails.getCountry());
		billingAddressViewResponse.setPostalCode(billingAddressDetails.getPostalCode());
		billingAddressViewResponse.setPhoneNumber(billingAddressDetails.getPhoneNumber());
		billingAddressViewResponse.setBillingAddressId(billingAddressDetails.getBillingAddressId());
		billingAddressViewResponse.setFirstName(billingAddressDetails.getFirstName());
		billingAddressViewResponse.setLastName(billingAddressDetails.getLastName());
		billingAddressViewResponse.setState(billingAddressDetails.getState());
		billingAddressViewResponse.setEmail(billingAddressDetails.getEmail());
		
		return billingAddressViewResponse;

	}

}
