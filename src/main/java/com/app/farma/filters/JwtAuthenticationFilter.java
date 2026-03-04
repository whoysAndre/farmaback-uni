package com.app.farma.filters;

import com.app.farma.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {

    // 1. Leer el header Authorization
    final String authHeader = request.getHeader("Authorization");

    // 2. Si no hay token o no empieza con "Bearer ", dejar pasar (sin autenticar)
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    // 3. Extraer el token (quitar "Bearer ")
    final String jwt = authHeader.substring(7);

    try {
      // 4. Extraer el email/username del token
      final String userEmail = jwtService.extractUsername(jwt);

      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

        // 5. Cargar el usuario de la BD
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        // 6. Validar el token
        if (jwtService.isTokenValid(jwt, userDetails)) {

          // 7. Crear el objeto de autenticación
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()   // Roles del usuario
          );
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          // 8. Guardar en el SecurityContext
          SecurityContextHolder.getContext().setAuthentication(authToken);

          // Debug: Verificar qué roles tiene el usuario
          System.out.println("Usuario autenticado: " + userEmail);
          System.out.println("Roles: " + userDetails.getAuthorities());
        }
      }
    } catch (Exception e) {
      // Log del error pero no interrumpimos el flujo
      System.out.println("Error procesando token JWT: " + e.getMessage());
    }

    // ¡IMPORTANTE! Siempre continuar con la cadena de filtros
    filterChain.doFilter(request, response);
  }
}
