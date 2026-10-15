package ke.co.codejambo.rentflow.auth.repository;

import ke.co.codejambo.rentflow.auth.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByEmailAndDeletedAtIsNull(String email);

    Optional<UserProfile> findByKeycloakUserId(UUID keycloakUserId);

    Optional<UserProfile> findByPhoneHashAndDeletedAtIsNull(String phoneHash);

    boolean existsByEmailAndDeletedAtIsNull(String email);
}
