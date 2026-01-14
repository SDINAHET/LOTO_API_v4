// package com.fdjloto.api.controller;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fdjloto.api.dto.TicketDTO;
// import com.fdjloto.api.model.Ticket;
// import com.fdjloto.api.model.User;
// import com.fdjloto.api.repository.TicketGainRepository;
// import com.fdjloto.api.repository.TicketRepository;
// import com.fdjloto.api.security.JwtUtils;
// import com.fdjloto.api.service.TicketService;
// import com.fdjloto.api.service.UserService;
// import jakarta.servlet.http.Cookie;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.doThrow;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(TicketController.class)
// @AutoConfigureMockMvc(addFilters = false)
// class TicketControllerWebMvcTest {

//     @Autowired MockMvc mockMvc;
//     @Autowired ObjectMapper objectMapper;

//     @MockBean TicketService ticketService;
//     @MockBean UserService userService;
//     @MockBean JwtUtils jwtUtils;

//     // évite crash contexte si injectés ailleurs / autowired
//     @MockBean TicketRepository ticketRepository;
//     @MockBean TicketGainRepository ticketGainRepository;

//     private User mkUser(String id, String email) {
//         User u = new User();
//         u.setId(id);
//         u.setEmail(email);
//         u.setAdmin(false);
//         return u;
//     }

//     private Ticket mkTicket(UUID id, User owner) {
//         Ticket t = new Ticket();
//         t.setId(id);              // UUID attendu
//         t.setUser(owner);         // pas de setUserId()
//         t.setCreatedAt(LocalDateTime.of(2025, 3, 15, 12, 30, 45));
//         t.setUpdatedAt(LocalDateTime.of(2025, 3, 16, 15, 45, 30));
//         return t;
//     }

//     @Test
//     void getTickets_missingJwtCookie_returns403() throws Exception {
//         mockMvc.perform(get("/api/tickets"))
//                 .andExpect(status().isForbidden())
//                 .andExpect(content().string("Missing token"));
//     }

//     @Test
//     void getTickets_user_returnsOwnTickets() throws Exception {
//         User u = mkUser("u1", "user@example.com");

//         when(jwtUtils.getUserFromJwtToken("TOKEN")).thenReturn("user@example.com");
//         when(userService.getUserByEmail("user@example.com")).thenReturn(Optional.of(u));

//         UUID ticketId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
//         TicketDTO dto = new TicketDTO(mkTicket(ticketId, u));

//         when(ticketService.getTicketsByUserId("u1")).thenReturn(List.of(dto));

//         mockMvc.perform(get("/api/tickets")
//                         .cookie(new Cookie("jwtToken", "TOKEN")))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].id").value(ticketId.toString()))
//                 .andExpect(jsonPath("$[0].userId").value("u1"));
//     }

//     @Test
//     void createTicket_missingJwtCookie_returns403() throws Exception {
//         TicketDTO body = new TicketDTO();

//         mockMvc.perform(post("/api/tickets")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(body)))
//                 .andExpect(status().isForbidden())
//                 .andExpect(content().string("Missing token"));
//     }

//     @Test
//     void createTicket_ok_returnsCreatedTicket() throws Exception {
//         User u = mkUser("u1", "user@example.com");

//         when(jwtUtils.getUserFromJwtToken("TOKEN")).thenReturn("user@example.com");
//         when(userService.getUserByEmail("user@example.com")).thenReturn(Optional.of(u));

//         UUID createdId = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
//         Ticket created = mkTicket(createdId, u);

//         when(ticketService.createTicket(eq("u1"), any(TicketDTO.class))).thenReturn(created);

//         mockMvc.perform(post("/api/tickets")
//                         .cookie(new Cookie("jwtToken", "TOKEN"))
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(new TicketDTO())))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(createdId.toString()))
//                 .andExpect(jsonPath("$.userId").value("u1"));
//     }

//     @Test
//     void deleteTicket_notOwner_returns403() throws Exception {
//         User other = mkUser("other", "other@example.com");

//         when(jwtUtils.getUserFromJwtToken("TOKEN")).thenReturn("other@example.com");
//         when(userService.getUserByEmail("other@example.com")).thenReturn(Optional.of(other));

//         UUID ticketId = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

//         // Le controller appelle direct ticketService.deleteTicket(ticketId, userId)
//         doThrow(new RuntimeException("Forbidden: you don't own this ticket"))
//                 .when(ticketService).deleteTicket(ticketId.toString(), "other");

//         mockMvc.perform(delete("/api/tickets/" + ticketId)
//                         .cookie(new Cookie("jwtToken", "TOKEN")))
//                 .andExpect(status().isForbidden())
//                 .andExpect(content().string("Forbidden: you don't own this ticket"));
//     }
// }
