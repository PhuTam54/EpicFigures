package com.example.apigateway.filter;

import com.example.apigateway.util.JwtUtil;
import com.example.apigateway.validator.RouteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            System.out.println("Processing request in JwtAuthenticationFilter");

            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                    System.out.println("JWT: " + authHeader);
                }
                try {
                    jwtUtil.validateJwtToken(authHeader);
                } catch (Exception e) {
                    System.out.println("Invalid access...!");
                    throw new RuntimeException("Unauthorized access to the application");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}