package io.cg.api;

import io.cg.dto.LoginCredentials;
import io.cg.dto.LoginSuccess;
import io.cg.enums.Role;
import io.cg.security.UserDetailService;
import io.cg.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/login")
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailService userDetailsService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/agent")
    public ResponseEntity<LoginSuccess> agentLogin(@RequestBody LoginCredentials loginCredentials) {
        log.info("Login request received for agent");
        return login (loginCredentials, Role.ROLE_AGENT.name());
    }

    @PostMapping("/admin")
    public ResponseEntity<LoginSuccess> adminLogin(@RequestBody LoginCredentials loginCredentials) {
        log.info("Login request received for admin");
        return login (loginCredentials, Role.ROLE_ADMIN.name());
    }

    @PostMapping("/member")
    public ResponseEntity<LoginSuccess> memberLogin(@RequestBody LoginCredentials loginCredentials) {
        log.info("Login request received for member");
        return login (loginCredentials, Role.ROLE_MEMBER.name());
    }

    private ResponseEntity<LoginSuccess> login(LoginCredentials loginCredentials, String userType) {
        log.info("Login request received for user {}", loginCredentials.getEmail());

        String emailAndType = loginCredentials.getEmail()+ ":" + userType;
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(emailAndType, loginCredentials.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("Login failed for user " + loginCredentials.getEmail());
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(emailAndType, loginCredentials.getPassword());
        authenticationManager.authenticate(token);
        UserDetails user = userDetailsService.loadUserByUsername(emailAndType);

        String jwtToken = jwtUtil.generateToken(
                new HashMap<>() {{
                    put("userType", userType);
                }},
                user);

        return ResponseEntity.ok(new LoginSuccess(jwtToken));
    }
}
