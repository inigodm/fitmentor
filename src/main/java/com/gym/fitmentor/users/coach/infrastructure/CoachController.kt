package com.gym.fitmentor.users.coach.infrastructure

import com.gym.fitmentor.users.coach.application.FindCoach
import com.gym.fitmentor.users.coach.application.CreateCoach
import com.gym.fitmentor.users.coach.application.UpdateCoach
import com.gym.fitmentor.users.coach.domain.Coach
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import java.util.*

/**
 * REST controller for managing [CoachJPA].
 */
@RestController
@RequestMapping("/api/coaches")
class CoachController(val findCoach: FindCoach, val saveCoach: CreateCoach, val updateCoach: UpdateCoach) {
    /**
     * `POST  /coaches` : Create a new coach.
     *
     * @param coach the coachDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new coachDTO, or with status `400 (Bad Request)` if the coach has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Throws(URISyntaxException::class)
    fun createCoach(@RequestBody coach: Coach): ResponseEntity.BodyBuilder {
        LOG.debug("REST request to save Coach : {}", coach)
        saveCoach.execute(coach)
        return ResponseEntity.created(URI("/api/coaches/" + coach.id))
    }

    /**
     * `PUT  /coaches/:id` : Updates an existing coach.
     *
     * @param id the id of the coachDTO to save.
     * @param coach the coachDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated coachDTO,
     * or with status `400 (Bad Request)` if the coachDTO is not valid,
     * or with status `500 (Internal Server Error)` if the coachDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Throws(URISyntaxException::class)
    fun updateCoach(
        @PathVariable(value = "id", required = false) id: UUID,
        @RequestBody coach: Coach
    ): ResponseEntity.HeadersBuilder<*> {
        LOG.debug("REST request to update Coach : {}, {}", id, coach)
        updateCoach.execute(coach)
        return ResponseEntity.noContent()
    }

    /**
     * `GET  /coaches/:id` : get the "id" coach.
     *
     * @param id the id of the coachDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the coachDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/{id}")
    fun getCoach(@PathVariable("id") id: UUID): ResponseEntity<Coach?>? {
        LOG.debug("REST request to get Coach : {}", id)
        val coach: Coach? = findCoach.execute(id)
        return ResponseUtil.wrapOrNotFound<Coach?>(Optional.ofNullable(coach))
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(CoachController::class.java)
    }
}
