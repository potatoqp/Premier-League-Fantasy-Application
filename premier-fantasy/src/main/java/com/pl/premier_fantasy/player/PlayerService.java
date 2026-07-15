package com.pl.premier_fantasy.player;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    public List<Player> getPlayers(){
        return getFilteredPlayers(null, null, null, null);
    }

    public List<Player> getFilteredPlayers(String team, String name, String position, String nation) {
        return playerRepository.findAll().stream()
                .filter(this::isValidPlayer)
                .filter(player -> matchesFilter(player.getTeam_name(), team))
                .filter(player -> matchesFilter(player.getName(), name))
                .filter(player -> matchesFilter(player.getPosition(), position))
                .filter(player -> matchesFilter(player.getNation(), nation))
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                this::buildPlayerKey,
                                player -> player,
                                (first, second) -> first,
                                LinkedHashMap::new
                        ),
                        map -> List.copyOf(map.values())
                ));
    }

    //find players from a specific team
    public List<Player> getPlayersFromTeam(String teamName) {
        return getFilteredPlayers(teamName, null, null, null);
    }

    // search player name for searchbar
    public List<Player> getPlayersByName(String searchText) {
        return getFilteredPlayers(null, searchText, null, null);
    }

    // search a player by his position
    public List<Player> getPlayersByPos(String searchText) {
        return getFilteredPlayers(null, null, searchText, null);
    }

    // search a player by his nationality
    public List<Player> getPlayerByNation(String searchText) {
        return getFilteredPlayers(null, null, null, searchText);
    }

    // find a player by both team and position
    public List<Player> getPlayersByTeamAndPosition(String team, String position){
        return getFilteredPlayers(team, null, position, null);
    }

    public Player addPlayer(Player player){
        playerRepository.save(player);
        return player;
    }

    public Player updatePlayer(Player updatedPlayer){
        Optional<Player> existingPlayer = playerRepository.findByName(updatedPlayer.getName());

        if (existingPlayer.isPresent()){
            Player playerToUpdate = existingPlayer.get();
            playerToUpdate.setName(updatedPlayer.getName());
            playerToUpdate.setTeam_name(updatedPlayer.getTeam_name());
            playerToUpdate.setPosition(updatedPlayer.getPosition());
            playerToUpdate.setNation(updatedPlayer.getNation());

            playerRepository.save(playerToUpdate);
            return playerToUpdate;
        }
        return null;
    }

    @Transactional
    public void deletePlayer(String playerName) {
        playerRepository.deleteByName(playerName);
    }

    private boolean isValidPlayer(Player player) {
        if (player == null) {
            return false;
        }

        String name = Optional.ofNullable(player.getName()).orElse("").trim();
        if (name.isEmpty()) {
            return false;
        }

        String normalizedName = name.toLowerCase(Locale.ROOT);
        return !normalizedName.contains("squad total") && !normalizedName.contains("total");
    }

    private boolean matchesFilter(String fieldValue, String filterValue) {
        if (filterValue == null || filterValue.isBlank()) {
            return true;
        }

        String normalizedFieldValue = Optional.ofNullable(fieldValue).orElse("").trim().toLowerCase(Locale.ROOT);
        String normalizedFilterValue = filterValue.trim().toLowerCase(Locale.ROOT);
        return normalizedFieldValue.contains(normalizedFilterValue);
    }

    private String buildPlayerKey(Player player) {
        return String.join("|",
                Optional.ofNullable(player.getName()).orElse(""),
                Optional.ofNullable(player.getTeam_name()).orElse(""),
                Optional.ofNullable(player.getPosition()).orElse(""),
                Optional.ofNullable(player.getNation()).orElse(""));
    }
}
