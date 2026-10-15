package ke.co.codejambo.rentflow.auth.controller;

import jakarta.validation.Valid;
import ke.co.codejambo.rentflow.auth.dto.CreateProfileRequest;
import ke.co.codejambo.rentflow.auth.dto.ProfileResponse;
import ke.co.codejambo.rentflow.auth.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profiles")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_profile:write') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody CreateProfileRequest req) {
        var created = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProfileResponse.from(created));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_profile:read') or hasRole('SYSTEM_ADMIN')")
    public ProfileResponse getById(@PathVariable UUID id) {
        return ProfileResponse.from(service.getById(id));
    }

    @GetMapping("/by-email/{email}")
    @PreAuthorize("hasAuthority('SCOPE_profile:read') or hasRole('SYSTEM_ADMIN')")
    public ProfileResponse getByEmail(@PathVariable String email) {
        return ProfileResponse.from(service.getByEmail(email));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_profile:read') or hasRole('SYSTEM_ADMIN')")
    public List<ProfileResponse> list() {
        return service.listAll().stream().map(ProfileResponse::from).toList();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "success", false,
            "error", Map.of("code", "CONFLICT", "message", ex.getMessage())
        ));
    }
}
