package com.inigo.fitmentor.coach.infrastructure

import com.inigo.arch.user.infrastucture.jpa.UserJpa
import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.fitmentor.shared.domain.UserId
import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import java.util.UUID

/**
 * A Coach.
 */
@Entity
@DynamicUpdate
@Table(name = "coaches")
class CoachJpa : Serializable {
    // jhipster-needle-entity-add-field - JHipster will add fields here
    @Id
    @Column(name = "id", nullable = false)
    lateinit var id: UUID

    @Column(name = "photo")
    var photo: String? = null

    @Column(name = "presentation")
    var presentation: String? = null

    @Column(name = "phone_number")
    var phonenumber: String? = null

    @OneToOne
    @JoinColumn(name = "user_id")
    lateinit var user: UserJpa

  fun toDomain(): Coach {
    return Coach(
      id = CoachId(id),
      photo = photo,
      presentation = presentation,
      phonenumber = phonenumber,
      user = UserId(user.id),
      email = user.email,
      username = user.username,
    )
  }

  companion object {
    @JvmStatic
    fun fromDomain(coach: Coach) = CoachJpa().apply {
      id = coach.id.value
      photo = coach.photo
      presentation = coach.presentation
      phonenumber = coach.phonenumber
      user = UserJpa().apply { id = coach.user!!.value }
    }
  }
}
