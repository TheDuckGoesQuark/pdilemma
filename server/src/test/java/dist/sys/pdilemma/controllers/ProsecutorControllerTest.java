package dist.sys.pdilemma.controllers;

import dist.sys.pdilemma.entities.Prisoner;
import dist.sys.pdilemma.exceptions.NotFoundException;
import dist.sys.pdilemma.models.Choice;
import dist.sys.pdilemma.models.GameModel;
import dist.sys.pdilemma.models.PrisonerModel;
import dist.sys.pdilemma.models.ProsecutorResponseModel;
import dist.sys.pdilemma.repositories.PrisonerRepository;
import dist.sys.pdilemma.services.ProsecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProsecutorController.class)
public class ProsecutorControllerTest {

    @MockBean
    private ProsecutorService prosecutorService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testConnection() throws Exception {
        when(prosecutorService.testConnection()).thenReturn("Hello from Server");

        MvcResult result = mockMvc.perform(get("/prosecutor/test")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("Hello from Server", content);
    }

    @Test
    public void getAllGamesWhenNoGames() throws Exception {
        when(prosecutorService.getAllGames()).thenReturn(new HashSet<>());

        MvcResult result = mockMvc.perform(get("/prosecutor/games")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void getAllGamesWhenOneGame() throws Exception {
        Set<PrisonerModel> prisoners = new HashSet<>();
        prisoners.add(new PrisonerModel(1, Choice.COOPERATE));
        prisoners.add(new PrisonerModel(2, Choice.BETRAY));
        GameModel gameModel = new GameModel(1, prisoners);
        when(prosecutorService.getAllGames()).thenReturn(Collections.singleton(gameModel));

        MvcResult result = mockMvc.perform(get("/prosecutor/games")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].gameId").value(1))
                .andExpect(jsonPath("$[0].prisoners.length()").value(2))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void getAllGamesWhenTwoGames() throws Exception {
        GameModel gameOne = new GameModel(1, new HashSet<>());
        GameModel gameTwo = new GameModel(1, new HashSet<>());
        Set<GameModel> games = new HashSet<>();
        games.add(gameOne);
        games.add(gameTwo);

        when(prosecutorService.getAllGames()).thenReturn(games);

        MvcResult result = mockMvc.perform(get("/prosecutor/games")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void getGameByIdWhenNotFound() throws Exception {
        when(prosecutorService.getAllGames()).thenThrow(new NotFoundException("Exception Text"));

        MvcResult result = mockMvc.perform(get("/prosecutor/games")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value(2))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

}