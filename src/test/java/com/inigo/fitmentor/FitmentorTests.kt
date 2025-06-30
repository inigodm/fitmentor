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
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import java.util.UUID

@SpringBootTest(classes = [ArchApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FitmentorTests {

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

    @Test
    fun `coaches creation should be idempotent and answer with a 200`() {
        val userId =  "00000000-0000-0000-0000-000000000044"
        val coachId = "00000000-0000-0000-0000-000000000023"

        UserUtils.generateUser(mockMvc,
            uuid = userId,
            name = "client2",
            email = "client2@email.com",
            role = Role.USER.name)

        val token = UserUtils.obtainToken(mockMvc, name = "client2")

        CoachUtils.generateCoach(mockMvc,
            user = userId,
            photo = "https://example.com/photo.jpg",
            id = coachId,
            token = token)

        CoachUtils.generateCoach(mockMvc,
            user = userId,
            photo = "https://example.com/photo.jpg",
            id = coachId,
            token = token)

        val savedCoach = entityManager
            .createQuery("SELECT u FROM CoachJpa u WHERE u.id = :id", CoachJpa::class.java)
            .setParameter("id", UUID.fromString(coachId))
            .singleResult

        val coach = CoachUtils.getCoach(mockMvc, token, coachId)

        assertNotNull(savedCoach)
        assert(savedCoach.id.toString() == coachId)
        assert(savedCoach.user.toString() == userId)
        assert(savedCoach.photo == "https://example.com/photo.jpg")
        assert(savedCoach.phonenumber == "123456789")

        assertNotNull(coach)
        assert(coach?.get("id") == coachId)
        assert(coach?.get("user") == userId)
        assert(coach?.get("photo") == "https://example.com/photo.jpg")
        assert(coach?.get("phonenumber") == "123456789")

    }

    @Test
    fun `should create plans`() {
        val userIdCo = "00000000-0000-0000-0000-000000000045"
        val userIdCl = "00000000-0000-0000-0000-000000000046"
        val clientId = "00000000-0000-0000-0000-000000000023"
        val coachId = "00000000-0000-0000-0000-000000000005"
        val planId = "123e4567-e89b-12d3-a456-426614174001"

        UserUtils.generateUser(mockMvc,
            uuid = userIdCl,
            name = "clientPl",
            email = "clientP@email.com",
            role = Role.USER.name)

        UserUtils.generateUser(mockMvc,
            uuid = userIdCo,
            name = "coachPl",
            email = "coach@email.com",
            role = Role.USER.name)


        val tokenCl = UserUtils.obtainToken(mockMvc, name = "clientPl")
        ClientUtils.generateClient(mockMvc,
            user = userIdCl,
            id = clientId,
            coach = "00000000-0000-0000-0000-000000000005",
            token = tokenCl)

        var tokenCo = UserUtils.obtainToken(mockMvc, name = "coachPl")
        CoachUtils.generateCoach(mockMvc,
            user = userIdCo,
            photo = "https://example.com/photo.jpg",
            id = coachId,
            token = tokenCo)

        tokenCo = UserUtils.obtainToken(mockMvc, name = "coachPl")

        PlanUtils.generatePlan(
            mockMvc = mockMvc,
            id = planId,
            client = clientId,
            coach = coachId,
            token = tokenCo)

        val plan = PlanUtils.getPlan(mockMvc, tokenCo, clientId)

        assertNotNull(plan)
        val planMap = plan!!.get(0) as Map<String, Any>
        assertThat(planMap.get("id")).isEqualTo(planId)
        assertThat(planMap.get("client")).isEqualTo(clientId)
        assertThat(planMap.get("coach")).isEqualTo(coachId)
        assertThat(planMap.get("description")).isEqualTo("Plan de entrenamiento personalizado")
    }
}