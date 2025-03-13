package kg.nurtelecom.backend_application.controllers.api;

import kg.nurtelecom.backend_application.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username,
                                      @RequestParam String password,
                                      @RequestParam(required = false, defaultValue = "ROLE_CLIENT") String role) {
        String checkSql = "SELECT count(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{username}, Integer.class);
        if(count != null && count > 0) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if(!role.equals("ROLE_CLIENT") && !role.equals("ROLE_ADMIN")) {
            return ResponseEntity.badRequest().body("Invalid role provided");
        }
        String insertSql = "INSERT INTO users (user_id, username, password, role) VALUES (?, ?, ?, ?)";
        UUID userId = UUID.randomUUID();
        int result = jdbcTemplate.update(insertSql, userId, username, passwordEncoder.encode(password), role);
        if(result > 0) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(500).body("Registration failed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,
                                   @RequestParam String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        String token = JwtUtil.generateToken(username);
        return ResponseEntity.ok(token);
    }
    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint() {
        String username = (String) org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return ResponseEntity.ok("Hello " + username + ", you accessed a protected endpoint!");
    }
}