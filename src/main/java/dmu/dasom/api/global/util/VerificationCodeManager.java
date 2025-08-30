package dmu.dasom.api.global.util;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class VerificationCodeManager {

    private static final long EXPIRATION_TIME_SECONDS = 180; // 인증 코드 유효 시간 (3분)
    private final StringRedisTemplate redisTemplate;

    // RedisTemplate 주입
    public VerificationCodeManager(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 인증 코드 생성 및 Redis에 저장
     * @param key - 사용자 식별값 (예: 이메일 주소)
     * @return 생성된 6자리 인증 코드
     */
    public String generateAndStoreCode(String key) {
        String code = String.valueOf((int)(Math.random() * 900000) + 100000); // 6자리 난수 생성
        // Redis에 key=코드 저장, TTL=3분
        redisTemplate.opsForValue().set(key, code, EXPIRATION_TIME_SECONDS, TimeUnit.SECONDS);
        return code;
    }

    /**
     * Redis에서 인증 코드 검증
     * @param key - 사용자 식별값 (예: 이메일 주소)
     * @param code - 사용자가 입력한 인증 코드
     * @throws CustomException - 코드가 없거나 일치하지 않을 때 발생
     */
    public void verifyCode(String key, String code) {
        String storedCode = redisTemplate.opsForValue().get(key); // Redis에서 코드 조회
        if (storedCode == null || !storedCode.equals(code)) {
            throw new CustomException(ErrorCode.VERIFICATION_CODE_NOT_VALID);
        }
        // 검증 성공 시 코드 삭제 (재사용 방지)
        redisTemplate.delete(key);
    }
}
