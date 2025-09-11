package com.inigo.fitmentor

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.databind.ObjectMapper
import com.inigo.arch.ArchApplication
import com.inigo.arch.UserUtils
import com.inigo.arch.user.domain.Role
import com.inigo.fitmentor.coach.infrastructure.CoachJpa
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import java.util.UUID

@SpringBootTest(classes = [ArchApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class CoachTest
{

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var entityManager: EntityManager

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
        val planId1 = "123e4567-e89b-12d3-a456-426614174001"
        val planId2 = "123e4567-e89b-12d3-a456-426614174002"

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
            id = planId1,
            client = clientId,
            coach = coachId,
            token = tokenCo)

        PlanUtils.generatePlan(
            mockMvc = mockMvc,
            id = planId2,
            client = clientId,
            coach = coachId,
            token = tokenCo)

        val plans = PlanUtils.getPlans(mockMvc, tokenCo, clientId)

        assertNotNull(plans)
        val planMap = plans!!.get(0)
        assertThat(planMap["id"]).isEqualTo(planId1)
        assertThat(planMap["client"]).isEqualTo(clientId)
        assertThat(planMap["coach"]).isEqualTo(coachId)
        assertThat(planMap["description"]).isEqualTo("Plan de entrenamiento personalizado")

        val plan = PlanUtils.getPlan(mockMvc, tokenCo, clientId, planId1)

        assertNotNull(plan)
        assertThat(plan!!["id"]).isEqualTo(planId1)
        assertThat(plan["client"]).isEqualTo(clientId)
        assertThat(plan["coach"]).isEqualTo(coachId)
        assertThat(plan["description"]).isEqualTo("Plan de entrenamiento personalizado")
    }
}