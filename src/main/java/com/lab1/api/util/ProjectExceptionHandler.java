package com.lab1.api.util;

import com.lab1.exceptions.UnauthorizedUserException;
import com.lab1.exceptions.entity.EntityAbsenceException;
import com.lab1.exceptions.entity.EntityInaccessibleDeleteException;
import com.lab1.exceptions.entity.EntityUpdateException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProjectExceptionHandler {

    @ExceptionHandler({
            EntityAbsenceException.class
    })
    public ResponseEntity<ExceptionResponseDTO> handleEntityAbsenceException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponseDTO(e.getMessage()));
    }


    @ExceptionHandler({
            EntityInaccessibleDeleteException.class,
            EntityUpdateException.class
    })
    public ResponseEntity<ExceptionResponseDTO> handleEntityAccessException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponseDTO(e.getMessage()));
    }

    @ExceptionHandler({
            UnauthorizedUserException.class
    })
    public ResponseEntity<ExceptionResponseDTO> handleUnauthorizedException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponseDTO(e.getMessage()));
    }

    @ExceptionHandler({
            ConstraintViolationException.class
    })
    public ResponseEntity<ExceptionResponseDTO> handleConstraintViolationException(ConstraintViolationException e) {
        // Извлекаем первое сообщение об ошибке
        String message = e.getConstraintViolations().stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("Validation error");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponseDTO(message));
    }
}
