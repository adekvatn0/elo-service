package io.adekvatn0.eloservice.match.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document("elo_Match")
data class Match(
    val league: String,
    val player1: String,
    val player2: String,
    val result: MatchResult,
    val eloDelta: Int?,
    val timestamp: Long?
)