package com.sabujak.gamong.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // User
    @ExceptionHandler(AlreadyLoginIdException.class)
    public ResponseEntity<String> alreadyLoginId(AlreadyLoginIdException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 있는 LoginId");
    }

    @ExceptionHandler(InvalidLoginIdException.class)
    public ResponseEntity<String> invalidLoginId(InvalidLoginIdException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("일치하지 않는 LoginId");
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> invalidPassword(InvalidPasswordException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("일치하지 않는 password");
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<String> invalidUser(InvalidUserException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없는 유저입니다.");
    }

    // ItemTrade
    @ExceptionHandler(InvalidItemTradeIdException.class)
    public ResponseEntity<String> invalidItemTradeId(InvalidItemTradeIdException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("없는 ItemTradeId");
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
