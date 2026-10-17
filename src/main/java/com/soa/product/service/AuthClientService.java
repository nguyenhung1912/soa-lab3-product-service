package com.soa.product.service;

import com.soa.product.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

/**
 * Client service for validating JWT tokens via Auth Service.
 */
@Service
public class AuthClientService {

    private final RestClient restClient;

    @Value("${auth.service.url:http://localhost:8080/auth}")
    private String authServiceUrl;

    public AuthClientService(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Validates the Bearer token by calling the remote Auth Service endpoint.
     */
    public AuthResponse validateTokenWithAuthService(String authHeaderOrToken) {
        if (authHeaderOrToken == null || authHeaderOrToken.trim().isEmpty()) {
            return new AuthResponse(false, "Thiếu token xác thực trong Header Authorization");
        }

        String headerValue = authHeaderOrToken.trim();
        if (!headerValue.startsWith("Bearer ")) {
            headerValue = "Bearer " + headerValue;
        }

        try {
            AuthResponse response = restClient.get()
                    .uri(authServiceUrl)
                    .header(HttpHeaders.AUTHORIZATION, headerValue)
                    .retrieve()
                    .body(AuthResponse.class);

            if (response != null) {
                return response;
            }
            return new AuthResponse(false, "Null Response");
        } catch (HttpClientErrorException.Unauthorized e) {
            AuthResponse errorBody = e.getResponseBodyAs(AuthResponse.class);
            if (errorBody != null && errorBody.getMessage() != null) {
                return errorBody;
            }
            return new AuthResponse(false, "Token không hợp lệ hoặc đã hết hạn");
        } catch (Exception e) {
            return new AuthResponse(false, "Không thể kết nối tới Auth Service");
        }
    }
}
