package dist.sys.pdilemma.controllers;

import dist.sys.pdilemma.entities.Prisoner;
import dist.sys.pdilemma.exceptions.BaseException;
import dist.sys.pdilemma.exceptions.ConflictException;
import dist.sys.pdilemma.exceptions.NotFoundException;
import dist.sys.pdilemma.exceptions.PreconditionFailedException;
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

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static dist.sys.pdilemma.models.Choice.BETRAY;
import static dist.sys.pdilemma.models.Choice.COOPERATE;
import static java.lang.Enum.valueOf;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProsecutorController.class)
public class ProsecutorControllerUnitTests {

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
        prisoners.add(new PrisonerModel(2, BETRAY));
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
    public void getAllGamesByJoinabilityWhenNoGamesAndJoinableTrue() throws Exception {
        when(prosecutorService.getAllGamesWithJoinability(eq(true))).thenReturn(new HashSet<>());

        MvcResult result = mockMvc.perform(get("/prosecutor/games?joinable=true")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void getAllGamesByJoinabilityWhenNoGamesAndJoinableFalse() throws Exception {
        when(prosecutorService.getAllGamesWithJoinability(eq(false))).thenReturn(new HashSet<>());

        MvcResult result = mockMvc.perform(get("/prosecutor/games?joinable=false")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void getAllGamesByJoinabilityWhenOneGameJoinableFalse() throws Exception {
        Set<PrisonerModel> prisoners = new HashSet<>();
        prisoners.add(new PrisonerModel(1, Choice.COOPERATE));
        prisoners.add(new PrisonerModel(2, BETRAY));
        GameModel gameModel = new GameModel(1, prisoners);
        when(prosecutorService.getAllGamesWithJoinability(eq(false)))
                .thenReturn(Collections.singleton(gameModel));

        MvcResult result = mockMvc.perform(get("/prosecutor/games?joinable=false")
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
    public void startNewGame() throws Exception {
        GameModel gameOne = new GameModel(1, new HashSet<>());

        when(prosecutorService.startNewGame()).thenReturn(gameOne);

        MvcResult result = mockMvc.perform(post("/prosecutor/games")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gameId").value(1))
                .andExpect(jsonPath("$.prisoners.length()").value(0))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void getGameByIdWhenNotFound() throws Exception {
        when(prosecutorService.getGameById(anyInt())).thenThrow(new NotFoundException("Exception Text"));

        MvcResult result = mockMvc.perform(get("/prosecutor/games/1")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value("Exception Text"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void getGameByIdWhenFound() throws Exception {
        GameModel gameOne = new GameModel(1, new HashSet<>());
        when(prosecutorService.getGameById(anyInt())).thenReturn(gameOne);

        MvcResult result = mockMvc.perform(get("/prosecutor/games/1")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value(1))
                .andExpect(jsonPath("$.prisoners.length()").value(0))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void deleteGameByIdWhenNotFound() throws Exception {
        doThrow(new NotFoundException("Not Found Text")).when(prosecutorService).deleteGameById(anyInt());

        MvcResult result = mockMvc.perform(delete("/prosecutor/games/1")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value("Not Found Text"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void deleteGameByIdWhenFound() throws Exception {
        doNothing().when(prosecutorService).deleteGameById(anyInt());

        mockMvc.perform(delete("/prosecutor/games/1")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getAllPrisonersFromGameWhenNotFound() throws Exception {
        doThrow(new NotFoundException("Not Found Text"))
                .when(prosecutorService).getAllPrisonersFromGame(anyInt());

        mockMvc.perform(get("/prosecutor/games/1/prisoners")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllPrisonersFromGameWhenOnePrisoner() throws Exception {
        Set<PrisonerModel> prisonerModels = new HashSet<>();
        prisonerModels.add(new PrisonerModel(1, BETRAY));

        when(prosecutorService.getAllPrisonersFromGame(anyInt())).thenReturn(prisonerModels);

        MvcResult result = mockMvc.perform(get("/prosecutor/games/1/prisoners")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].prisonerId").value(1))
                .andExpect(jsonPath("$.[0].choice").value("B"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void getAllPrisonersFromGameWhenTwoPrisoners() throws Exception {
        Set<PrisonerModel> prisonerModels = new HashSet<>();
        prisonerModels.add(new PrisonerModel(1, BETRAY));
        prisonerModels.add(new PrisonerModel(2, COOPERATE));

        when(prosecutorService.getAllPrisonersFromGame(anyInt())).thenReturn(prisonerModels);

        MvcResult result = mockMvc.perform(get("/prosecutor/games/1/prisoners")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void addPrisonerToGameWhenGameNotFound() throws Exception {
        doThrow(new NotFoundException("Not Found Text")).when(prosecutorService).addPrisonerToGame(anyInt());

        MvcResult result = mockMvc.perform(post("/prosecutor/games/1/prisoners")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value("Not Found Text"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void addPrisonerToGameWhenGameFound() throws Exception {
        when(prosecutorService.addPrisonerToGame(anyInt())).thenReturn(new PrisonerModel(1, null));

        MvcResult result = mockMvc.perform(post("/prosecutor/games/1/prisoners")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.prisonerId").value(1))
                .andExpect(jsonPath("$.choice").isEmpty())
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void addPrisonerToGameWhenTooManyPrisoners() throws Exception {
        doThrow(new ConflictException("Conflict Text")).when(prosecutorService).addPrisonerToGame(anyInt());

        MvcResult result = mockMvc.perform(post("/prosecutor/games/1/prisoners")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Conflict Text"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void getPrisonerByIdWhenNotFound() throws Exception {
        when(prosecutorService.getPrisonerById(anyInt(), anyInt()))
                .thenThrow(new NotFoundException("Exception Text"));

        MvcResult result = mockMvc.perform(get("/prosecutor/games/1/prisoners/1")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Exception Text"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void getPrisonerByIdWhenFound() throws Exception {
        PrisonerModel prisonerModel = new PrisonerModel(1, BETRAY);
        when(prosecutorService.getPrisonerById(anyInt(), anyInt())).thenReturn(prisonerModel);

        MvcResult result = mockMvc.perform(get("/prosecutor/games/1/prisoners/1")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prisonerId").value(1))
                .andExpect(jsonPath("$.choice").value("B"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void setPrisonersChoiceWhenNotFound() throws Exception {
        when(prosecutorService.setPrisonersChoice(anyInt(), anyInt(), any()))
                .thenThrow(new NotFoundException("Exception Text"));

        MvcResult result = mockMvc.perform(put("/prosecutor/games/1/prisoners/1")
                .content("{\"choice\":\"B\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Exception Text"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void setPrisonersChoiceWhenConflict() throws Exception {
        when(prosecutorService.setPrisonersChoice(anyInt(), anyInt(), any()))
                .thenThrow(new ConflictException("Exception Text"));

        MvcResult result = mockMvc.perform(put("/prosecutor/games/1/prisoners/1")
                .content("{\"choice\":\"B\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Exception Text"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void setPrisonersChoiceWhenNullBody() throws Exception {
        mockMvc.perform(put("/prosecutor/games/1/prisoners/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void setPrisonersChoiceWhenChoiceMissing() throws Exception {
         mockMvc.perform(put("/prosecutor/games/1/prisoners/1")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void setPrisonersChoiceWhenChoiceNull() throws Exception {
        MvcResult result = mockMvc.perform(put("/prosecutor/games/1/prisoners/1")
                .content("{\"choice\": null}")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void setPrisonersChoiceWhenFound() throws Exception {
        PrisonerModel prisonerModel = new PrisonerModel(1, BETRAY);
        when(prosecutorService.setPrisonersChoice(anyInt(), anyInt(), any())).thenReturn(prisonerModel);

        MvcResult result = mockMvc.perform(put("/prosecutor/games/1/prisoners/1")
                .content("{\"choice\":\"B\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.prisonerId").value(1))
                .andExpect(jsonPath("$.choice").value("B"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void getNumYearsReduction() throws Exception {
        ProsecutorResponseModel response = new ProsecutorResponseModel(5);
        when(prosecutorService.getYearsReduction(anyInt(), anyInt())).thenReturn(response);

        MvcResult result = mockMvc.perform(get("/prosecutor/games/1/prisoners/1/numYearsReduction")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numYearsReduction").value(5))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void getNumYearsReductionWhenNotFound() throws Exception {
        when(prosecutorService.getYearsReduction(anyInt(), anyInt()))
                .thenThrow(new NotFoundException("Exception Text"));

        MvcResult result = mockMvc.perform(get("/prosecutor/games/1/prisoners/1/numYearsReduction")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Exception Text"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void getNumYearsReductionWhenPreconditionFailed() throws Exception {
        when(prosecutorService.getYearsReduction(anyInt(), anyInt()))
                .thenThrow(new PreconditionFailedException("Exception Text"));

        MvcResult result = mockMvc.perform(get("/prosecutor/games/1/prisoners/1/numYearsReduction")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.message").value("Exception Text"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }
}