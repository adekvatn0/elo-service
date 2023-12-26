package io.adekvatn0.eloservice.player.repo

import io.adekvatn0.eloservice.player.entity.Player
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface PlayerRepository : MongoRepository<Player, String> {
    fun findByLeagueAndName(league: String, name: String): Optional<Player>
    fun findAllByLeague(league: String, pageable: Pageable): List<Player>
}