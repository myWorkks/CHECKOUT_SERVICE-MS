package com.bharath.checkout.controller.ms.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.bharath.checkout.controller.ms.communication.model.ViewCartResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bharath.checkout.exception.CheckOutServiceException;

import reactor.core.publisher.Mono;

@Component
public class CartServiceImpl implements CartService {

	@Autowired
	private WebClient.Builder webclient;

	@Value("${cart.ms.url}")
	private String cartServiceUrl;
	@Value("${cart.ms.viewcart.endpoint}")
	private String viewCartEndPoint;

	@Override
	public ViewCartResponse viewCart(Long userId) {

		return webclient.baseUrl(cartServiceUrl).build().get()
				.uri(uriBuilder -> uriBuilder.path(viewCartEndPoint).queryParam("cartId", userId).build())
				.retrieve().bodyToMono(ViewCartResponse.class).onErrorResume(WebClientResponseException.class, ex -> {
					String responseCode = String.valueOf(ex.getRawStatusCode());
					if (responseCode.startsWith("2"))
						return Mono.empty();
					else if (responseCode.equals("400")) {

						ObjectMapper objectMapper = new ObjectMapper();
						JsonNode jsonNode = null;
						try {
							jsonNode = objectMapper.readTree(ex.getResponseBodyAsString());
						} catch (JsonProcessingException e) {
							throw new RuntimeException(e);
						}
						String errorMessage = jsonNode.get("errorMessage").asText();
						return Mono.error(() -> new CheckOutServiceException(errorMessage));
					}

				else
						return Mono.error(ex);
				}).block();
	}

}
