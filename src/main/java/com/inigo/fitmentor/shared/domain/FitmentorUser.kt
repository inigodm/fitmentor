package com.inigo.fitmentor.shared.domain

import com.inigo.arch.shared.domain.AggregateRoot
import com.inigo.arch.user.domain.Role
import java.util.UUID

open class FitmentorUser(
    val phonenumber: String? = null,
    val email: String,
    val username: String,
    val role: Role,
    var user: UserId?,
    aggregateName: String,
) : AggregateRoot(aggregateName = aggregateName, uuid = UUID.randomUUID())