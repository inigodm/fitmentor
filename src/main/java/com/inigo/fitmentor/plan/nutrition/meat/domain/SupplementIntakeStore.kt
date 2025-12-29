package com.inigo.fitmentor.plan.nutrition.meat.domain

interface SupplementIntakeStore {
    fun save(supplementIntake: SupplementIntake)
}