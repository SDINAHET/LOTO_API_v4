// package com.fdjloto.api.controller;

// import com.fdjloto.api.model.User;
// import com.fdjloto.api.repository.TicketGainRepository;
// import com.fdjloto.api.repository.TicketRepository;
// import com.fdjloto.api.repository.UserRepository;
// import com.fdjloto.api.security.JwtUtils;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.List;
// import java.util.Optional;

// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// class SecurityIntegrationTest {

//     @Autowired MockMvc mockMvc;

//     @MockBean JwtUtils jwtUtils;

//     // ✅ évite la casse du contexte via d’autres beans/controllers
//     @MockBean TicketRepository ticketRepository;
//     @MockBean TicketGainRepository ticketGainRepository;
//     @MockBean UserRepository userRepository;

//     @Test
//     void protectedEndpoint_withoutToken_returns401() throws Exception {
//         mockMvc.perform(get("/api/protected/userinfo"))
//                 .andExpect(status().isUnauthorized())
//                 .andExpect(content().contentTypeCompatibleWith("application/json"))
//                 .andExpect(jsonPath("$.error").value("Unauthorized"));
//     }

//     @Test
//     void adminEndpoint_withUserRole_returns403() throws Exception {
//         when(jwtUtils.validateAccessToken("TOKEN")).thenReturn(true);
//         when(jwtUtils.getUserFromJwtToken("TOKEN")).thenReturn("user@example.com");
//         when(jwtUtils.getRolesFromJwtToken("TOKEN")).thenReturn(List.of("ROLE_USER"));

//         User u = new User();
//         u.setId("u1");
//         u.setEmail("user@example.com");
//         u.setAdmin(false);
//         when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(u));

//         mockMvc.perform(get("/api/admin/users")
//                         .cookie(new jakarta.servlet.http.Cookie("jwtToken", "TOKEN")))
//                 .andExpect(status().isForbidden())
//                 .andExpect(content().contentTypeCompatibleWith("application/json"))
//                 .andExpect(jsonPath("$.error").value("Forbidden"));
//     }
// }
