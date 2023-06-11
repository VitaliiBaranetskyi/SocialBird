package com.github.vitaliibaranetskyi.socialbird.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                ""
        )

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(DuplicateFollowException.class)
    ResponseEntity<ErrorResponse> handleDuplicateFollowException(DuplicateFollowException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                ""
        )

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalCommentContent.class)
    ResponseEntity<ErrorResponse> handleIllegalCommentContent(IllegalCommentContent ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                ""
        )

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalPostContent.class)
    ResponseEntity<ErrorResponse> handleIllegalPostContent(IllegalPostContent ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                ""
        )

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AlreadyLikedException.class)
    ResponseEntity<ErrorResponse> handleAlreadyLikedException(AlreadyLikedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                ""
        )

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    ResponseEntity<ErrorResponse> handleUsernameAlreadyTakenException(UsernameAlreadyTakenException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                ""
        )

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(EmptyPasswordException.class)
    ResponseEntity<ErrorResponse> handleEmptyPasswordException(EmptyPasswordException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                ""
        )

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(EmptyUsernameException.class)
    ResponseEntity<ErrorResponse> handleEmptyUsernameException(EmptyUsernameException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                ""
        )

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST)
    }
}


