package com.inigo.arch.user.infrastucture.spring

class UnauthorizedError(message: String, e: Exception? = null) : RuntimeException(message, e) {
    companion object {
        fun becauseUserOrPasswordNotFonud(username: String): UnauthorizedError {
            return UnauthorizedError("User with username $username or password not found")
        }
    }
}