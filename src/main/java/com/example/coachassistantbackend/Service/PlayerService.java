package com.example.coachassistantbackend.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.coachassistantbackend.Entity.Player;
import com.example.coachassistantbackend.Repository.PlayerRepository;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    public List<Player> findAllPlayers(){
        return playerRepository.findAll();
    }

    public Optional<Player> findPlayer(Long id){
        return playerRepository.findById(id);
    }

    public boolean playerExist(Long id){
        return playerRepository.existsById(id);
    }

    public Player save(Player player){
        return playerRepository.save(player);
    }

    public void delete(Long id){
        playerRepository.deleteById(id);
    }
    
}
