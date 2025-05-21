package com.inigo.arch

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.net.InetAddress
import java.net.UnknownHostException

@SpringBootApplication
@EnableJpaRepositories("com.inigo")
object ArchApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        val environment = SpringApplication.run(ArchApplication::class.java, *args).getEnvironment()
        logApplicationStartup(environment)
    }

    private fun logApplicationStartup(env: ConfigurableEnvironment) {
        val protocol = env.getProperty("server.ssl.key-store") ?: "http"
        val applicationName = env.getProperty("spring.application.name")
        val serverPort = env.getProperty("server.port")
        val contextPath = env.getProperty("server.servlet.context-path") ?: "/"
        var hostAddress: String? = "localhost"
        try {
            hostAddress = InetAddress.getLocalHost().hostAddress
        } catch (e: UnknownHostException) {
            println("The host name could not be determined, using `localhost` as fallback")
        }
        println(
            """
                ----------------------------------------------------------
                ${'\t'}Application '$applicationName' is running! Access URLs:
                ${'\t'}Local: ${'\t'}${'\t'}$protocol://localhost:$serverPort$contextPath
                ${'\t'}External: ${'\t'}$protocol://$hostAddress:$serverPort$contextPath
                ${'\t'}Profile(s): ${'\t'}${env.activeProfiles.joinToString(", ")}
                ----------------------------------------------------------
            """.trimIndent())
    }
}
