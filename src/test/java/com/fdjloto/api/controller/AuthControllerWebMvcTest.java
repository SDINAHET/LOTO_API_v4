// package com.fdjloto.api.controller;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fdjloto.api.model.LoginRequest;
// import com.fdjloto.api.model.RefreshToken;
// import com.fdjloto.api.model.User;
// import com.fdjloto.api.repository.RefreshTokenRepository;
// import com.fdjloto.api.repository.TicketGainRepository;
// import com.fdjloto.api.repository.TicketRepository;
// import com.fdjloto.api.repository.UserRepository;
// import com.fdjloto.api.security.JwtUtils;
// import com.fdjloto.api.service.RefreshTokenService;
// import com.fdjloto.api.service.UserService;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.test.web.servlet.MockMvc;

// import java.time.Instant;
// import java.util.List;
// import java.util.Optional;

// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(controllers = AuthController.class)
// @AutoConfigureMockMvc(addFilters = false)
// class AuthControllerWebMvcTest {

//     @Autowired MockMvc mockMvc;
//     @Autowired ObjectMapper objectMapper;

//     @MockBean AuthenticationManager authenticationManager;
//     @MockBean JwtUtils jwtUtils;
//     @MockBean UserService userService;
//     @MockBean PasswordEncoder passwordEncoder;
//     @MockBean UserRepository userRepository;
//     @MockBean RefreshTokenService refreshTokenService;
//     @MockBean RefreshTokenRepository refreshTokenRepository;

//     // ✅ évite des erreurs de contexte (beans qui dépendent de ces repos)
//     @MockBean TicketRepository ticketRepository;
//     @MockBean TicketGainRepository ticketGainRepository;

//     @Test
//     void login3_success_setsCookies_andReturnsToken() throws Exception {
//         var req = new LoginRequest();
//         req.setEmail("user@example.com");
//         req.setPassword("password");

//         Authentication authentication = Mockito.mock(Authentication.class);
//         when(authenticationManager.authenticate(any())).thenReturn(authentication);

//         when(jwtUtils.generateAccessToken(authentication)).thenReturn("ACCESS");
//         when(jwtUtils.generateRefreshToken("user@example.com")).thenReturn("REFRESH");

//         User u = new User();
//         u.setId("11111111-1111-1111-1111-111111111111");
//         u.setEmail("user@example.com");
//         u.setAdmin(false);
//         when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(u));

//         doNothing().when(refreshTokenService).save(eq(u), eq("REFRESH"), any(Instant.class));

//         mockMvc.perform(post("/api/auth/login3")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(req)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.token").value("ACCESS"))
//                 .andExpect(jsonPath("$.message").value("Login successful"))
//                 .andExpect(header().stringValues("Set-Cookie",
//                         org.hamcrest.Matchers.hasItem(org.hamcrest.Matchers.containsString("jwtToken="))))
//                 .andExpect(header().stringValues("Set-Cookie",
//                         org.hamcrest.Matchers.hasItem(org.hamcrest.Matchers.containsString("refreshToken="))))
//                 .andExpect(header().stringValues("Set-Cookie",
//                         org.hamcrest.Matchers.hasItem(org.hamcrest.Matchers.containsString("sid="))));
//     }

//     @Test
//     void login3_invalidCredentials_returns401() throws Exception {
//         var req = new LoginRequest();
//         req.setEmail("user@example.com");
//         req.setPassword("wrong");

//         when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("bad credentials"));

//         mockMvc.perform(post("/api/auth/login3")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(req)))
//                 .andExpect(status().isUnauthorized())
//                 .andExpect(jsonPath("$.message").value("Login failed"));
//     }

//     @Test
//     void refresh_missingCookie_returns401() throws Exception {
//         mockMvc.perform(post("/api/auth/refresh"))
//                 .andExpect(status().isUnauthorized())
//                 .andExpect(jsonPath("$.message").value("Invalid refresh token"));
//     }

//     @Test
//     void refresh_validToken_rotatesAndReturnsNewAccess() throws Exception {
//         when(jwtUtils.validateRefreshToken("OLD_REFRESH")).thenReturn(true);
//         when(jwtUtils.getUserFromJwtToken("OLD_REFRESH")).thenReturn("user@example.com");

//         User u = new User();
//         u.setId("11111111-1111-1111-1111-111111111111");
//         u.setEmail("user@example.com");
//         u.setAdmin(false);
//         when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(u));

//         when(refreshTokenService.hash("OLD_REFRESH")).thenReturn("HASH_OLD");

//         RefreshToken rt = new RefreshToken();
//         rt.setId("rt1");
//         rt.setUser(u);
//         rt.setTokenHash("HASH_OLD");
//         rt.setCreatedAt(Instant.now());
//         rt.setExpiresAt(Instant.now().plusSeconds(3600));
//         when(refreshTokenRepository.findByTokenHash("HASH_OLD")).thenReturn(Optional.of(rt));

//         doNothing().when(refreshTokenService).revoke(rt);

//         when(jwtUtils.generateRefreshToken("user@example.com")).thenReturn("NEW_REFRESH");
//         when(jwtUtils.generateAccessTokenFromEmailAndRoles(eq("user@example.com"), eq(List.of("ROLE_USER"))))
//                 .thenReturn("NEW_ACCESS");
//         doNothing().when(refreshTokenService).save(eq(u), eq("NEW_REFRESH"), any(Instant.class));

//         mockMvc.perform(post("/api/auth/refresh")
//                         .cookie(new jakarta.servlet.http.Cookie("refreshToken", "OLD_REFRESH")))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.token").value("NEW_ACCESS"))
//                 .andExpect(jsonPath("$.message").value("Token refreshed"))
//                 .andExpect(header().stringValues("Set-Cookie",
//                         org.hamcrest.Matchers.hasItem(org.hamcrest.Matchers.containsString("jwtToken="))))
//                 .andExpect(header().stringValues("Set-Cookie",
//                         org.hamcrest.Matchers.hasItem(org.hamcrest.Matchers.containsString("refreshToken="))));
//     }

//     @Test
//     void me_invalidOrMissingToken_returns401() throws Exception {
//         when(jwtUtils.validateAccessToken(anyString())).thenReturn(false);

//         mockMvc.perform(get("/api/auth/me"))
//                 .andExpect(status().isUnauthorized())
//                 .andExpect(jsonPath("$.error").value("User not authenticated"));
//     }
// }
