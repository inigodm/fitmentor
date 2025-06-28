package com.inigo.arch.user.infrastucture

import com.inigo.arch.user.application.AddTypeToUser
import com.inigo.shared.domain.events.ClientCreated
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class AddTypeOnClientCreated(val addTypeToUser: AddTypeToUser) {
    @EventListener
    fun handle(clientCreated: ClientCreated) {
        addTypeToUser.execute(
            clientCreated.userId,
            "client"
        )
    }
}