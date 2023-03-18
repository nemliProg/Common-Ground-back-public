package io.cg.api;

import io.cg.dto.LoginCredentials;
import io.cg.dto.LoginSuccess;
import io.cg.entities.Admin;
import io.cg.entities.Member;
import io.cg.entities.parents.User;
import io.cg.enums.Role;
import io.cg.repositories.AdminRepository;
import io.cg.repositories.MemberRepository;
import io.cg.security.UserDetailService;
import io.cg.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/login")
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailService userDetailsService;
    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailService userDetailsService, AdminRepository adminRepository, MemberRepository memberRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.adminRepository = adminRepository;
        this.memberRepository = memberRepository;
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
        String username;
        if (userType.equals(Role.ROLE_ADMIN.name())){
            Optional<Admin> admin = adminRepository.findByEmail(loginCredentials.getEmail());
            username = admin.map(User::getUsername).orElse(null);

        } else {
            Optional<Member> member = memberRepository.findByEmail(loginCredentials.getEmail());
            username = member.map(User::getUsername).orElse(null);
        }


        String jwtToken = jwtUtil.generateToken(
                new HashMap<>() {{
                    put("userType", userType);
                    put("username", username);
                }},
                user);

        return ResponseEntity.ok(new LoginSuccess(jwtToken, username, userType, loginCredentials.getEmail()));
    }
}
