package com.example.coachassistantbackend.Service;

import java.util.List;
import java.util.Optional;

import com.example.coachassistantbackend.Entity.Player;

public interface PlayerService {

    List<Player> findAllPlayers();

    Optional<Player> findPlayer(Long id);

    boolean playerExist(Long id);

    Player save(Player player);

    void delete(Long id);

}
