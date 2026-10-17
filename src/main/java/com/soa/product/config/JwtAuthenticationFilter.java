package com.soa.product.config;

import com.soa.product.dto.AuthResponse;
import com.soa.product.service.AuthClientService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/*
 * Filter that intercepts HTTP requests and validates JWT tokens via Auth Service.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthClientService authClientService;

    public JwtAuthenticationFilter(AuthClientService authClientService) {
        this.authClientService = authClientService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.trim().isEmpty()) {
            AuthResponse authResponse = authClientService.validateTokenWithAuthService(authHeader);

            if (authResponse.isSuccess() && SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = authResponse.getUserName() != null ? authResponse.getUserName() : "authenticatedUser";

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.emptyList());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                request.setAttribute("authErrorMessage", authResponse.getMessage());
            }
        } else {
            request.setAttribute("authErrorMessage",
                    "Vui lòng cung cấp JWT Token trong Header 'Authorization: Bearer <token>'");
        }

        filterChain.doFilter(request, response);
    }
}
