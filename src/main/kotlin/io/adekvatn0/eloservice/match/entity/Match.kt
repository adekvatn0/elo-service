package io.adekvatn0.eloservice.match.entity

import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@Document("elo_Match")
data class Match(
    val league: String,
    val player1: String,
    val player2: String,
    val eloDelta: Int,
    val result: MatchResult,
    val date: ZonedDateTime
)