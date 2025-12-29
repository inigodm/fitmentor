package com.inigo.arch.user.infrastucture

import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.converter.AttestedCredentialDataConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WebAuthnConfig {

    @Bean
    fun objectConverter(): ObjectConverter {
        return ObjectConverter()
    }

    @Bean
    fun attestedCredentialDataConverter(objectConverter: ObjectConverter): AttestedCredentialDataConverter {
        return AttestedCredentialDataConverter(objectConverter)
    }
}