package dmu.dasom.api.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.common.exception.ErrorResponse;
import dmu.dasom.api.global.auth.dto.LoginRequestDto;
import dmu.dasom.api.global.auth.dto.TokenBox;
import dmu.dasom.api.global.auth.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();

            // 로그인 요청 정보를 파싱
            final LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            // 로그인 요청 정보로 인증 시도
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        } catch (InternalAuthenticationServiceException | IOException e) { // 인증 과정에서 내부 오류 발생 시 (ex. 사용자 정보 없음)
            throw new AuthenticationException("Authentication Failed.", e) {};
        }
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final Authentication authResult) {
        // 기존 토큰 만료 처리
        jwtUtil.blacklistTokens(authResult.getName());

        final Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        final Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        final GrantedAuthority auth = iterator.next();

        final String authority = auth.getAuthority();

        // 토큰 생성
        final TokenBox tokenBox = jwtUtil.generateTokenBox(authResult.getName(), authority);

        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Access-Token", tokenBox.getAccessToken());
        response.setHeader("Refresh-Token", tokenBox.getRefreshToken());
        response.setHeader("Authority", tokenBox.getAuthority());
    }

    @Override
    protected void unsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException failed) throws IOException {
        // 로그인 실패 응답
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse(ErrorCode.LOGIN_FAILED)));
    }

}
