// package com.fdjloto.api.controller;

// import com.fdjloto.api.model.LoginRequest;
// import com.fdjloto.api.model.User;
// import com.fdjloto.api.security.JwtUtils;
// import com.fdjloto.api.service.UserService;

// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.enums.ParameterIn;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.responses.ApiResponses;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import jakarta.servlet.http.HttpServletResponse;
// import io.swagger.v3.oas.annotations.media.Content;
// import io.swagger.v3.oas.annotations.media.Schema;


// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.bind.annotation.*;
// import jakarta.servlet.http.Cookie;
// import java.util.UUID;
// import java.util.Map;
// import java.util.HashMap;
// import java.util.Optional;

// import com.fdjloto.api.payload.MessageResponse;
// import com.fdjloto.api.repository.UserRepository;
// import jakarta.annotation.security.PermitAll;
// import io.swagger.v3.oas.annotations.Parameter;

// /**
//  * **Authentication Controller (`AuthController`)**
//  *
//  * This controller manages user authentication, login, logout, and user info retrieval via JWT.
//  */
// @Tag(name = "Authentication", description = "Endpoints for user authentication, login, logout, and account management.")
// // @CrossOrigin(origins = "http://127.0.0.1:5500") // 🔥 Allows CORS for Live Server
// // @CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
// // @CrossOrigin(
// //    origins = "http://127.0.0.1:5500",
// //    allowCredentials = "true",
// //    allowedHeaders = "*",
// //    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
// //)
// @CrossOrigin(
//     origins = {
//         "http://127.0.0.1:5500",
//         "http://localhost:5500"
//     },
//     allowCredentials = "true"
// )
// @RestController
// @RequestMapping("/api/auth")
// public class AuthController {

//     private final AuthenticationManager authenticationManager;
//     private final JwtUtils jwtUtils;
//     private final UserService userService;
//     private final PasswordEncoder passwordEncoder;
//     private final UserRepository userRepository; // ✅ Adding the repository

//     // Define JWT cookie name as a constant
//     private static final String JWT_COOKIE_NAME = "jwtToken";

//     // Constructor to initialize dependencies
//     public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
//         this.authenticationManager = authenticationManager;
//         this.jwtUtils = jwtUtils;
//         this.userService = userService;
//         this.passwordEncoder = passwordEncoder;
//         this.userRepository = userRepository; // ✅ Injecting the repository
//     }

//     /**
//      * **User Login (JWT)**
//      *
//      * @param email    User email.
//      * @param password User password.
//      * @return **200 OK** - Returns a JWT token on successful authentication.
//      * @return **401 Unauthorized** - If authentication fails.
//      */
//     @Operation(summary = "User login with JWT", description = "Authenticates a user and returns a JWT token.")
//     @ApiResponses({
//             @ApiResponse(responseCode = "200", description = "Successful login, returns JWT token."),
//             @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials.")
//     })
//     // 🔑 Simple authentication that returns a JWT token
//     @PostMapping("/login4")
//     public ResponseEntity<String> authenticateUser(@RequestParam String email, @RequestParam String password) {
//         Authentication authentication = authenticationManager.authenticate(
//                 new UsernamePasswordAuthenticationToken(email, password)
//         );
//         SecurityContextHolder.getContext().setAuthentication(authentication);
//         String jwt = jwtUtils.generateJwtToken(authentication);
//         return ResponseEntity.ok(jwt);
//     }

//     /**
//      * **Login with JWT stored in HTTP-only cookie**
//      *
//      * @param loginRequest User login details.
//      * @param response     HTTP response to store the JWT token in a cookie.
//      * @return **200 OK** - Returns JWT token and success message.
//      * @return **401 Unauthorized** - If authentication fails.
//      */
//     @Operation(summary = "User login with JWT stored in cookies", description = "Authenticates a user and stores JWT in a secure cookie.")
//     @ApiResponses({
//             @ApiResponse(responseCode = "200", description = "Successful login, JWT stored in a secure cookie."),
//             @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials.")
//     })
//     // 🔑 Authentication with JWT token stored in cookie & returned in response
//     @PostMapping("/login3")
//     public ResponseEntity<Map<String, String>> authenticateUserWithCookieAndLocalStorage(
//             @RequestBody LoginRequest loginRequest,
//             HttpServletResponse response
//     ) {
//         try {
//             // 🔐 Authentication process
//             Authentication authentication = authenticationManager.authenticate(
//                     new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
//             );
//             SecurityContextHolder.getContext().setAuthentication(authentication);

//             // 🔑 Generate JWT token
//             String jwt = jwtUtils.generateJwtToken(authentication);

//             // 🍪 1. Store JWT in a secure cookie
//             Cookie jwtCookie = new Cookie("jwtToken", jwt);
//             jwtCookie.setHttpOnly(false);  // HttpOnly for security against XSS attacks
//             jwtCookie.setSecure(false);   // ⚠️ Set 'true' in production (HTTPS required)
//             jwtCookie.setPath("/");
//             jwtCookie.setMaxAge(10 * 60); // Expiry: 10 minutes
//             response.addCookie(jwtCookie);

