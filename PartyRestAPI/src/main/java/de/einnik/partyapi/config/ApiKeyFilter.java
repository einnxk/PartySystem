package de.einnik.partyapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that only requests which provide a Key defined
 * in application.properties is able to get a response by our
 * data and not every one
 */
@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    /**
     * Get the Key from the Properties File
     */
    @Value("${plugin.apiKey}")
    private String apiKey;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String key = request.getHeader("api-key");

        if (key == null || !(key.equals(apiKey))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}