package io.adekvatn0.eloservice.league.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("elo_League")
data class League(
    @Id
    val name: String,
    val initialElo: Int,
    val kfactor: Int,
    val points: Points
) {
    data class Points(
        val win: Double,
        val draw: Double,
        val lose: Double
    )
}