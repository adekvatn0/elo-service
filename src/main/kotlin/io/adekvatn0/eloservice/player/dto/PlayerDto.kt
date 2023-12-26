package io.adekvatn0.eloservice.player.dto

data class PlayerDto(
    val name: String,
    val league: String,
    val elo: Int? = null,
    val statistic: Statistic = Statistic()
) {
    data class Statistic(
        val wins: Int = 0,
        val loses: Int = 0,
        val draws: Int = 0
    )
}