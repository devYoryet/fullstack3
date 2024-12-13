package com.sumativafs3.demo.security;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import com.sumativafs3.demo.models.Rol;
import com.sumativafs3.demo.models.Usuario;

class CustomUserDetailsTest {

    @Test
    void testGetAuthoritiesWithNoRole() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setPassword("password");
        // No establecemos rol, dejándolo como null

        CustomUserDetails userDetails = new CustomUserDetails(usuario);

        // Act
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testGetAuthoritiesWithRole() {
        // Arrange
        Rol rol = new Rol();
        rol.setNombre("ADMIN");

        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setPassword("password");
        usuario.setRol(rol);

        CustomUserDetails userDetails = new CustomUserDetails(usuario);

        // Act
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testGetAuthoritiesWithRolePrefix() {
        // Arrange
        Rol rol = new Rol();
        rol.setNombre("ROLE_ADMIN");

        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setPassword("password");
        usuario.setRol(rol);

        CustomUserDetails userDetails = new CustomUserDetails(usuario);

        // Act
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testUserDetails() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setPassword("password");

        CustomUserDetails userDetails = new CustomUserDetails(usuario);

        // Assert
        assertEquals("test@test.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
    }
}