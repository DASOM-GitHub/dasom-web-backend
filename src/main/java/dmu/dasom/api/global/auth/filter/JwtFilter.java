package dmu.dasom.api.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.common.exception.ErrorResponse;
import dmu.dasom.api.global.auth.jwt.JwtUtil;
import dmu.dasom.api.global.auth.userdetails.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String HEADER_PREFIX = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 토큰 추출
        final String authHeaderValue = request.getHeader(HEADER_PREFIX);

        // 토큰이 없거나 Bearer 토큰이 아닌 경우
        if (authHeaderValue == null || !authHeaderValue.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = jwtUtil.parseToken(authHeaderValue);

        // 토큰이 블랙리스트에 등록되어 있거나 만료된 경우
        if (jwtUtil.isBlacklisted(token) || jwtUtil.isExpired(token)) {
            response.setStatus(ErrorCode.TOKEN_EXPIRED.getStatus());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse(ErrorCode.TOKEN_EXPIRED)));
            return;
        }

        // 토큰이 유효한 경우 SecurityContext에 인증 정보 저장
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.parseClaims(token).getSubject());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (CustomException e) {
            response.setStatus(e.getErrorCode().getStatus());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse(e.getErrorCode())));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
