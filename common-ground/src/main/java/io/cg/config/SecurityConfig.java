package io.cg.config;

import io.cg.security.JWTAuthFilter;
import io.cg.security.JWTAuthenticationProvider;
import io.cg.security.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTAuthenticationProvider jwtAuthenticationProvider;
    private final UserDetailService userDetailService;

    private final JWTAuthFilter jwtAuthFilter;

    @Autowired
    @Lazy
    public SecurityConfig(JWTAuthenticationProvider jwtAuthenticationProvider, UserDetailService userDetailService, JWTAuthFilter jwtAuthFilter) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.userDetailService = userDetailService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests().requestMatchers("/login/**", "/register")
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/agent/**").hasAnyAuthority("ROLE_AGENT")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/**").hasAnyAuthority("ROLE_AGENT", "ROLE_MEMBER")
                .anyRequest()
                .authenticated()
                .and()
                .authenticationProvider(jwtAuthenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .authenticationProvider(jwtAuthenticationProvider)
                .userDetailsService(userDetailService);
        return authenticationManagerBuilder.build();
    }

}
