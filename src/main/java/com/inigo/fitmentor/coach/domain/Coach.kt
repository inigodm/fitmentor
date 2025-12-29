package com.inigo.fitmentor.coach.domain

import com.inigo.arch.user.domain.Role.COACH
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.fitmentor.shared.domain.FitmentorUser
import com.inigo.fitmentor.shared.domain.UserId
import com.inigo.fitmentor.shared.domain.events.CoachCreated
import com.inigo.fitmentor.shared.domain.events.CoachUpdated
import com.inigo.fitmentor.shared.infrastructure.UserService

class Coach(
  val id: CoachId,
  val photo: String? = null,
  val presentation: String? = null,
  user: UserId,
  phonenumber: String? = null,
  email: String,
  username: String,
) : FitmentorUser(aggregateName = "snapshots.client",
    phonenumber = phonenumber,
    email = email,
    username = username,
    user = user,
    role = COACH) {

  fun ensureUserExists(store: CoachService, userService: UserService): Coach {
    if (!store.existsUser(this)) {
        userService.createUser(this)
    }
    return this
  }

  fun save(store: CoachService): Coach {
    this.user = store.save(this).user
    record(CoachUpdated(coachId = this.id.value, userId = this.user!!.value))
    return this
  }

  fun create(store: CoachService): Coach {
    this.user = store.save(this).user
    record(CoachCreated(coachId = this.id.value, userId = this.user!!.value))
    return this
  }

  fun alreadyExists(store: CoachService): Boolean {
    return store.existsCoach(this)
  }
}