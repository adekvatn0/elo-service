package io.adekvatn0.eloservice

import io.adekvatn0.eloservice.league.repo.LeagueRepository
import io.adekvatn0.eloservice.player.repo.PlayerRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.annotation.DirtiesContext
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class MongoRepositoryContainerTest {

    @Autowired
    lateinit var playerRepository: PlayerRepository

    @Autowired
    lateinit var leagueRepository: LeagueRepository

    @BeforeEach
    fun clean() {
        playerRepository.deleteAll()
        leagueRepository.deleteAll()
    }

    companion object {
        @Container
        @ServiceConnection
        val mongo = MongoDBContainer(DockerImageName.parse("mongo:7.0"))
    }
}