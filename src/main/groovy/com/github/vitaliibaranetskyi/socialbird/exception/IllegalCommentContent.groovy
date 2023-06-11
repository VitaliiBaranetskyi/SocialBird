package com.github.vitaliibaranetskyi.socialbird.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class IllegalCommentContent extends RuntimeException {
    IllegalCommentContent(String message) {
        super(message)
    }
}
