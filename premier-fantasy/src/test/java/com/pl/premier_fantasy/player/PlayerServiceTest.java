package com.pl.premier_fantasy.player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void getPlayers_filtersOutSummaryRows() {
        Player realPlayer = new Player();
        realPlayer.setName("Bukayo Saka");
        realPlayer.setTeam_name("Arsenal");
        realPlayer.setPosition("FW");

        Player summaryPlayer = new Player();
        summaryPlayer.setName("Squad Total");
        summaryPlayer.setTeam_name("Arsenal");
        summaryPlayer.setPosition("-");

        when(playerRepository.findAll()).thenReturn(List.of(realPlayer, summaryPlayer));

        List<Player> players = playerService.getPlayers();

        assertEquals(1, players.size());
        assertEquals("Bukayo Saka", players.get(0).getName());
    }
}
