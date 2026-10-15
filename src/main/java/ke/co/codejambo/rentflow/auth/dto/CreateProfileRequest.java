package ke.co.codejambo.rentflow.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record CreateProfileRequest(
    @NotNull UUID keycloakUserId,
    @NotBlank @Email String email,
    @NotBlank String fullName,
    @Pattern(regexp = "^\\+254[0-9]{9}$", message = "Phone must be a valid Kenyan number (e.g., +254712345678)")
    String phone,
    @NotBlank @Pattern(regexp = "LANDLORD|PROPERTY_MANAGER|TENANT|SYSTEM_ADMIN",
        message = "Role must be LANDLORD, PROPERTY_MANAGER, TENANT, or SYSTEM_ADMIN")
    String role
) {}
