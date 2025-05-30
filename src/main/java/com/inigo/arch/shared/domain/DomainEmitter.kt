package com.inigo.arch.shared.domain

interface DomainEmitter {
    fun <T : DomainEvent?> emit(event: T?)
}