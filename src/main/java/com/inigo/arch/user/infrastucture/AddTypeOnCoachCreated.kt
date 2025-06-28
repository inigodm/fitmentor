package com.inigo.arch.user.infrastucture

import com.inigo.arch.user.application.AddTypeToUser
import com.inigo.shared.domain.events.ClientCreated
import com.inigo.shared.domain.events.CoachCreated
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class AddTypeOnCoachCreated(val addTypeToUser: AddTypeToUser) {
    @EventListener
    fun handle(coachCreated: CoachCreated) {
        addTypeToUser.execute(
            coachCreated.userId,
            "coach"
        )
    }
}