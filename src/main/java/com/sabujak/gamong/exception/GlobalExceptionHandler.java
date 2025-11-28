package com.sabujak.gamong.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // User
    @ExceptionHandler(AlreadyUserIdException.class)
    public ResponseEntity<String> AlreadyUserId(AlreadyUserIdException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 있는 UserId");
    }

    // Jwt
    @ExceptionHandler(HandleJwtException.class)
    public ResponseEntity<String> handleJwt(HandleJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(S3PresignedException.class)
    public ResponseEntity<String> s3Presigned(S3PresignedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotAllowedFileTypeException.class)
    public ResponseEntity<String> notAllowedFileType(NotAllowedFileTypeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("허용되지 않은 MIME 타입입니다.");
    }
}
