package dmu.dasom.api.domain.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customException(final CustomException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(new ErrorResponse(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ErrorCode.ARGUMENT_NOT_VALID));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationException(final ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ErrorCode.ARGUMENT_NOT_VALID));
    }

}