//             // 🔥 2. Configure SameSite for CORS
//             // 🚨 Use "None" in production (HTTPS required)
//             String cookieHeader = String.format("%s=%s; HttpOnly; Path=/; Max-Age=%d; SameSite=None; Secure=%b",
//                     "jwtToken", jwt, 10 * 60, false);
//             response.addHeader("Set-Cookie", cookieHeader);

//             // 📝 3. Return the token in JSON response for Local Storage
//             Map<String, String> responseBody = new HashMap<>();
//             responseBody.put("token", jwt);
//             responseBody.put("message", "Login successful");

//             // 📜 Log the generated JWT
//             System.out.println("🔑 JWT generated: " + jwt);

//             return ResponseEntity.ok(responseBody);

//         } catch (Exception e) {
//             // ❌ Authentication failure
//             Map<String, String> errorResponse = new HashMap<>();
//             errorResponse.put("message", "Login failed");
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
//         }
//     }


//     /**
//      * **User Registration**
//      *
//      * @param user User object containing registration details.
//      * @return **200 OK** - Returns the newly registered user.
//      * @return **400 Bad Request** - If the request contains invalid data.
//      */
//     @Operation(summary = "Register a new user", description = "Creates a new user account with encrypted password.")
//     @ApiResponses({
//             @ApiResponse(responseCode = "200", description = "User registered successfully."),
//             @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data.")
//     })
//     // 🛠️ User registration
//     @PostMapping("/register")
//     @PermitAll // <-- important if method security is enabled
// //    public ResponseEntity<User> registerUser(@RequestBody User user) {
// //        user.setId(UUID.randomUUID().toString());
// //        user.setPassword(passwordEncoder.encode(user.getPassword()));
// //        return ResponseEntity.ok(userService.createUser(user));
// //    }
// //    public ResponseEntity<User> registerUser(@RequestBody User user) {
// //        System.out.println("Register endpoint hit. User email: " + user.getEmail());
// //        System.out.println("Headers: " + user.toString());
// //        user.setId(UUID.randomUUID().toString());
// //        user.setPassword(passwordEncoder.encode(user.getPassword()));
// //        User created = userService.createUser(user);
// //        System.out.println("User created: " + created.getEmail());
// //        return ResponseEntity.ok(created);
// //    }
//     public ResponseEntity<User> registerUser(@RequestBody User user) {
//        user.setId(UUID.randomUUID().toString());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setAdmin(false); // 🔒 Sécurité : jamais admin à l’inscription
//        return ResponseEntity.ok(userService.createUser(user));
//     }

//     /**
//      * 🔄 Update user details
//      *
//      * @param id   User ID (UUID) to update
//      * @param user User object containing new details
//      * @return Updated user
//      */
//     @Operation(
//         summary = "Update a user",
//         description = "Updates the details of an existing user by providing their ID.",
//         responses = {
//             @ApiResponse(responseCode = "200", description = "User successfully updated",
//                 content = @Content(mediaType = "application/json",
//                 schema = @Schema(implementation = User.class))),
//             @ApiResponse(responseCode = "400", description = "Invalid request"),
//             @ApiResponse(responseCode = "404", description = "User not found"),
//             @ApiResponse(responseCode = "500", description = "Internal server error")
//         }
//     )
//     // 🔄 Update user details
//     @PutMapping("/{id}")
//     public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User user) {
//         user.setPassword(passwordEncoder.encode(user.getPassword()));
//         return ResponseEntity.ok(userService.updateUser(id, user));
//     }


//     /**
//      * ✅ Health check for AuthController
//      *
//      * This endpoint verifies that the authentication controller is up and running.
//      *
//      * @return JSON message confirming that the controller is operational
//      */
//     @Operation(
//         summary = "Check AuthController status",
//         description = "Returns a simple message to verify that the AuthController is operational.",
//         responses = {
//             @ApiResponse(responseCode = "200", description = "AuthController is operational",
//                 content = @Content(mediaType = "application/json",
//                 schema = @Schema(example = "{\"message\": \"AuthController OK\"}"))),
//             @ApiResponse(responseCode = "500", description = "Internal server error")
//         }
//     )
//     @GetMapping("/ping")
//     public ResponseEntity<Map<String, String>> ping() {
//         Map<String, String> res = new HashMap<>();
//         res.put("message", "AuthController OK");
//         return ResponseEntity.ok(res);
//     }


