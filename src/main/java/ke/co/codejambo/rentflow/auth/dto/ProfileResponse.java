package ke.co.codejambo.rentflow.auth.dto;

import ke.co.codejambo.rentflow.auth.entity.UserProfile;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ProfileResponse(
    UUID id,
    UUID keycloakUserId,
    String email,
    String fullName,
    String role,
    String status,
    boolean mfaEnabled,
    OffsetDateTime createdAt
) {
    public static ProfileResponse from(UserProfile p) {
        return new ProfileResponse(
            p.getId(),
            p.getKeycloakUserId(),
            p.getEmail(),
            p.getFullName(),
            p.getRole(),
            p.getStatus(),
            p.isMfaEnabled(),
            p.getCreatedAt()
        );
    }
}
