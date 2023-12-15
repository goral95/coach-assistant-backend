package com.example.coachassistantbackend.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.coachassistantbackend.Entity.Player;
import com.example.coachassistantbackend.Repository.PlayerRepository;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    PlayerService(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    public List<Player> findAllPlayers(){
        return playerRepository.findAll();
    }
    
}
