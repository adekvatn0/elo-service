package io.adekvatn0.eloservice.match

import io.adekvatn0.eloservice.dto.Result
import io.adekvatn0.eloservice.league.repo.LeagueRepository
import io.adekvatn0.eloservice.match.dto.MatchDto
import io.adekvatn0.eloservice.match.dto.MatchMapper
import io.adekvatn0.eloservice.match.entity.MatchResult
import io.adekvatn0.eloservice.match.repo.MatchRepository
import io.adekvatn0.eloservice.player.repo.PlayerRepository
import io.adekvatn0.eloservice.rating.EloService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class MatchService(
    private val matchRepository: MatchRepository,
    private val leagueRepository: LeagueRepository,
    private val playerRepository: PlayerRepository,
    private val eloService: EloService,
    private val mapper: MatchMapper
) {

    fun findMatchesForPlayer(league: String, name: String, page: Int, size: Int): Result<List<MatchDto>> {
        val matches = matchRepository.findAllByPlayerAndLeague(
            league,
            name,
            PageRequest.of(page, size, Sort.by("timestamp").descending())
        )

        return Result.success(matches.map { mapper.toDto(it) }.toList())
    }

    @Transactional
    fun createMatch(matchDto: MatchDto): Result<MatchDto> {
        val league = leagueRepository.findById(matchDto.league).getOrNull()
            ?: return Result.error("League ${matchDto.league} does not exists")

        val p1 = playerRepository.findByLeagueAndName(matchDto.league, matchDto.player1).getOrNull()
            ?: return Result.error("Player ${matchDto.player1} does not exists")

        val p2 = playerRepository.findByLeagueAndName(matchDto.league, matchDto.player2).getOrNull()
            ?: return Result.error("Player ${matchDto.player2} does not exists")

        val points = when (matchDto.result) {
            MatchResult.WIN -> league.points.win
            MatchResult.DRAW -> league.points.draw
            MatchResult.LOSE -> league.points.lose
        }

        val delta = eloService.calculateDelta(p1.elo, p2.elo, points, league.kfactor)

        when (matchDto.result) {
            MatchResult.WIN -> {
                playerRepository.saveAll(
                    listOf(
                        p1.copy(elo = p1.elo + delta, statistic = p1.statistic.copy(wins = p1.statistic.wins + 1)),
                        p2.copy(elo = p2.elo - delta, statistic = p2.statistic.copy(loses = p2.statistic.loses + 1))
                    )
                )
            }

            MatchResult.DRAW -> {
                playerRepository.saveAll(
                    listOf(
                        p1.copy(elo = p1.elo + delta, statistic = p1.statistic.copy(draws = p1.statistic.draws + 1)),
                        p2.copy(elo = p2.elo - delta, statistic = p2.statistic.copy(draws = p2.statistic.draws + 1))
                    )
                )
            }

            MatchResult.LOSE -> {
                playerRepository.saveAll(
                    listOf(
                        p1.copy(elo = p1.elo + delta, statistic = p1.statistic.copy(loses = p1.statistic.loses + 1)),
                        p2.copy(elo = p2.elo - delta, statistic = p2.statistic.copy(wins = p2.statistic.wins + 1))
                    )
                )
            }
        }

        val match = mapper.toEntity(matchDto)
            .copy(
                timestamp = System.currentTimeMillis(),
                eloDelta = delta
            )

        val saved = matchRepository.save(match)

        return Result.success(mapper.toDto(saved))
    }
}
