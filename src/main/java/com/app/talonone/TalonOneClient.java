package talonone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

/**
 * TalonOneClient is a reusable and centralized client for interacting with Talon.One's Integration API.
 * It provides methods to update user profiles, evaluate sessions for rewards, and confirm loyalty points.
 * <p>
 * Configuration properties (base URL and API key) are loaded from application.properties.
 * All requests include the API key in the Authorization header as 'Bearer {apiKey}'.
 * <p>
 * Usage example:
 * <pre>
 *     @Autowired
 *     private TalonOneClient talonOneClient;
 *     
 *     talonOneClient.updateProfile("user123", profileDto);
 *     RewardsResponse rewards = talonOneClient.evaluateSession(sessionDto);
 *     talonOneClient.confirmLoyalty("user123", 150.0);
 * </pre>
 */
@Component
public class TalonOneClient {

    @Value("${talonone.base-url}")
    private String baseUrl;

    @Value("${talonone.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    /**
     * Constructs a TalonOneClient with the provided RestTemplate.
     * @param restTemplate the RestTemplate to use for HTTP requests
     */
    public TalonOneClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Updates a user profile in Talon.One.
     *
     * @param userId the unique identifier of the user
     * @param dto    the profile data to update (ProfileDTO)
     * @throws TalonOneClientException if the request fails or Talon.One returns an error
     */
    public void updateProfile(String userId, ProfileDTO dto) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/v1/profiles/{userId}")
                .buildAndExpand(userId)
                .toUriString();

        HttpHeaders headers = createHeaders();
        HttpEntity<ProfileDTO> request = new HttpEntity<>(dto, headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
        } catch (HttpStatusCodeException ex) {
            throw new TalonOneClientException("Failed to update profile: " + ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            throw new TalonOneClientException("Unexpected error updating profile", ex);
        }
    }

    /**
     * Evaluates a session in Talon.One to determine applicable rewards and discounts.
     *
     * @param dto the session data to evaluate (SessionDTO)
     * @return RewardsResponse containing personalized rewards and discounts
     * @throws TalonOneClientException if the request fails or Talon.One returns an error
     */
    public RewardsResponse evaluateSession(SessionDTO dto) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/v1/sessions")
                .toUriString();

        HttpHeaders headers = createHeaders();
        HttpEntity<SessionDTO> request = new HttpEntity<>(dto, headers);

        try {
            ResponseEntity<RewardsResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, RewardsResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new TalonOneClientException("Failed to evaluate session: Unexpected response status " + response.getStatusCode());
            }
        } catch (HttpStatusCodeException ex) {
            throw new TalonOneClientException("Failed to evaluate session: " + ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            throw new TalonOneClientException("Unexpected error evaluating session", ex);
        }
    }

    /**
     * Confirms loyalty points for a user in Talon.One after a successful order.
     *
     * @param userId      the unique identifier of the user
     * @param totalAmount the total amount of the order to confirm
     * @throws TalonOneClientException if the request fails or Talon.One returns an error
     */
    public void confirmLoyalty(String userId, double totalAmount) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/v1/loyalty/{userId}/confirm")
                .buildAndExpand(userId)
                .toUriString();

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Assuming Talon.One expects a JSON object like {"totalAmount": 123.45}
        String body = String.format("{\"totalAmount\": %s}", totalAmount);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
        } catch (HttpStatusCodeException ex) {
            throw new TalonOneClientException("Failed to confirm loyalty: " + ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            throw new TalonOneClientException("Unexpected error confirming loyalty", ex);
        }
    }

    /**
     * Creates HTTP headers with Authorization and Content-Type.
     *
     * @return HttpHeaders with required headers set
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}

/**
 * Exception thrown when an error occurs during communication with the Talon.One API.
 */
class TalonOneClientException extends RuntimeException {
    public TalonOneClientException(String message) {
        super(message);
    }

    public TalonOneClientException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Assume ProfileDTO, SessionDTO, and RewardsResponse are imported from your DTO/model packages.
