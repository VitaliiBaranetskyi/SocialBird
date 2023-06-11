package com.github.vitaliibaranetskyi.socialbird.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class AlreadyLikedException extends RuntimeException {

    AlreadyLikedException(String message) {
        super(message)
    }
}
