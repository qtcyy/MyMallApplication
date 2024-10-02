package org.example.mymallapplication.dal.config;

import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author chengyiyang
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler
    public SaResult handleException(Exception e) {
        log.error(e.getLocalizedMessage());
        return SaResult.error(e.getMessage());
    }
}
