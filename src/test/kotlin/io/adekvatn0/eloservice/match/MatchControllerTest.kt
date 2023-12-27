package io.adekvatn0.eloservice.match

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.adekvatn0.eloservice.MongoRepositoryContainerTest
import io.adekvatn0.eloservice.dto.Result
import io.adekvatn0.eloservice.league.entity.League
import io.adekvatn0.eloservice.match.dto.MatchDto
import io.adekvatn0.eloservice.match.entity.Match
import io.adekvatn0.eloservice.match.entity.MatchResult
import io.adekvatn0.eloservice.player.entity.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@AutoConfigureMockMvc
class MatchControllerTest : MongoRepositoryContainerTest() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun createWinMatchThenSuccess() {
        val leagueName = "league"
        val player1name = "name1"
        val player2name = "name2"
        val expectedDelta = 12

        val request = MatchDto(
            leagueName,
            player1name,
            player2name,
            MatchResult.WIN
        )

        val p1 = playerRepository.save(Player(player1name, leagueName, 1200, 9, 2, 8))
        val p2 = playerRepository.save(Player(player2name, leagueName, 1400, 18, 5, 14))

        leagueRepository.save(League(leagueName, 1500, 16, League.Points(1.0, 0.5, 0.0)))

        //test
        val responseString = mockMvc.post("/leagues/$leagueName/matches") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn().response.contentAsString

        val response = objectMapper.readValue(responseString, object : TypeReference<Result<MatchDto?>?>() {})!!

        //verify response
        assertThat(response.status).isEqualTo(Result.Status.SUCCESS)
        val matchDto = response.entity as MatchDto
        assertThat(matchDto.league).isEqualTo(leagueName)
        assertThat(matchDto.player1).isEqualTo(player1name)
        assertThat(matchDto.player2).isEqualTo(player2name)
        assertThat(matchDto.result).isEqualTo(MatchResult.WIN)

        assertThat(matchDto.eloDelta).isEqualTo(expectedDelta)
        assertThat(matchDto.timestamp).isNotNull()

        //verify players
        val p1saved = playerRepository.findByLeagueAndName(leagueName, player1name).get()
        assertThat(p1saved.elo).isEqualTo(p1.elo + expectedDelta)
        assertThat(p1saved.wins).isEqualTo(p1.wins + 1)
        assertThat(p1saved.draws).isEqualTo(p1.draws)
        assertThat(p1saved.loses).isEqualTo(p1.loses)

