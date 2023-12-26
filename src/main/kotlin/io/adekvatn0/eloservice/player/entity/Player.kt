package io.adekvatn0.eloservice.player.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("elo_Player")
data class Player(
    @Id
    val name: String,
    val league: String,
    val elo: Int = 0,
    val statistic: Statistic = Statistic()
) {
    data class Statistic(
        val wins: Int = 0,
        val loses: Int = 0,
        val draws: Int = 0
    )
}