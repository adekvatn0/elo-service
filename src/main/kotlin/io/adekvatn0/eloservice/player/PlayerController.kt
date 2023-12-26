package io.adekvatn0.eloservice.player

import io.adekvatn0.eloservice.dto.Result
import io.adekvatn0.eloservice.player.dto.PlayerDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/leagues/{league}/players")
class PlayerController(
    private val playerService: PlayerService
) {

    @GetMapping
    fun findPlayers(
        @PathVariable(required = true) league: String,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) page: Int = 0,
        @RequestParam(required = false) size: Int = 10,
    ): Result<List<PlayerDto>> {
        return playerService.findPlayers(league, name, page, size)
    }

    @PostMapping
    fun createPlayer(
        @RequestBody playerDto: PlayerDto
    ): Result<PlayerDto> {
        return playerService.createPlayer(playerDto)
    }
}