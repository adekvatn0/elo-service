package io.adekvatn0.eloservice.league.repo

import io.adekvatn0.eloservice.league.entity.League
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface LeagueRepository : MongoRepository<League, String> {
    fun findByName(name: String): Optional<League>
}