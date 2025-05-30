package com.inigo.arch.domainevents

import com.inigo.arch.shared.domain.DomainEmitter


object SharedEventEmitter : DomainEmitter by SpringDomainEventEmitter()