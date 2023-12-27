package io.adekvatn0.eloservice.match

import io.adekvatn0.eloservice.dto.Result
import io.adekvatn0.eloservice.match.dto.MatchDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/leagues/{league}/matches")
class MatchController(
    private val matchService: MatchService
) {

    @GetMapping
    fun findMatchesForPlayer(
        @PathVariable league: String,
        @RequestParam(required = false) name: String,
        @RequestParam(required = false) page: Int = 0,
        @RequestParam(required = false) size: Int = 10,
    ): Result<List<MatchDto>> {
        return matchService.findMatchesForPlayer(league, name, page, size)
    }

    @PostMapping
    fun createMatch(
        @RequestBody request: MatchDto
    ): Result<MatchDto> {
        return matchService.createMatch(request)
    }

}