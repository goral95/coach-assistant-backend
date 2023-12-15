package com.example.coachassistantbackend.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.coachassistantbackend.Entity.Player;
import com.example.coachassistantbackend.Service.PlayerService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api")
@Transactional
public class PlayerResource {
    
    private final Logger log = LoggerFactory.getLogger(PlayerResource.class);

    private final PlayerService playerService;

    PlayerResource(PlayerService playerService){
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayers(){
        log.debug("REST attempt to get all players");
        return ResponseEntity.ok().body(playerService.findAllPlayers());
    }

}
