package com.inigo.arch.user.infrastucture.handlers

import com.inigo.arch.user.application.AddTypeToUser
import com.inigo.fitmentor.shared.domain.events.CoachCreated
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