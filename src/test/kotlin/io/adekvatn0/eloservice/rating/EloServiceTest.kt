package io.adekvatn0.eloservice.rating

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EloServiceTest {

    @Test
    fun lostRatingEqualsGainedRating() {
        val service = EloService()

        val kFactor = 16

        val r1 = 1100
        val r2 = 1250

        val win = 1.0
        val draw = 0.5
        val lose = 0.0

        run {
            val r1Delta = service.calculateDelta(r1, r2, win, kFactor)
            val r2Delta = service.calculateDelta(r2, r1, lose, kFactor)
            assertThat(r1Delta + r2Delta).isEqualTo(0)
        }

        run {
            val r1Delta = service.calculateDelta(r1, r2, lose, kFactor)
            val r2Delta = service.calculateDelta(r2, r1, win, kFactor)
            assertThat(r1Delta + r2Delta).isEqualTo(0)
        }

        run {
            val r1Delta = service.calculateDelta(r1, r2, draw, kFactor)
            val r2Delta = service.calculateDelta(r2, r1, draw, kFactor)
            assertThat(r1Delta + r2Delta).isEqualTo(0)
        }
    }
}