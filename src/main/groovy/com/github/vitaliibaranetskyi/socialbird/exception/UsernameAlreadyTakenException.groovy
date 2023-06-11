package com.github.vitaliibaranetskyi.socialbird.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class UsernameAlreadyTakenException extends RuntimeException {

    UsernameAlreadyTakenException(String message) {
        super(message)
    }
}
