package com.ecommerce.order.client;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ProductServiceClientConfig {

    @Bean
    public ProductServiceClient productServiceClientInterface(RestClient.Builder restClientBuilder){
        RestClient restClient=restClientBuilder
                    .baseUrl("http://product-service")
                    .defaultStatusHandler(HttpStatusCode::is4xxClientError,((request,response)->Optional.empty()))
                    .build();
        RestClientAdapter adapter =RestClientAdapter.create(restClient);

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        ProductServiceClient productServiceClient = factory.createClient(ProductServiceClient.class);

        return productServiceClient;


    }

}
