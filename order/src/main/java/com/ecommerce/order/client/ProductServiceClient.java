package com.ecommerce.order.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.ecommerce.order.dto.ProductResponse;

@HttpExchange
public interface ProductServiceClient {
    
    @GetExchange("/api/products/{id}")
    ProductResponse getProductDetails(@PathVariable Long id);

}
