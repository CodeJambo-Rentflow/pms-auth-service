package ke.co.codejambo.rentflow.auth.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/debug")
public class DebugController {

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal Jwt jwt,
                                  JwtAuthenticationToken auth) {
        Map<String, Object> result = new HashMap<>();
        result.put("subject", jwt.getSubject());
        result.put("scopeClaimRaw", Objects.toString(jwt.getClaim("scope"), "<null>"));
        result.put("authorities", auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        result.put("authoritiesRaw", auth.getAuthorities().toString());
        return result;
    }
}
