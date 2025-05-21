package com.inigo.arch.user.domain

import java.util.UUID

class User (val id: UUID, val username: Username, val email: Email, val password: Password, val role: Role)