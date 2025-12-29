package com.inigo.arch

import assertk.assertThat
import assertk.assertions.startsWith
import com.fasterxml.jackson.databind.ObjectMapper
import com.inigo.arch.user.infrastucture.jpa.UserJpa
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import java.util.UUID

@SpringBootTest(classes = [ArchApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
internal class ArchUserTests {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    fun `create user and verify in database`() {
        // Body de la solicitud
        UserUtils.generateUser(mockMvc,
            role = "USER")

        val savedUser = entityManager
            .createQuery("SELECT u FROM UserJpa u WHERE u.id = :id", UserJpa::class.java)
            .setParameter("id", UUID.fromString("00000000-0000-0000-0000-000000000004"))
            .singleResult

        assertNotNull(savedUser)
        assert(savedUser.username == "iñigo")
        assert(savedUser.email == "user@test.com")
        assert(savedUser.role == "USER")
    }

    @Test
    fun `should get a token`() {
        println("Port: $port")
        val token = UserUtils.obtainToken(mockMvc)
        println("Token: $token")
        assertThat(token).startsWith("""Bearer """)
    }
}
