package com.sumativafs3.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sumativafs3.demo.security.CustomAccessDeniedHandler;
import com.sumativafs3.demo.security.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/auth/registro").permitAll()
                
                // Rutas para administración de productos
                .requestMatchers(HttpMethod.GET, "/api/productos/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/api/productos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")
                
                // Rutas para compras
                .requestMatchers(HttpMethod.GET, "/api/compras").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/api/compras").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/api/compras/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.PUT, "/api/compras/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/compras/**").hasRole("ADMIN")
                
                // Rutas para administración de usuarios
                .requestMatchers("/api/usuarios/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/usuarios/perfil").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/perfil").hasAnyRole("ADMIN", "USER")
                
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> basic
                .authenticationEntryPoint(authenticationEntryPoint)
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}