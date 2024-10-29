// SecurityConfig.java (actualizado)
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
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/roles/crear").permitAll()
                
                // Endpoints de publicaciones
                .requestMatchers(HttpMethod.GET, "/api/publicaciones").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/publicaciones").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/publicaciones/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/publicaciones/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/publicaciones/{id}").hasAnyRole("ADMIN", "USER")
                
                // Endpoints de comentarios
                .requestMatchers(HttpMethod.GET, "/api/publicaciones/*/comentarios").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/api/publicaciones/*/comentarios/*").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/api/publicaciones/*/comentarios").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.PUT, "/api/publicaciones/*/comentarios/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/publicaciones/*/comentarios/*").hasRole("ADMIN")
                
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