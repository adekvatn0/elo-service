package io.adekvatn0.eloservice.player

import io.adekvatn0.eloservice.dto.Result
import io.adekvatn0.eloservice.league.entity.League
import io.adekvatn0.eloservice.league.repo.LeagueRepository
import io.adekvatn0.eloservice.player.dto.PlayerDto
import io.adekvatn0.eloservice.player.dto.PlayerMapper
import io.adekvatn0.eloservice.player.entity.Player
import io.adekvatn0.eloservice.player.repo.PlayerRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.*

class PlayerServiceTest {

    @Test
    fun findPlayerThenSuccess() {
        val name = "player"
        val leagueName = "league"
        val elo = 500
        val player = Player(name, leagueName, elo)

        //mocks
        val leagueRepo = mock<LeagueRepository> {
        }
        val playerRepo = mock<PlayerRepository> {
            on { findByLeagueAndName(leagueName, name) } doReturn Optional.of(player)
        }
        val mapper = mock<PlayerMapper> {
            on { toDto(player) } doReturn PlayerDto(name, leagueName, elo)
        }

        //test
        val service = PlayerService(playerRepo, leagueRepo, mapper)
        val result = service.findPlayers(leagueName, name, 0, 10)

        assertEquals(Result.Status.SUCCESS, result.status)
        assertEquals(name, result.entity!![0].name)
        assertEquals(elo, result.entity!![0].elo)

        verify(playerRepo, times(1)).findByLeagueAndName(leagueName, name)
        verify(mapper, times(1)).toDto(player)

        verifyNoMoreInteractions(leagueRepo, playerRepo, mapper)
    }

    @Test
    fun createPlayerThenSuccess() {
        val name = "player"
        val leagueName = "league"
        val leagueInitialElo = 1500
        val player = Player(name, leagueName, leagueInitialElo)

        //mocks
        val leagueRepo = mock<LeagueRepository> {
            on { findByName(leagueName) } doReturn Optional.of(
                League(
                    leagueName,
                    leagueInitialElo,
                    16,
                    League.Points(1.0, 0.5, 0.0)
                )
            )
        }
        val playerRepo = mock<PlayerRepository> {
            on { findByLeagueAndName(leagueName, name) } doReturn Optional.empty()
            on { save(player) } doReturn player
        }
        val mapper = mock<PlayerMapper> {
            on { toDto(player) } doReturn PlayerDto(name, leagueName)
            on { toEntity(PlayerDto(name, leagueName)) } doReturn player
        }

        //test
        val service = PlayerService(playerRepo, leagueRepo, mapper)
        val result = service.createPlayer(PlayerDto(name, leagueName))

        assertEquals(Result.Status.SUCCESS, result.status)
        assertEquals(PlayerDto(name, leagueName), result.entity)
        assertEquals(null, result.details)

        verify(leagueRepo, times(1)).findByName(leagueName)
        verify(playerRepo, times(1)).findByLeagueAndName(leagueName, name)
        verify(playerRepo, times(1)).save(player)
        verify(mapper, times(1)).toDto(player)
        verify(mapper, times(1)).toEntity(PlayerDto(name, leagueName))

        verifyNoMoreInteractions(leagueRepo, playerRepo, mapper)
    }


    @Test
    fun createPlayerAndNameIsOccupiedThenError() {
        val name = "player"
        val leagueName = "league"
        val leagueInitialElo = 1500
        val player = Player(name, leagueName)

        //mocks
        val leagueRepo = mock<LeagueRepository> {
            on { findByName(leagueName) } doReturn Optional.of(
                League(
                    leagueName,
                    leagueInitialElo,
                    16,
                    League.Points(1.0, 0.5, 0.0)
                )
            )
        }
        val playerRepo = mock<PlayerRepository> {
            on { findByLeagueAndName(leagueName, name) } doReturn Optional.of(player)
        }
        val mapper = mock<PlayerMapper> {}

        //test
        val service = PlayerService(playerRepo, leagueRepo, mapper)
        val result = service.createPlayer(PlayerDto(name, leagueName))

        assertEquals(Result.Status.ERROR, result.status)
        assertEquals(null, result.entity)
        assertEquals("Player with name $name in league $leagueName already exists", result.details)

        verify(playerRepo, times(1)).findByLeagueAndName(leagueName, name)

        verifyNoMoreInteractions(leagueRepo, playerRepo, mapper)
    }

    @Test
    fun createPlayerAndLeagueNotExistsThenError() {
        val name = "player"
        val leagueName = "league"

        //mocks
        val leagueRepo = mock<LeagueRepository> {
            on { findByName(leagueName) } doReturn Optional.empty()
        }
        val playerRepo = mock<PlayerRepository> {
            on { findByLeagueAndName(leagueName, name) } doReturn Optional.empty()
        }
        val mapper = mock<PlayerMapper> {}

        //test
        val service = PlayerService(playerRepo, leagueRepo, mapper)
        val result = service.createPlayer(PlayerDto(name, leagueName))

        assertEquals(Result.Status.ERROR, result.status)
        assertEquals(null, result.entity)
        assertEquals("League $leagueName does not exists", result.details)

        verify(playerRepo, times(1)).findByLeagueAndName(leagueName, name)
        verify(leagueRepo, times(1)).findByName(leagueName)

        verifyNoMoreInteractions(leagueRepo, playerRepo, mapper)
    }

}