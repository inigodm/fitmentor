package com.inigo.shared.domain.events


import java.util.UUID

class CoachCreated(
    name: String = "coach.created",
    coachId: UUID,
    userId: UUID
) : CoachUpdated(name, coachId, userId)