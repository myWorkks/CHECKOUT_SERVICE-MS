package com.bharath.checkout.service.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.checkout.controller.ms.communication.CartService;
import com.bharath.checkout.controller.ms.communication.model.ViewCartProductResponse;
import com.bharath.checkout.controller.ms.communication.model.ViewCartResponse;
import com.bharath.checkout.entity.BillingAddressDetails;
import com.bharath.checkout.entity.CheckoutDetails;
import com.bharath.checkout.entity.ShippingAddressDetails;
import com.bharath.checkout.exception.CheckOutServiceException;
import com.bharath.checkout.exception.CheckOutServiceExceptionMessages;
import com.bharath.checkout.model.BillingAddressRequest;
import com.bharath.checkout.model.BillingAddressViewResponse;
import com.bharath.checkout.model.CartCheckoutRequest;
import com.bharath.checkout.model.ShippingAddressRequest;
import com.bharath.checkout.model.ShippingAddressViewResponse;
import com.bharath.checkout.model.ViewCheckOutResponse;
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
	@Autowired
	private CartService cartService;

	@Override
	@Transactional
	public void cartCheckout(CartCheckoutRequest checkoutRequests, Long userId) throws CheckOutServiceException {
		ViewCartResponse viewCartResponse = cartService.viewCart(userId);
		if (viewCartResponse == null)
			throw new CheckOutServiceException(CheckOutServiceExceptionMessages.ERROR_CALLING_CART_SERVICE);
		List<ViewCartProductResponse> cartProducts = viewCartResponse.getCartProducts();
		List<CheckoutDetails> checkOutDetails = new ArrayList<CheckoutDetails>();
		List<Long> cartProductIds = null;
		if (cartProducts != null && !cartProducts.isEmpty()) {
			cartProductIds = cartProducts.stream().filter(cartproduct -> cartproduct.getIsChecked())
					.map(cartProduct -> cartProduct.getCartProductId()).collect(Collectors.toList());
		}
		if (cartProductIds != null && !cartProductIds.isEmpty()) {
			checkOutDetails = checkOutRepository.findByCartProductIdIn(cartProductIds);
			Map<Long, CheckoutDetails> cartProductIdAndCheckOutDetailsMap = checkOutDetails.stream().collect(Collectors
					.toMap(checkOutDetail -> checkOutDetail.getCartProductId(), checkOutDetail -> checkOutDetail));

			for (Long id : cartProductIds) {
				if (!cartProductIdAndCheckOutDetailsMap.containsKey(id)) {
					checkOutDetails.add(mapToCheckoutDetails(id, checkoutRequests));
				}
			}
		}

		if (checkOutDetails != null && !checkOutDetails.isEmpty()) {
			checkOutRepository.saveAll(checkOutDetails);
		}

	}

	private CheckoutDetails mapToCheckoutDetails(Long cartProductId, CartCheckoutRequest checkoutRequests) {
		CheckoutDetails checkOut = new CheckoutDetails();
		checkOut.setCartProductId(cartProductId);
		checkOut.setBillingAddressId(checkoutRequests.getBillingAddressId());
		checkOut.setShippingAddressId(checkoutRequests.getShippingAddressId());
		return checkOut;
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
		shippingAddressDetails
				.setIsDefault(shippingAddress.getIsDefault() == null ? false : shippingAddress.getIsDefault());
		shippingAddressDetails.setAddressType(shippingAddress.getAddressType());
		// return shippingAddressDetailsRepository.save(shippingAddressDetails);
		return shippingAddressDetails;
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
		billingAddressDetails.setIsDefault(
				billingAddressRequest.getIsDefault() == null ? false : billingAddressRequest.getIsDefault());
		billingAddressDetails.setAddressType(billingAddressRequest.getAddressType());
		// return billingAddressDetailsRepository.save(billingAddressDetails);
		return billingAddressDetails;

	}

	@Override
	public List<ShippingAddressViewResponse> viewShippingAddress(Long userId, List<Long> shippingAddressIds)
			throws CheckOutServiceException {
		List<ShippingAddressViewResponse> shippingAddressViewResponses = new ArrayList<ShippingAddressViewResponse>();
		if (shippingAddressIds != null && !shippingAddressIds.isEmpty()) {
			if (shippingAddressIds.size() == 1) {
				Optional<ShippingAddressDetails> optionalShippingAddress = shippingAddressDetailsRepository
						.findByUserIdAndShippingAddressId(userId, shippingAddressIds.get(0));

				ShippingAddressDetails shippingAddressDetails = optionalShippingAddress
						.orElseThrow(() -> new CheckOutServiceException(
								CheckOutServiceExceptionMessages.SHIPPING_ADDRESS_NOT_FOUND));
				shippingAddressViewResponses.add(mapToShippingAddressViewResponse(shippingAddressDetails));
			} else {
				List<ShippingAddressDetails> shippingAddressDetails = shippingAddressDetailsRepository
						.findByUserIdAndShippingAddressIdIn(userId, shippingAddressIds);
				List<ShippingAddressViewResponse> shippingAddresses = shippingAddressDetails.stream()
						.map(shippingaddress -> mapToShippingAddressViewResponse(shippingaddress)).toList();
				shippingAddressViewResponses.addAll(shippingAddresses);
			}
		}

		return shippingAddressViewResponses;
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
		shippingAddressViewResponse.setIsDefault(shippingAddressDetails.getIsDefault());
		shippingAddressViewResponse.setEmail(shippingAddressDetails.getEmail());
		shippingAddressViewResponse.setAddressType(shippingAddressDetails.getAddressType());
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
		billingAddressViewResponse.setIsDefault(billingAddressDetails.getIsDefault());
		billingAddressViewResponse.setAddressType(billingAddressDetails.getAddressType());
		return billingAddressViewResponse;

	}

	@Override
	public List<ShippingAddressViewResponse> viewAllShippingAddress(Long userId) throws CheckOutServiceException {
		List<ShippingAddressDetails> shippingAddressDetailsList = shippingAddressDetailsRepository.findByUserId(userId);
		if (shippingAddressDetailsList != null && !shippingAddressDetailsList.isEmpty()) {
			return shippingAddressDetailsList.stream()
					.map(shippingAddress -> mapToShippingAddressViewResponse(shippingAddress))
					.collect(Collectors.toList());
		}

		throw new CheckOutServiceException(CheckOutServiceExceptionMessages.NO_SHIPPING_ADDRESS_ADDED);
	}

	@Override
	public List<BillingAddressViewResponse> viewAllBillingAddress(Long userId) throws CheckOutServiceException {
		List<BillingAddressDetails> billingAddressDetailsList = billingAddressDetailsRepository.findByUserId(userId);
		if (billingAddressDetailsList != null && !billingAddressDetailsList.isEmpty()) {
			return billingAddressDetailsList.stream()
					.map(billingAddress -> mapToBillingAddressViewResponse(billingAddress))
					.collect(Collectors.toList());
		}

		throw new CheckOutServiceException(CheckOutServiceExceptionMessages.NO_BILLING_ADDRESS_ADDED);
	}

	@Override
	public List<BillingAddressViewResponse> addBillingAddress(BillingAddressRequest billingAddressRequest, Long userId)
			throws CheckOutServiceException {

		BillingAddressDetails billingAddressDetails = saveBillingAddress(billingAddressRequest, userId);

		if (billingAddressRequest.getIsDefault() == null ? false : billingAddressRequest.getIsDefault()) {
			List<BillingAddressDetails> billingAddressDetailsToBeSaved = new ArrayList<BillingAddressDetails>();
			billingAddressDetailsToBeSaved.add(billingAddressDetails);
			Optional<BillingAddressDetails> optBillingAddressDetails = billingAddressDetailsRepository
					.findByIsDefaultAndUserId(true, userId);
			if (optBillingAddressDetails.isPresent()) {
				BillingAddressDetails billAddressDetails = optBillingAddressDetails.get();
				billAddressDetails.setIsDefault(false);
				billingAddressDetailsToBeSaved.add(billAddressDetails);
			}
			billingAddressDetailsRepository.saveAll(billingAddressDetailsToBeSaved);
		} else {
			billingAddressDetailsRepository.save(billingAddressDetails);
		}
		return viewAllBillingAddress(userId);
	}

	@Override
	public List<ShippingAddressViewResponse> addShippingAddress(ShippingAddressRequest shippingAddressRequest,
			Long userId) throws CheckOutServiceException {
		ShippingAddressDetails shippingAddressDetails = saveShippingAddress(shippingAddressRequest, userId);

		if (shippingAddressRequest.getIsDefault() == null ? false : shippingAddressRequest.getIsDefault()) {
			List<ShippingAddressDetails> shippingAddressDetailsToBeSaved = new ArrayList<ShippingAddressDetails>();
			shippingAddressDetailsToBeSaved.add(shippingAddressDetails);
			Optional<ShippingAddressDetails> optShippingAddressDetails = shippingAddressDetailsRepository
					.findByIsDefaultAndUserId(true, userId);
			if (optShippingAddressDetails.isPresent()) {
				ShippingAddressDetails shipAddressDetails = optShippingAddressDetails.get();
				shipAddressDetails.setIsDefault(false);
				shippingAddressDetailsToBeSaved.add(shipAddressDetails);
			}
			shippingAddressDetailsRepository.saveAll(shippingAddressDetailsToBeSaved);
		} else {
			shippingAddressDetailsRepository.save(shippingAddressDetails);
		}
		return viewAllShippingAddress(userId);
	}

	@Override
	public List<ViewCheckOutResponse> viewCheckOutWithCartProductIds(List<Long> cartProductIds) {
		List<ViewCheckOutResponse> viewCheckOutResponses = new ArrayList<ViewCheckOutResponse>();
		List<CheckoutDetails> checkOutDetails = checkOutRepository.findByCartProductIdIn(cartProductIds);
		if (checkOutDetails != null && !checkOutDetails.isEmpty()) {
			for (CheckoutDetails checkOut : checkOutDetails) {
				if (checkOut != null) {
					ViewCheckOutResponse viewCheckOutResponse = new ViewCheckOutResponse();
					viewCheckOutResponse.setCartProductId(checkOut.getCartProductId());
					viewCheckOutResponse.setBillingAddressId(checkOut.getBillingAddressId());
					viewCheckOutResponse.setShippingAddressId(checkOut.getShippingAddressId());
					viewCheckOutResponses.add(viewCheckOutResponse);
				}
			}
		}
		return viewCheckOutResponses;
	}

}
//if (checkoutRequests == null || checkoutRequests.isEmpty())
//throw new CheckOutServiceException(CheckOutServiceExceptionMessages.CART_PRODUCTS_IS_EMPTY);
//List<CheckOutResponse> checkOutResponseList = new ArrayList<CheckOutResponse>();
//for (CartCheckoutRequest cartCheckoutRequest : checkoutRequests) {
//Optional<CheckoutDetails> optCheckOut = checkOutRepository
//		.findByCartProductId(cartCheckoutRequest.getCartProductId());
//if (optCheckOut.isPresent())
//	throw new CheckOutServiceException(CheckOutServiceExceptionMessages.ALREADY_ADDRESS_SET);
//if (cartCheckoutRequest.getBillingAddress().getBillingAddressId() == null)
//	billingAddressDetails = saveBillingAddress(cartCheckoutRequest.getBillingAddress(), userId);
//else {
//
//	Optional<BillingAddressDetails> optional = billingAddressDetailsRepository
//			.findById(cartCheckoutRequest.getBillingAddress().getBillingAddressId());
//
//	billingAddressDetails = optional
//			.orElseGet(() -> saveBillingAddress(cartCheckoutRequest.getBillingAddress(), userId));
//}
//
//if (cartCheckoutRequest.getShippingAddress().getShippingAddressId() == null)
//	shippingAddressDetails = saveShippingAddress(cartCheckoutRequest.getShippingAddress(), userId);
//else {
//
//	Optional<ShippingAddressDetails> optional = shippingAddressDetailsRepository
//			.findById(cartCheckoutRequest.getShippingAddress().getShippingAddressId());
//
//	shippingAddressDetails = optional
//			.orElseGet(() -> saveShippingAddress(cartCheckoutRequest.getShippingAddress(), userId));
//}
//
//CheckoutDetails checkoutDetails = new CheckoutDetails();
//checkoutDetails.setBillingAddress(billingAddressDetails);
//checkoutDetails.setShippingAddress(shippingAddressDetails);
//checkoutDetails.setCartProductId(cartCheckoutRequest.getCartProductId());
//checkoutDetails = checkOutRepository.save(checkoutDetails);
//CheckOutResponse checkOutResponse = new CheckOutResponse();
//checkOutResponse.setBillingAddressId(checkoutDetails.getBillingAddress().getBillingAddressId());
//checkOutResponse.setShippingAddressId(checkoutDetails.getShippingAddress().getShippingAddressId());
//checkOutResponse.setCartProductId(checkoutDetails.getCartProductId());
//
//checkOutResponseList.add(checkOutResponse);
//}
//return checkOutResponseList;