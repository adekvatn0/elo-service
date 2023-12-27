package io.adekvatn0.eloservice.match.repo

import io.adekvatn0.eloservice.match.entity.Match
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface MatchRepository : MongoRepository<Match, String> {

    @Query("{ \$or: [{league: ?0, player1:  ?1}, {league: ?0, player2:  ?1}] }")
    fun findAllByPlayerAndLeague(leagueName: String, playerName: String, pageable: Pageable): List<Match>
}