package dmu.dasom.api.global.auth.jwt;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.global.auth.dto.TokenBox;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private static final String ACCESS_TOKEN_PREFIX = "ACCESS_";
    private static final String REFRESH_TOKEN_PREFIX = "REFRESH_";
    private static final String BLACKLIST_PREFIX = "BLACKLIST_";

    private final SecretKey secretKey;
    private final StringRedisTemplate redisTemplate;

    public JwtUtil(@Value("${jwt.secret}") final String secretKey, final StringRedisTemplate redisTemplate) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.redisTemplate = redisTemplate;
    }

    // Access, Refresh 토큰 생성
    public TokenBox generateTokenBox(final String email) {
        final TokenBox tokenBox = TokenBox.builder()
                .accessToken(generateToken(email, accessTokenExpiration))
                .refreshToken(generateToken(email, refreshTokenExpiration))
                .build();

        saveTokens(tokenBox, email);

        return tokenBox;
    }

    // 토큰 생성
    public String generateToken(final String email, final long expirationMs) {
        return Jwts.builder()
                .issuer("dmu-dasom.or.kr")
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(this.secretKey)
                .compact();
    }

    // 토큰 저장
    public void saveTokens(final TokenBox tokenBox, final String email) {
        redisTemplate.opsForValue().set(ACCESS_TOKEN_PREFIX.concat(email), tokenBox.getAccessToken(), accessTokenExpiration, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX.concat(email), tokenBox.getRefreshToken(), refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }

    // Request 헤더에서 토큰 추출
    public String parseToken(final String authHeaderValue) {
        return authHeaderValue.split(" ")[1];
    }

    // 토큰으로부터 Claims 추출
    public Claims parseClaims(final String token) {
        try {
            return Jwts.parser()
                    .verifyWith(this.secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SecurityException | MalformedJwtException | SignatureException |
                 UnsupportedJwtException | IllegalArgumentException | ClaimJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        }
    }

    // 토큰의 남은 수명 반환
    public long getRemainingTokenExpiration(final String token) {
        return parseClaims(token).getExpiration().getTime() - System.currentTimeMillis();
    }

    // Access, Refresh 토큰 블랙리스트 추가
    public void blacklistTokens(final String email) {
        final String accessTokenKey = ACCESS_TOKEN_PREFIX.concat(email);
        final String refreshTokenKey = REFRESH_TOKEN_PREFIX.concat(email);

        if (redisTemplate.hasKey(accessTokenKey)) {
            blacklistToken(redisTemplate.opsForValue().get(accessTokenKey), email);
            redisTemplate.delete(accessTokenKey);
        }

        if (redisTemplate.hasKey(refreshTokenKey)) {
            blacklistToken(redisTemplate.opsForValue().get(refreshTokenKey), email);
            redisTemplate.delete(refreshTokenKey);
        }
    }

    // 토큰 블랙리스트 추가
    public void blacklistToken(final String token, final String email) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX.concat(token), email, getRemainingTokenExpiration(token), TimeUnit.MILLISECONDS);
    }

    // 토큰 블랙리스트 확인
    public boolean isBlacklisted(final String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX.concat(token));
    }

    // 토큰 만료 여부 확인
    public boolean isExpired(final String token) {
        try {
            return Jwts.parser()
                    .verifyWith(this.secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }

}
