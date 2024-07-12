package com.bharath.checkout.controller.ms.communication;

import com.bharath.checkout.controller.ms.communication.model.ViewCartResponse;

public interface CartService {
public ViewCartResponse viewCart(Long userId);
}
