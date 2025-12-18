package com.inigo.fitmentor.coach.infrastructure

import com.inigo.fitmentor.coach.application.CreateCoach
import com.inigo.fitmentor.coach.application.FindCoach
import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.fitmentor.shared.domain.UserId
import com.inigo.fitmentor.shared.infrastructure.UserService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.apache.kafka.common.security.scram.internals.ScramFormatter.username
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springdoc.core.providers.SpringWebProvider
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
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
    val createCoach: CreateCoach,
    private val userService: UserService,
    private val springWebProvider: SpringWebProvider
) {

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
                user = coach.user!!.value,
                email = coach.email,
                username = coach.username
            ))
        }
    }

    @PostMapping
    fun modifyCoach(@RequestBody request: CoachCreationRequestBody): ResponseEntity<*> {
        println("---------------------------" + request)
        val dom = toDomain(
        CoachModificationRequest(
            id = UUID.fromString(request.id),
            phonenumber = request.phonenumber,
            presentation = request.presentation,
            photo = request.photo,
            user = UUID.fromString(request.user),
            email = request.email,
            username = request.username
        )
        )
        println("============================================ despues ")
        createCoach.execute(
            dom
        )
        println("++++++++++++++++++++++++++++++++++++++  fin")
        return ResponseEntity.ok("")
    }

    fun toDomain(clientReq: CoachModificationRequest): Coach {
        return with(clientReq) {
            Coach(
                id = CoachId(id),
                phonenumber = phonenumber,
                presentation = presentation,
                photo = photo,
                user = UserId(user),
                email = email,
                username = username
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
        @field:NotNull(message = "name must not be null")var username: String,
        @field:NotNull(message = "email must not be null")var email: String,
        @field:NotNull(message = "user must not be null") var user: UUID
    ) : Serializable

    data class CoachResponse(
        var id: UUID,
        var phonenumber: String? = null,
        var presentation: String? = null,
        var photo: String? = null,
        var user: UUID,
        var username: String,
        var email: String
    )

    data class CoachCreationRequestBody(
        @field:NotNull(message = "id must not be null") var id: String,
        var phonenumber: String?,
        var presentation: String?,
        @field:NotNull(message = "userId  must not be null") var user: String,
        var photo: String?,
        var username: String,
        var email: String
    )
}
