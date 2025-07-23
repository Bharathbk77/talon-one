package com.app.model;

import lombok.*;
import java.util.Map;

/**
 * DTO for updating user profile in Talon.One.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {
    private String userId;
    private Map<String, Object> attributes; // Custom attributes for Talon.One profile
}
