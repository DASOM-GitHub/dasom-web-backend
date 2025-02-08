package dmu.dasom.api.global.auth.filter;

import dmu.dasom.api.global.auth.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLogoutFilter extends OncePerRequestFilter {

    private static final String LOGOUT_URI = "/api/auth/logout";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 로그아웃 요청 검증
        if (request.getRequestURI().equals(LOGOUT_URI) && request.getMethod().equals(HttpMethod.POST.name()) && SecurityContextHolder.getContext().getAuthentication() != null) {
            // 로그아웃 요청 시 토큰 만료 처리
            jwtUtil.blacklistTokens(SecurityContextHolder.getContext().getAuthentication().getName());
            response.setStatus(HttpStatus.OK.value());
            return;
        }

        filterChain.doFilter(request, response);
    }

}
