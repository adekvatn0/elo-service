package io.adekvatn0.eloservice.match.repo

import io.adekvatn0.eloservice.match.entity.Match
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface MatchRepository : MongoRepository<Match, String> {
    fun findAllByPlayer1OrPlayer2(name: String, pageable: Pageable): List<Match>
}