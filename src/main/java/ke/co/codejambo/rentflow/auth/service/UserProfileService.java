package ke.co.codejambo.rentflow.auth.service;

import ke.co.codejambo.rentflow.auth.dto.CreateProfileRequest;
import ke.co.codejambo.rentflow.auth.entity.UserProfile;
import ke.co.codejambo.rentflow.auth.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserProfile create(CreateProfileRequest req) {
        if (repository.existsByEmailAndDeletedAtIsNull(req.email())) {
            throw new IllegalStateException("Email already registered: " + req.email());
        }

        UserProfile p = new UserProfile();
        p.setKeycloakUserId(req.keycloakUserId());
        p.setEmail(req.email());
        p.setFullName(req.fullName());
        p.setRole(req.role());
        p.setStatus("ACTIVE");
        p.setMfaEnabled(false);

        // Phone encryption will be added in the security hardening pass
        // (PMS-SEC-001 §5.4). For now, store the hash for lookup only.
        if (req.phone() != null && !req.phone().isBlank()) {
            p.setPhoneHash(sha256Hex(req.phone()));
        }

        return repository.save(p);
    }

    @Transactional(readOnly = true)
    public UserProfile getById(UUID id) {
        return repository.findById(id)
            .filter(u -> u.getDeletedAt() == null)
            .orElseThrow(() -> new IllegalStateException("User profile not found: " + id));
    }

    @Transactional(readOnly = true)
    public UserProfile getByEmail(String email) {
        return repository.findByEmailAndDeletedAtIsNull(email)
            .orElseThrow(() -> new IllegalStateException("User profile not found: " + email));
    }

    @Transactional(readOnly = true)
    public List<UserProfile> listAll() {
        return repository.findAll().stream()
            .filter(u -> u.getDeletedAt() == null)
            .toList();
    }

    private static String sha256Hex(String input) {
        try {
            var digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Hash failure", e);
        }
    }
}
