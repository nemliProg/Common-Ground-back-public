package io.cg.security;

import io.cg.entities.Admin;
import io.cg.entities.Member;
import io.cg.enums.Role;
import io.cg.repositories.AdminRepository;
import io.cg.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailService implements UserDetailsService {

    final AdminRepository adminRepository;

    final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String emailAndType) throws UsernameNotFoundException {
        log.info("Inside UserDetailService for user {}", emailAndType);
        String email    = emailAndType.split(":")[0];
        String type     = emailAndType.split(":")[1];

        switch (type) {
            case "admin" -> {
                Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
                return new User(email, admin.getPassword(), Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name())));
            }
            case "agent" -> {
                Member agent = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Agent not found"));
                return new User(email, agent.getPassword(), Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_AGENT.name())));
            }
            case "member" -> {
                Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("member not found"));
                return new User(email, member.getPassword(), Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_MEMBER.name())));
            }
            default -> throw new UsernameNotFoundException("User not found");
        }
    }
}
