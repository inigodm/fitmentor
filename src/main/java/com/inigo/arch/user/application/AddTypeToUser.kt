package com.inigo.arch.user.application

import com.inigo.arch.user.domain.UserStore
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AddTypeToUser(val userStore: UserStore) {
    fun execute(userId: UUID, type: String) {
        userStore.updateUserType(
            userId = userId,
            type = type
        )
    }
}