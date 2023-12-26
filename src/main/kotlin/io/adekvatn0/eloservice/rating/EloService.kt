package io.adekvatn0.eloservice.rating

import org.springframework.stereotype.Service
import java.math.BigDecimal
import kotlin.math.pow
import kotlin.math.roundToInt

@Service
class EloService {

    /**
     * https://en.wikipedia.org/wiki/Elo_rating_system
     */
    fun calculateDelta(r1: Int, r2: Int, points: Double, kFactor: Int): Int {
        val expectedScore =
            1.0 / (1.0 + 10.0.pow((BigDecimal(r2) - BigDecimal(r1)).divide(BigDecimal(400)).toDouble()))

        val newR1 = r1 + kFactor * (points - expectedScore)

        return newR1.roundToInt() - r1
    }
}