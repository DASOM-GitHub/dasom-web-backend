package dmu.dasom.api.domain.common.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<ErrorResponse> handleError(final HttpServletRequest request) {
        // 에러 코드 추출
        final Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        final HttpStatus status = statusCode != null ? HttpStatus.valueOf(statusCode) : HttpStatus.INTERNAL_SERVER_ERROR;

        // 404 에러
        if (status == HttpStatus.NOT_FOUND)
            return ResponseEntity.status(status).body(new ErrorResponse(ErrorCode.NOT_FOUND));

        // 기타 에러
        return ResponseEntity.status(status).body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
    }

}
