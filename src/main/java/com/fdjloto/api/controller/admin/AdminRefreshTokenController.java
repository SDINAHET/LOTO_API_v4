package com.fdjloto.api.controller.admin;

import com.fdjloto.api.model.RefreshToken;
import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.RefreshTokenRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Tag(name = "Admin - Refresh Tokens", description = "Admin endpoints for refresh tokens (read-only).")
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRefreshTokenController {

    private final RefreshTokenRepository refreshTokenRepository;

    public AdminRefreshTokenController(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * DTO SAFE : ne renvoie JAMAIS le refresh token brut
     * (uniquement hash + dates + revokedAt + userId)
     */
    public record RefreshTokenAdminView(
            String id,
            String userId,
            String tokenHash,
            Instant createdAt,
            Instant expiresAt,
            Instant revokedAt,
            boolean revoked
    ) {}

    /**
     * ✅ LISTE pour console admin (read-only)
     * URL: GET /api/admin/refresh-tokens
     */
    @GetMapping("/refresh-tokens")
    public ResponseEntity<List<RefreshTokenAdminView>> listRefreshTokens() {

        Iterable<RefreshToken> iterable = refreshTokenRepository.findAll();

        List<RefreshTokenAdminView> out = StreamSupport.stream(iterable.spliterator(), false)
                // tri: derniers en premier (comme HeidiSQL)
                .sorted(Comparator.comparing(
                        (RefreshToken rt) -> rt.getCreatedAt(),
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .map(rt -> {
                    Instant revokedAt = rt.getRevokedAt();

                    // ✅ Compat JPA classique : RefreshToken -> User
                    String userId = null;
                    User u = rt.getUser(); // <-- c'est ça qui remplace getUserId()
                    if (u != null && u.getId() != null) {
                        userId = u.getId().toString();
                    }

                    return new RefreshTokenAdminView(
                            rt.getId(),
                            userId,
                            rt.getTokenHash(),
                            rt.getCreatedAt(),
                            rt.getExpiresAt(),
                            revokedAt,
                            revokedAt != null
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(out);
    }
}
