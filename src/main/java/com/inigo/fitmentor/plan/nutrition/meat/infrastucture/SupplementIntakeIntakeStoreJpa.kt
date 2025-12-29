package com.inigo.fitmentor.plan.nutrition.meat.infrastucture

import com.inigo.fitmentor.plan.nutrition.meat.domain.SupplementIntakeStore
import com.inigo.fitmentor.plan.nutrition.meat.domain.SupplementIntake
import org.springframework.stereotype.Component

@Component
class SupplementIntakeIntakeStoreJpa(val repository: SupplementIntakeJpaRepository): SupplementIntakeStore {
    override fun save(supplementIntake: SupplementIntake) {
        repository.save(SupplementIntakeJpa.from(supplementIntake))
    }
}