package org.example.key_info.core.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.auth.provider.JwtProvider;
import org.example.key_info.core.auth.util.JwtUtils;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token == null) {
            fc.doFilter(request, response);
            return;
        }
        try {
            jwtProvider.validateAccessToken(token);

            final Claims claims = jwtProvider.getAccessClaims(token);
            final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
            jwtInfoToken.setAuthenticated(true);
            jwtInfoToken.setUserId(UUID.fromString(claims.getSubject()));
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);

            String url = ((HttpServletRequest) request).getRequestURI();

            if(!url.contains("auth") && jwtInfoToken.getAuthorities().contains(Role.UNSPECIFIED)) {
                throw new ExceptionInApplication("Access denied for UNSPECIFIED role", ExceptionType.FORBIDDEN);
            }
        } catch (ExceptionInApplication e) {
            response.getWriter().write(e.getMessage());
            HttpServletResponse hsr = (HttpServletResponse) response;
            hsr.setStatus(401);
            return;
        }

        fc.doFilter(request, response);
     }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
