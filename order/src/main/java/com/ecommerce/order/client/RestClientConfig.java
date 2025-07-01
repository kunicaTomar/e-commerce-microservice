package com.ecommerce.order.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;


@Configuration
public class RestClientConfig {
    @Autowired(required=false)
    private ObservationRegistry  observationRegistry;

    @Autowired(required=false)
    private Tracer tracer;

    @Autowired(required=false)
    private Propagator propogator;

    
    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        RestClient.Builder builder =RestClient.builder();
        if(observationRegistry!=null){
            builder.requestInterceptor(createTracingInterceptor());
        }

        return builder;
    }


    private ClientHttpRequestInterceptor createTracingInterceptor() {
        return ((request,body,execution) -> {
            if(tracer!=null && propogator!=null && tracer.currentSpan() !=null){
                propogator.inject(tracer.currentTraceContext().context(),
                request.getHeaders(),
                (carrier,key,value) -> carrier.add(key,value));
            }
            return execution.execute(request,body);
        });
    }



}
