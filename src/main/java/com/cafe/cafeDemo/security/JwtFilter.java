package com.cafe.cafeDemo.security;

import com.cafe.cafeDemo.model.Usuario;
import com.cafe.cafeDemo.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtUtil.isTokenValid(token)) {
                String usuarioNombre = jwtUtil.extractUsuario(token);
                int versionToken     = jwtUtil.extractVersion(token);

                // Verificar que la versión del token coincide con la BD
                Usuario usuario = usuarioRepository.findByUsuario(usuarioNombre);

                if (usuario == null || usuario.getTokenVersion() != versionToken) {
                    // Sesión desplazada — otra notebook inició sesión después
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"Sesión cerrada por inicio de sesión en otro dispositivo\"}");
                    return;
                }

                String rol = jwtUtil.extractRol(token);
                if (rol == null || rol.isBlank()) rol = "OPERADOR";

                var auth = new UsernamePasswordAuthenticationToken(
                        usuarioNombre, null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
