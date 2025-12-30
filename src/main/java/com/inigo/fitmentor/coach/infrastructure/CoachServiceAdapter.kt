package com.inigo.fitmentor.coach.infrastructure

import com.inigo.arch.user.infrastucture.jpa.UserJpaRepository
import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.fitmentor.coach.domain.CoachService
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.arch.shared.domain.errors.NotFoundError
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
    private val userJpaRepository: UserJpaRepository,
    private val nutritionistRepository: NutritionistRepository,
    private val fitnessRepository: FitnessRepository
    ) : CoachService {
    override fun save(coach: Coach): Coach {
        LOG.debug("Request to save Coach : {}", coach)
        val savedCoach = coachRepository.save(CoachJpa.fromDomain(coach)).toDomain()
        
        if (coach.isNutritionist) {
            nutritionistRepository.save(NutritionistJpa.fromDomain( savedCoach.user!!.value))
        }
        
        if (coach.isFitness) {
            fitnessRepository.save(FitnessJpa.fromDomain( savedCoach.user!!.value))
        }
        
        return savedCoach
    }

    override fun findAll(): List<Coach> {
        LOG.debug("Request to get all Coachs")
        return coachRepository.findAll().stream()
            .map { obj: CoachJpa -> obj.toDomain() }
            .collect(Collectors.toList())
    }

    override fun findByCoachId(id: CoachId): Coach? {
        LOG.debug("Request to get Coach : {}", id.value)
        return coachRepository.findById(id.value)
            .map(Function { obj: CoachJpa -> obj.toDomain() })
            .orElseThrow {NotFoundError.becauseNoCoachExistForGivenId( id.value.toString()) }
    }

    fun delete(id: UUID) {
        LOG.debug("Request to delete Coach : {}", id)
        coachRepository.deleteById(id)
    }

    override fun existsUser(coach: Coach) : Boolean {
        LOG.debug("Request to check existence of Coach : {}  as user : {}", coach.id.value, coach.user!!.value)
        return userJpaRepository.findById(coach.user!!.value).isPresent
    }

    override fun existsCoach(coach: Coach): Boolean {
        LOG.debug("Request to check existence of Coach : {}", coach.id.value)
        return coachRepository.findById(coach.user!!.value).isPresent
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(CoachServiceAdapter::class.java)
    }
}
