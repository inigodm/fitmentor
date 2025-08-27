package com.inigo.fitmentor

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.databind.ObjectMapper
import com.inigo.arch.ArchApplication
import com.inigo.arch.UserUtils
import com.inigo.arch.user.domain.Role
import com.inigo.fitmentor.client.infrastructure.ClientJpa
import com.inigo.fitmentor.coach.infrastructure.CoachJpa
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import java.util.UUID

@SpringBootTest(classes = [ArchApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class ClientTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    fun `client creation should be idempotent and answer with a 200`() {
        val userId =  "00000000-0000-0000-0000-000000000042"
        val clientId = "00000000-0000-0000-0000-000000000023"

        UserUtils.generateUser(mockMvc,
            uuid = userId,
            name = "client1",
            email = "client1@email.com",
            role = Role.USER.name)

        val token = UserUtils.obtainToken(mockMvc, name = "client1")

        ClientUtils.generateClient(mockMvc,
            user = userId,
            id = clientId,
            coach = "00000000-0000-0000-0000-000000000005",
            token = token)

        ClientUtils.generateClient(mockMvc,
            user = userId,
            id = clientId,
            coach = "00000000-0000-0000-0000-000000000005",
            token = token)

        val savedClient = entityManager
            .createQuery("SELECT u FROM ClientJpa u WHERE u.id = :id", ClientJpa::class.java)
            .setParameter("id", UUID.fromString(clientId))
            .singleResult

        val client = ClientUtils.getClient(mockMvc, token, clientId)

        assertNotNull(savedClient)
        assert(savedClient.id.toString() == clientId)
        assert(savedClient.user.toString() == userId)
        assert(savedClient.goals == "Perder peso y ganar músculo")
        assert(savedClient.age is Int)
        assert(savedClient.injuries == "Mofollon")
        assert(savedClient.weight is Int)
        assert(savedClient.equipmentAccess == 1)
        assert(savedClient.phonenumber == "123456789")

        assertNotNull(client)
        assert(client?.get("id") == clientId)
        assert(client?.get("user") == userId)
        assert(client?.get("goals") == "Perder peso y ganar músculo")
        assert(client?.get("age") is Int)
        assert(client?.get("injuries") == "Mofollon")
        assert(client?.get("weight") is Int)
        assert(client?.get("equipmentAccess") == 1)
        assert(client?.get("phonenumber") == "123456789")

    }

    @Test
    fun `should return a 404 if client or coach does not exist when doing a get`() {
        val userId =  "00000000-0000-0000-0000-000000000042"

        UserUtils.generateUser(mockMvc,
            uuid = userId,
            name = "client1",
            email = "client1@email.com",
            role = Role.USER.name)

        val token = UserUtils.obtainToken(mockMvc, name = "client1")

        ClientUtils.getClientError(mockMvc, token, 404, "00000000-0000-0000-0000-000000000999")
        CoachUtils.getCoachError(mockMvc, token, 404, "00000000-0000-0000-0000-000000000999")

    }
}