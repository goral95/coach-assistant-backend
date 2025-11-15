package com.example.coachassistantbackend.Controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.coachassistantbackend.Entity.Player;
import com.example.coachassistantbackend.Exception.ObjectIdentityException;
import com.example.coachassistantbackend.Exception.ObjectNotFoundException;
import com.example.coachassistantbackend.Service.PlayerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Transactional
public class PlayerController {

    private final Logger log = LoggerFactory.getLogger(PlayerController.class);

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayers() {
        log.debug("REST request to get all players");
        return ResponseEntity.ok().body(playerService.findAllPlayers());
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        log.debug("REST request to get player with id: {}", id);
        Player result = playerService.findPlayer(id)
                .orElseThrow(() -> new ObjectNotFoundException("Player not exist"));
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/players")
    public ResponseEntity<Player> createNewPlayer(@RequestBody @Valid Player player) throws URISyntaxException {
        log.debug("REST request to save player: {}", player);
        Player result = playerService.save(player);
        return ResponseEntity
                .created(new URI("/api/player/" + result.getId()))
                .body(result);
    }

    @PutMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody @Valid Player player) {
        log.debug("REST request to update player with id: {}", player.getId());
        if (player.getId() == null) {
            throw new ObjectIdentityException("Player id can't be null");
        }
        if (!Objects.equals(id, player.getId())) {
            throw new ObjectIdentityException("There is problem to identify player. Please check body and path id.");
        }
        if (!playerService.playerExist(id)) {
            throw new ObjectNotFoundException("Player not exist");
        }
        Player result = playerService.save(player);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        log.debug("REST request to delete player with id: {}", id);
        if (!playerService.playerExist(id)) {
            throw new ObjectNotFoundException("Player not exist");
        }
        playerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