//     /**
//      * 🔐 Retrieve JWT token from cookie
//      *
//      * This endpoint retrieves the JWT token stored in the "jwtToken" cookie.
//      * If the cookie is missing or empty, access is denied.
//      *
//      * @param jwtToken JWT stored in cookies (optional)
//      * @return JWT token if present, otherwise an unauthorized error
//      */
//     @Operation(
//         summary = "Get JWT from cookie",
//         description = "Retrieves the JWT token stored in the 'jwtToken' cookie.",
//         responses = {
//             @ApiResponse(responseCode = "200", description = "JWT successfully retrieved",
//                 content = @Content(mediaType = "application/json",
//                 schema = @Schema(example = "{\"jwtToken\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"))),
//             @ApiResponse(responseCode = "401", description = "JWT missing",
//                 content = @Content(mediaType = "application/json",
//                 schema = @Schema(example = "{\"message\": \"JWT manquant\"}"))),
//             @ApiResponse(responseCode = "500", description = "Internal server error")
//         }
//     )
//     @GetMapping("/token")
//     public ResponseEntity<Map<String, String>> getJwtFromCookie(
//             @Parameter(
//                 name = "jwtToken",
//                 description = "JWT token stored in cookies",
//                 required = false,
//                 in = ParameterIn.COOKIE
//             )
//             @CookieValue(name = "jwtToken", required = false) String jwtToken
//     ) {
//         if (jwtToken == null || jwtToken.isBlank()) {
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                     .body(Map.of("message", "JWT manquant"));
//         }
//         return ResponseEntity.ok(Map.of("jwtToken", jwtToken));
//     }


//     /**
//      * **Retrieve User Info**
//      *
//      * @param token JWT token from cookie.
//      * @return **200 OK** - Returns user information.
//      * @return **401 Unauthorized** - If the user is not authenticated.
//      * @return **404 Not Found** - If the user does not exist.
//      */
//     @Operation(summary = "Get authenticated user info", description = "Retrieves user details based on JWT token.")
//     @ApiResponses({
//             @ApiResponse(responseCode = "200", description = "User authenticated successfully."),
//             @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT."),
//             @ApiResponse(responseCode = "404", description = "User not found.")
//     })
//     // 👤 Retrieve user information based on JWT token
//     @GetMapping("/me")
//     public ResponseEntity<Map<String, String>> getUserInfo(
//             @CookieValue(name = "jwtToken", required = false) String token) {

//         if (token == null || !jwtUtils.validateJwtToken(token)) {
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                                 .body(Map.of("error", "User not authenticated"));
//         }

//         String email = jwtUtils.getUserFromJwtToken(token);

//         // ✅ Retrieve the user from email
//         Optional<User> user = userRepository.findByEmail(email);
//         if (user.isEmpty()) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                                 .body(Map.of("error", "User not found"));
//         }

//         User u = user.get();
//         Map<String, String> response = new HashMap<>();
//         response.put("id", u.getId().toString()); // ✅ Convert ID to String
//         response.put("email", email);
//         response.put("first_name", u.getFirstName());
//         response.put("last_name", u.getLastName());
//         response.put("message", "User authenticated");

//         return ResponseEntity.ok(response);
//     }

//     // 🔑 Authentication for Swagger integration
//     @PostMapping("/login-swagger")
//     public ResponseEntity<Map<String, String>> authenticateUserForSwagger(
//             @RequestBody LoginRequest loginRequest,
//             HttpServletResponse response
//     ) {
//         try {
//             Authentication authentication = authenticationManager.authenticate(
//                     new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
//             );
//             SecurityContextHolder.getContext().setAuthentication(authentication);

//             String jwt = jwtUtils.generateJwtToken(authentication);

//             // 🍪 Store JWT in a secure cookie
//             Cookie jwtCookie = new Cookie("jwtToken", jwt);
//             jwtCookie.setHttpOnly(false);
//             jwtCookie.setSecure(false);   // ⚠️ Set 'true' in production (HTTPS required)
//             jwtCookie.setPath("/");
//             jwtCookie.setMaxAge(10 * 60); // Expiry: 10 minutes
//             jwtCookie.setDomain("localhost");
//             jwtCookie.setAttribute("SameSite", "None");
//             response.addCookie(jwtCookie);

//             Map<String, String> responseBody = new HashMap<>();
//             responseBody.put("token", jwt);
//             responseBody.put("message", "Login successful");

//             return ResponseEntity.ok(responseBody);

//         } catch (Exception e) {
//             Map<String, String> errorResponse = new HashMap<>();
//             errorResponse.put("message", "Login failed");
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
//         }
//     }

//     /**
//      * **User Logout**
//      *
//      * @param response HTTP response to clear JWT cookie.
//      * @return **200 OK** - Logout successful.
//      */
//     @Operation(summary = "User logout", description = "Clears JWT cookie and logs out the user.")
//     @ApiResponses({
//             @ApiResponse(responseCode = "200", description = "User logged out successfully.")
//     })
//     // 🔄 Logout and invalidate JWT token
//     @PostMapping("/logout")
//     public ResponseEntity<Map<String, String>> logoutWithCookie(HttpServletResponse response) {
//         // 🔐 Invalidate SecurityContext
//         SecurityContextHolder.clearContext();

//         // 🍪 Remove JWT Cookie
//         Cookie jwtCookie = new Cookie(JWT_COOKIE_NAME, null);
//         jwtCookie.setHttpOnly(false);
//         jwtCookie.setSecure(false); // 🔒 Set to true in production
//         jwtCookie.setPath("/");
//         jwtCookie.setMaxAge(0); // ❌ Expires immediately

//         response.addCookie(jwtCookie);

//         // ✅ Successful logout response
//         Map<String, String> responseBody = new HashMap<>();
//         responseBody.put("message", "Logout successful");
//         return ResponseEntity.ok(responseBody);
//     }
// }
