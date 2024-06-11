package com.example.coachassistantbackend.Service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.coachassistantbackend.Entity.Player;
import com.example.coachassistantbackend.Repository.PlayerRepository;
import com.example.coachassistantbackend.Service.PlayerService;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> findPlayer(Long id) {
        return playerRepository.findById(id);
    }

    public boolean playerExist(Long id) {
        return playerRepository.existsById(id);
    }

    public Player save(Player player) {
        return playerRepository.save(player);
    }

    public void delete(Long id) {
        playerRepository.deleteById(id);
    }

}
