package org.andulir.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(AndulirSystemException.class)
    public void systemExceptionHandler(AndulirSystemException systemException) {
        log.error("Andulir出现异常！ {}",systemException.toString());
    }

    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e) {
        log.error("Andulir出现异常！ {}",e.toString());
    }
}
