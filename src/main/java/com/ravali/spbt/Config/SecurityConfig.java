package com.ravali.spbt.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public SecurityFilterChain customSecurityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs (use cautiously in production)

                // Stateless session (suitable for REST APIs)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Define authorization rules
                .authorizeRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll() // Allow access to public endpoints
                        .requestMatchers("api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())                          // All other requests require authentication

                //Enable HTTP Basic Authentication
                .httpBasic(httpBasicConfigurer -> {});

        return http.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager(){
        UserDetails adminUser = User
                .withUsername(adminUsername)
                .password(passwordEncoder().encode(adminPassword))
                .roles("NORMAL_ADMIN", "ADMIN")
                .build();

        UserDetails superUser = User
                .withUsername("super" + adminUsername)
                .password(passwordEncoder().encode("super" + adminPassword))
                .roles("SUPER_ADMIN", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(adminUser, superUser);
    }

}