        val p2saved = playerRepository.findByLeagueAndName(leagueName, player2name).get()
        assertThat(p2saved.elo).isEqualTo(p2.elo - expectedDelta)
        assertThat(p2saved.wins).isEqualTo(p2.wins)
        assertThat(p2saved.draws).isEqualTo(p2.draws)
        assertThat(p2saved.loses).isEqualTo(p2.loses + 1)
    }

    @Test
    fun createDrawMatchThenSuccess() {
        val leagueName = "league"
        val player1name = "name1"
        val player2name = "name2"
        val expectedDelta = 4

        val request = MatchDto(
            leagueName,
            player1name,
            player2name,
            MatchResult.DRAW
        )

        val p1 = playerRepository.save(Player(player1name, leagueName, 1200, 9, 2, 8))
        val p2 = playerRepository.save(Player(player2name, leagueName, 1400, 18, 5, 14))

        leagueRepository.save(League(leagueName, 1500, 16, League.Points(1.0, 0.5, 0.0)))

        //test
        val responseString = mockMvc.post("/leagues/$leagueName/matches") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn().response.contentAsString

        val response = objectMapper.readValue(responseString, object : TypeReference<Result<MatchDto?>?>() {})!!

        //verify response
        assertThat(response.status).isEqualTo(Result.Status.SUCCESS)
        val matchDto = response.entity as MatchDto
        assertThat(matchDto.league).isEqualTo(leagueName)
        assertThat(matchDto.player1).isEqualTo(player1name)
        assertThat(matchDto.player2).isEqualTo(player2name)
        assertThat(matchDto.result).isEqualTo(MatchResult.DRAW)
        assertThat(matchDto.eloDelta).isEqualTo(expectedDelta)
        assertThat(matchDto.timestamp).isNotNull()

        //verify players
        val p1saved = playerRepository.findByLeagueAndName(leagueName, player1name).get()
        assertThat(p1saved.elo).isEqualTo(p1.elo + expectedDelta)
        assertThat(p1saved.wins).isEqualTo(p1.wins)
        assertThat(p1saved.draws).isEqualTo(p1.draws + 1)
        assertThat(p1saved.loses).isEqualTo(p1.loses)

        val p2saved = playerRepository.findByLeagueAndName(leagueName, player2name).get()
        assertThat(p2saved.elo).isEqualTo(p2.elo - expectedDelta)
        assertThat(p2saved.wins).isEqualTo(p2.wins)
        assertThat(p2saved.draws).isEqualTo(p2.draws + 1)
        assertThat(p2saved.loses).isEqualTo(p2.loses)
    }

    @Test
    fun createLoseMatchThenSuccess() {
        val leagueName = "league"
        val player1name = "name1"
        val player2name = "name2"
        val expectedDelta = -4

        val request = MatchDto(
            leagueName,
            player1name,
            player2name,
            MatchResult.LOSE
        )

        val p1 = playerRepository.save(Player(player1name, leagueName, 1200, 9, 2, 8))
        val p2 = playerRepository.save(Player(player2name, leagueName, 1400, 18, 5, 14))

        leagueRepository.save(League(leagueName, 1500, 16, League.Points(1.0, 0.5, 0.0)))

        //test
        val responseString = mockMvc.post("/leagues/$leagueName/matches") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn().response.contentAsString

        val response = objectMapper.readValue(responseString, object : TypeReference<Result<MatchDto?>?>() {})!!

        //verify response
        assertThat(response.status).isEqualTo(Result.Status.SUCCESS)
        val matchDto = response.entity as MatchDto
        assertThat(matchDto.league).isEqualTo(leagueName)
        assertThat(matchDto.player1).isEqualTo(player1name)
        assertThat(matchDto.player2).isEqualTo(player2name)
        assertThat(matchDto.result).isEqualTo(MatchResult.LOSE)
        assertThat(matchDto.eloDelta).isEqualTo(expectedDelta)
        assertThat(matchDto.timestamp).isNotNull()

        //verify players
        val p1saved = playerRepository.findByLeagueAndName(leagueName, player1name).get()
        assertThat(p1saved.elo).isEqualTo(p1.elo + expectedDelta)
        assertThat(p1saved.wins).isEqualTo(p1.wins)
        assertThat(p1saved.draws).isEqualTo(p1.draws)
        assertThat(p1saved.loses).isEqualTo(p1.loses + 1)

        val p2saved = playerRepository.findByLeagueAndName(leagueName, player2name).get()
        assertThat(p2saved.elo).isEqualTo(p2.elo - expectedDelta)
        assertThat(p2saved.wins).isEqualTo(p2.wins + 1)
        assertThat(p2saved.draws).isEqualTo(p2.draws)
        assertThat(p2saved.loses).isEqualTo(p2.loses)
    }

    @Test
    fun createMatchAndLeagueNotExistsThenError() {
        val leagueName = "league"
        val player1name = "name1"
        val player2name = "name2"

        val request = MatchDto(
            leagueName,
            player1name,
            player2name,
            MatchResult.WIN
        )

        //test
        mockMvc.post("/leagues/$leagueName/matches") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(
                    objectMapper.writeValueAsString(
                        Result.error<MatchDto>("League $leagueName does not exists")
                    )
                )
            }
        }
    }

    @Test
    fun createMatchAndPlayer1NotExistsThenError() {
        val leagueName = "league"
        val player1name = "name1"
        val player2name = "name2"

        val request = MatchDto(
            leagueName,
            player1name,
            player2name,
            MatchResult.WIN
        )

        leagueRepository.save(League(leagueName, 1500, 16, League.Points(1.0, 0.5, 0.0)))

        //test
        mockMvc.post("/leagues/$leagueName/matches") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(
                    objectMapper.writeValueAsString(
                        Result.error<MatchDto>("Player $player1name does not exists")
                    )
                )
            }
        }
    }

    @Test
    fun createMatchAndPlayer2NotExistsThenError() {
        val leagueName = "league"
        val player1name = "name1"
        val player2name = "name2"

        val request = MatchDto(
            leagueName,
            player1name,
            player2name,
            MatchResult.WIN
        )

        playerRepository.save(Player(player1name, leagueName, 1200, 9, 2, 8))
        leagueRepository.save(League(leagueName, 1500, 16, League.Points(1.0, 0.5, 0.0)))

        //test
        mockMvc.post("/leagues/$leagueName/matches") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(
                    objectMapper.writeValueAsString(
                        Result.error<MatchDto>("Player $player2name does not exists")
                    )
                )
            }
        }
    }

    @Test
    fun findMatchesThenSuccessSortedDateDesc() {
        val leagueName = "league"
        val playerName = "player"

        matchRepository.saveAll(
            listOf(
                Match(leagueName, playerName, "player2", MatchResult.WIN, 10, 1),
                Match(leagueName, "player2", playerName, MatchResult.LOSE, 5, 2),
                Match(leagueName, playerName, "player2", MatchResult.DRAW, 9, 3),
                Match(leagueName, "player2", playerName, MatchResult.WIN, 3, 4),
                Match(leagueName, playerName, "player2", MatchResult.LOSE, -5, 5),
                Match("league2", playerName, "player2", MatchResult.LOSE, -5, 5), //wrong league
            )
        )

        mockMvc.get("/leagues/$leagueName/matches?name=$playerName")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(objectMapper.writeValueAsString(
                    Result.success(
                        listOf(
                            MatchDto(leagueName, playerName, "player2", MatchResult.LOSE, -5, 5),
                            MatchDto(leagueName, "player2", playerName, MatchResult.WIN, 3, 4),
                            MatchDto(leagueName, playerName, "player2", MatchResult.DRAW, 9, 3),
                            MatchDto(leagueName, "player2", playerName, MatchResult.LOSE, 5, 2),
                            MatchDto(leagueName, playerName, "player2", MatchResult.WIN, 10, 1),
                        )
                    )
                )) }
            }
    }
}