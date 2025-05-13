package com.gym.fitmentor.users.coach.infrastructure

import com.gym.fitmentor.users.coach.domain.Coach
import com.gym.fitmentor.users.coach.domain.CoachService
import com.gym.fitmentor.users.coach.infrastructure.CoachJPA.Companion.fromDomain
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.Function

/**
 * Service Implementation for managing [CoachJPA].
 */
@Service
@Transactional
open class CoachServiceAdapter(private val coachRepository: CoachRepository) : CoachService {
    override fun save(coach: Coach) {
        LOG.debug("Request to save Coach : {}", coach)
        coachRepository.save<CoachJPA?>(fromDomain(coach))
    }

    @Transactional(readOnly = true)
    override fun findAll(): MutableList<Coach?> {
        LOG.debug("Request to get all Coaches")
        return coachRepository.findAll().stream().map<Coach?> { obj: CoachJPA? -> obj!!.toDomain() }.toList()
    }

    @Transactional(readOnly = true)
    override fun findOne(id: UUID): Optional<Coach?> {
        LOG.debug("Request to get Coach : {}", id)
        return coachRepository.findById(id)
            .map<Coach?>(Function { obj: CoachJPA? -> obj!!.toDomain() })
    }

    override fun delete(id: UUID) {
        LOG.debug("Request to delete Coach : {}", id)
        coachRepository.deleteById(id)
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(CoachServiceAdapter::class.java)
    }
}
