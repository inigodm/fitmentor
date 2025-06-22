package com.inigo.fitmentor.coach.infrastructure

import com.inigo.arch.user.infrastucture.jpa.UserJpaRepository
import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.fitmentor.coach.domain.CoachService
import com.inigo.shared.domain.CoachId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import java.util.function.Function
import java.util.stream.Collectors

/**
 * Service Implementation for managing [CoachJpa].
 */
@Service
@Transactional
open class CoachServiceAdapter(
    private val coachRepository: CoachRepository,
    private val userJpaRepository: UserJpaRepository
    ) : CoachService {
    override fun save(coach: Coach) {
        LOG.debug("Request to save Coach : {}", coach)
        coachRepository.save(CoachJpa.fromDomain(coach))
    }

    fun findAll(): List<Coach> {
        LOG.debug("Request to get all Coachs")
        return coachRepository.findAll().stream()
            .map { obj: CoachJpa -> obj.toDomain() }
            .collect(Collectors.toList())
    }

    override fun findByCoachId(id: CoachId): Coach? {
        LOG.debug("Request to get Coach : {}", id.value)
        return coachRepository.findById(id.value)
            .map(Function { obj: CoachJpa -> obj.toDomain() }).orElse(null)
    }

    fun delete(id: UUID) {
        LOG.debug("Request to delete Coach : {}", id)
        coachRepository.deleteById(id)
    }

    override fun existsUser(coach: Coach) : Boolean {
        LOG.debug("Request to check existence of Coach : {}  as user : {}", coach.id.value, coach.user.value)
        return userJpaRepository.findById(coach.user.value).isPresent
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(CoachServiceAdapter::class.java)
    }
}
