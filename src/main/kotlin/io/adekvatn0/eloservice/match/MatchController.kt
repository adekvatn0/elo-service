package io.adekvatn0.eloservice.match

import io.adekvatn0.eloservice.dto.Result
import io.adekvatn0.eloservice.match.dto.MatchDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/league/{league}/matches")
class MatchController(
    private val matchService: MatchService
) {

    @GetMapping
    fun findMatchesForPlayer(
        @RequestParam(required = false) name: String,
        @RequestParam(required = false) page: Int = 0,
        @RequestParam(required = false) size: Int = 10,
        @PathVariable league: String,
    ): Result<List<MatchDto>> {
        return matchService.findMatchesForPlayer(name, page, size)
    }

    @PostMapping
    fun createMatch(
        @RequestBody request: MatchDto
    ): Result<MatchDto> {
        return matchService.createMatch(request)
    }

}