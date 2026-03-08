package com.omeralkan.parameter.exception;

import com.omeralkan.parameter.entity.ErrorMessageEntity;
import com.omeralkan.parameter.repository.ErrorMessageRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice // Spring Boot'a "Tüm Controller'ların koruması budur" diyoruz
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorMessageRepository errorMessageRepository;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {

        String language = request.getHeader("Accept-Language");

        if (language == null || language.isEmpty()) {
            language = "tr";
        } else if (language.length() >= 2) {
            language = language.substring(0, 2).toLowerCase();
        }

        String errorMessage = errorMessageRepository.findByErrorCodeAndLanguage(ex.getErrorCode(), language)
                .map(ErrorMessageEntity::getMessage)
                // Veritabanında o kodu bulamazsak patlamasın diye yedek mesajımız:
                .orElse("Bilinmeyen bir hata oluştu / Unknown error occurred");

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ex.getErrorCode(),
                errorMessage
        );

        HttpStatus status = ex.getErrorCode().contains("404") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(errorResponse);
    }
}