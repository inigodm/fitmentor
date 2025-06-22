package com.inigo.fitmentor.coach.infrastructure

import com.inigo.fitmentor.coach.application.FindCoach
import com.inigo.fitmentor.coach.application.UpdateCoach
import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.shared.domain.CoachId
import com.inigo.shared.domain.UserId
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable
import java.util.*

/**
 * REST controller for managing [CoachJpa].
 */
@RestController
@RequestMapping("/api/user/coaches")
@Validated
class CoachController(
    val findCoach: FindCoach,
    val updateCoach: UpdateCoach) {

    /**
     * `GET  /coachs/:id` : get the "id" client.
     *
     * @param id the id of the coachDto to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the clientDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/{id}")
    fun getClient(@NotNull @PathVariable("id") id: UUID): ResponseEntity<CoachResponse> {
        LOG.debug("REST request to get Coach : {}", id)
        val coach: Coach? = findCoach.execute(CoachId(id))
        return if (coach == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(CoachResponse(
                id = coach.id.value,
                phonenumber = coach.phonenumber,
                presentation = coach.presentation,
                photo = coach.photo,
                user = coach.user.value
            ))
        }
    }

    @PutMapping()
    fun modifyCoach(@Valid @RequestBody coach: CoachModificationRequest): ResponseEntity<*> {
        LOG.debug("REST request to update Coach : {}", coach.id)

        val updatedCoach = updateCoach.execute(toDomain(coach))
        return ResponseEntity.ok("")
    }

    fun toDomain(clientReq: CoachModificationRequest): Coach {
        return with(clientReq) {
            Coach(
                id = CoachId(id),
                phonenumber = phonenumber,
                presentation = presentation,
                photo = photo,
                user = UserId(user)
            )
        }
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(CoachController::class.java)
    }

    data class CoachModificationRequest(
        @field:NotNull(message = "id must not be null") var id: UUID,
        var phonenumber: String? = null,
        var presentation: String? = null,
        var photo: String? = null,
        @field:NotNull(message = "user must not be null") var user: UUID
    ) : Serializable

    data class CoachResponse(
        var id: UUID,
        var phonenumber: String? = null,
        var presentation: String? = null,
        var photo: String? = null,
        var user: UUID
    )
}
