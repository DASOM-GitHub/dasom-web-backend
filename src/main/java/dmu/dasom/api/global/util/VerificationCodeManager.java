package dmu.dasom.api.global.util;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class VerificationCodeManager {

    private static final long EXPIRATION_TIME_SECONDS = 180; // 3분
    private final ConcurrentHashMap<String, String> codeStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Instant> expirationStore = new ConcurrentHashMap<>();

    public String generateAndStoreCode(String key) {
        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        codeStore.put(key, code);
        expirationStore.put(key, Instant.now().plusSeconds(EXPIRATION_TIME_SECONDS));
        return code;
    }

    public void verifyCode(String key, String code) {
        if (!isCodeValid(key, code)) {
            throw new CustomException(ErrorCode.VERIFICATION_CODE_NOT_VALID);
        }
        removeCode(key);
    }

    private boolean isCodeValid(String key, String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }
        Instant expirationTime = expirationStore.get(key);
        if (expirationTime == null || Instant.now().isAfter(expirationTime)) {
            removeCode(key);
            return false;
        }
        return code.equals(codeStore.get(key));
    }

    private void removeCode(String key) {
        codeStore.remove(key);
        expirationStore.remove(key);
    }
}
