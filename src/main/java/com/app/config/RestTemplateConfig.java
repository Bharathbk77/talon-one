package com.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

/**
 * Configuration class for RestTemplate used to interact with Talon.One's Integration API.
 * <p>
 * This configuration ensures secure, efficient, and maintainable HTTP communication with Talon.One.
 * It injects the API key securely from application properties and attaches it to all outgoing requests.
 * Additionally, it logs essential request details without exposing sensitive information.
 * </p>
 *
 * <p>
 * Usage:
 * <pre>
 *     &#64;Autowired
 *     private RestTemplate talonOneRestTemplate;
 * </pre>
 * </p>
 */
@Configuration
public class RestTemplateConfig {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);

    /**
     * The Talon.One API key, injected from application properties.
     */
    @Value("${talonone.api-key}")
    private String talonOneApiKey;

    /**
     * Defines a singleton, thread-safe RestTemplate bean configured for Talon.One Integration API.
     *
     * @return configured RestTemplate instance
     */
    @Bean
    public RestTemplate talonOneRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new TalonOneApiRequestInterceptor(talonOneApiKey)));
        return restTemplate;
    }

    /**
     * Interceptor for attaching authentication headers and logging requests to Talon.One.
     */
    private static class TalonOneApiRequestInterceptor implements ClientHttpRequestInterceptor {

        private final String apiKey;

        /**
         * Constructs the interceptor with the provided API key.
         *
         * @param apiKey the Talon.One API key
         */
        public TalonOneApiRequestInterceptor(String apiKey) {
            this.apiKey = apiKey;
        }

        /**
         * Intercepts the HTTP request to add authentication and log essential details.
         *
         * @param request   the HTTP request
         * @param body      the request body
         * @param execution the request execution
         * @return the HTTP response
         * @throws IOException if an I/O error occurs
         */
        @Override
        public ClientHttpResponse intercept(
                ClientHttpRequest request,
                byte[] body,
                ClientHttpRequestExecution execution) throws IOException {

            // Attach Authorization header securely
            request.getHeaders().setBearerAuth(apiKey);

            // Log essential request details (method and URI)
            logger.info("Talon.One API Request: {} {}", request.getMethod(), request.getURI());

            // Do not log headers or body to avoid leaking sensitive data

            return execution.execute(request, body);
        }
    }
}
