package dist.sys.pdilemma.services.impl;

import dist.sys.pdilemma.entities.Game;
import dist.sys.pdilemma.entities.Prisoner;
import dist.sys.pdilemma.exceptions.ConflictException;
import dist.sys.pdilemma.exceptions.NotFoundException;
import dist.sys.pdilemma.exceptions.PreconditionFailedException;
import dist.sys.pdilemma.models.*;
import dist.sys.pdilemma.repositories.GameRepository;
import dist.sys.pdilemma.repositories.PrisonerRepository;
import dist.sys.pdilemma.services.ProsecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static dist.sys.pdilemma.models.Choice.BETRAY;
import static dist.sys.pdilemma.models.Choice.COOPERATE;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProsecutorServiceImpl.class)
@EnableConfigurationProperties
public class ProsecutorServiceImplTest {

    @Autowired
    private ProsecutorService prosecutorService;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private PrisonerRepository prisonerRepository;

    @Test
    public void testConnection() {
        String result = prosecutorService.testConnection();
        assertEquals(result, "Hello from Server");
    }

    @Test
    public void startNewGame() {
        Game newGame = new Game();
        newGame.setGameId(1);
        when(gameRepository.save(any())).thenReturn(newGame);

        GameModel response = prosecutorService.startNewGame();

        assertEquals(1, response.getGameId());
        assertNotNull(response.getPrisoners());
        assertEquals(0, response.getPrisoners().size());
    }

    @Test
    public void getAllGamesWhenNoGames() {
        when(gameRepository.findAll()).thenReturn(new HashSet<>());

        Set<GameModel> games = prosecutorService.getAllGames();

        assertEquals(0, games.size());
    }

    @Test
    public void getAllGamesWhenOneGame() {
        Game newGame = new Game();
        newGame.setGameId(1);
        when(gameRepository.findAll()).thenReturn(Collections.singleton(newGame));

        Set<GameModel> games = prosecutorService.getAllGames();

        assertEquals(1, games.size());
        GameModel responseGame = games.iterator().next();

        assertEquals(1, responseGame.getGameId());
        assertEquals(0, responseGame.getPrisoners().size());
    }

    @Test
    public void getAllGamesWhenTwoGames() {
        Game gameOne = new Game();
        gameOne.setGameId(1);
        Game gameTwo = new Game();
        gameTwo.setGameId(2);
        Set<Game> savedGames = new HashSet<>();
        savedGames.add(gameOne);
        savedGames.add(gameTwo);
        when(gameRepository.findAll()).thenReturn(savedGames);

        Set<GameModel> games = prosecutorService.getAllGames();

        assertEquals(2, games.size());
        assertTrue(games.stream().anyMatch(g -> g.getGameId() == 1));
        assertTrue(games.stream().anyMatch(g -> g.getGameId() == 2));
    }

    @Test(expected = NotFoundException.class)
    public void getGameByIdWhenGameNotExist() {
        when(gameRepository.findById(anyInt())).thenReturn(Optional.empty());
        prosecutorService.getGameById(1);
    }

    @Test
    public void getGameByIdWhenGameExist() {
        Game gameOne = new Game();
        gameOne.setGameId(1);
        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        GameModel response = prosecutorService.getGameById(1);
        assertEquals(1, response.getGameId());
        assertEquals(0, response.getPrisoners().size());
    }

    @Test(expected = NotFoundException.class)
    public void addPrisonerToGameWhenGameNotExist() {
        when(gameRepository.findById(anyInt())).thenReturn(Optional.empty());
        prosecutorService.addPrisonerToGame(1);
    }

    @Test(expected = ConflictException.class)
    public void addPrisonerToGameWhenGameFull() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setPrisonerId(1);

        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameOne);
        prisonerTwo.setPrisonerId(2);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);
        prisoners.add(prisonerTwo);

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        prosecutorService.addPrisonerToGame(1);
    }

    @Test
    public void addPrisonerToGameWhenGameHasOne() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setPrisonerId(1);

        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameOne);
        prisonerTwo.setPrisonerId(2);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));
        when(prisonerRepository.save(any())).thenReturn(prisonerTwo);

        PrisonerModel response = prosecutorService.addPrisonerToGame(1);
        assertEquals(2, response.getPrisonerId());
        assertNull(response.getChoice());
    }

    @Test
    public void addPrisonerToGameWhenGameHasNone() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setPrisonerId(1);

        Set<Prisoner> prisoners = new HashSet<>();

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));
        when(prisonerRepository.save(any())).thenReturn(prisonerOne);

        PrisonerModel response = prosecutorService.addPrisonerToGame(1);
        assertEquals(1, response.getPrisonerId());
        assertNull(response.getChoice());
    }

    @Test(expected = NotFoundException.class)
    public void getPrisonerByIdWhenGameNotExist() {
        when(gameRepository.findById(anyInt())).thenReturn(Optional.empty());
        prosecutorService.getPrisonerById(1, 1);
    }

    @Test(expected = NotFoundException.class)
    public void getPrisonerByIdWhenGamePrisonerNotExistInGame() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Set<Prisoner> prisoners = new HashSet<>();

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        prosecutorService.getPrisonerById(1, 1);
    }

    @Test
    public void getPrisonerByIdWhenPrisonerAndGameExist() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setPrisonerId(1);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        PrisonerModel prisoner = prosecutorService.getPrisonerById(1, 1);
        assertEquals(1, prisoner.getPrisonerId());
        assertNull(prisoner.getChoice());
    }

    @Test(expected = NotFoundException.class)
    public void getAllPrisonersFromGameWhenGameNotExist() {
        when(gameRepository.findById(anyInt())).thenReturn(Optional.empty());
        prosecutorService.getAllPrisonersFromGame(1);
    }

    @Test
    public void getAllPrisonersFromGameWhenNoPrisoners() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        Set<PrisonerModel> prisoners = prosecutorService.getAllPrisonersFromGame(1);
        assertEquals(0, prisoners.size());
    }

    @Test
    public void getAllPrisonersFromGameWhenOnePrisoner() {
        Game gameOne = new Game();
        gameOne.setGameId(1);


        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setPrisonerId(1);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);

        gameOne.setPrisoners(prisoners);
        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        Set<PrisonerModel> response = prosecutorService.getAllPrisonersFromGame(1);
        assertEquals(1, response.size());
        PrisonerModel responsePrisonersOne = response.iterator().next();

        assertEquals(1, responsePrisonersOne.getPrisonerId());
        assertNull(responsePrisonersOne.getChoice());
    }

    @Test
    public void getAllPrisonersFromGameWhenTwoPrisoners() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setPrisonerId(1);

        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameOne);
        prisonerTwo.setPrisonerId(2);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);
        prisoners.add(prisonerTwo);

        gameOne.setPrisoners(prisoners);
        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        Set<PrisonerModel> responsePrisoners = prosecutorService.getAllPrisonersFromGame(1);

        assertEquals(2, responsePrisoners.size());
        assertTrue(responsePrisoners.stream().anyMatch(g -> g.getPrisonerId() == 1));
        assertTrue(responsePrisoners.stream().anyMatch(g -> g.getPrisonerId() == 2));
    }

    @Test(expected = NotFoundException.class)
    public void setPrisonersChoiceWhenGameNotExist() {
        when(gameRepository.findById(anyInt())).thenReturn(Optional.empty());

        ChoiceRequestModel choice = new ChoiceRequestModel();
        choice.setChoice(COOPERATE);
        prosecutorService.setPrisonersChoice(1, 1, choice);
    }

    @Test(expected = NotFoundException.class)
    public void setPrisonersChoiceWhenPrisonerNotExistInGame() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        ChoiceRequestModel choice = new ChoiceRequestModel();
        choice.setChoice(COOPERATE);
        prosecutorService.setPrisonersChoice(1, 1, choice);
    }

    @Test
    public void setPrisonersChoiceWhenPrisonerNotMadeChoice() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setPrisonerId(1);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        ChoiceRequestModel choice = new ChoiceRequestModel();
        choice.setChoice(COOPERATE);
        PrisonerModel response = prosecutorService.setPrisonersChoice(1, 1, choice);

        assertEquals(COOPERATE, response.getChoice());
        assertEquals(1, response.getPrisonerId());
    }

    @Test(expected = ConflictException.class)
    public void setPrisonersChoiceWhenPrisonerAlreadyMadeChoice() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setChoice(COOPERATE);
        prisonerOne.setPrisonerId(1);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        ChoiceRequestModel choice = new ChoiceRequestModel();
        choice.setChoice(BETRAY);
        prosecutorService.setPrisonersChoice(1, 1, choice);
    }

    @Test(expected = NotFoundException.class)
    public void deleteGameByIdWhenGameNotExist() {
        when(gameRepository.findById(anyInt())).thenReturn(Optional.empty());
        prosecutorService.deleteGameById(1);
    }

    @Test
    public void deleteGameByIdWhenGameExist() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));
        doNothing().when(gameRepository).delete(any());

        prosecutorService.deleteGameById(1);
    }

    @Test(expected = NotFoundException.class)
    public void getYearsReductionWhenGameNotExist() {
        when(gameRepository.findById(anyInt())).thenReturn(Optional.empty());
        prosecutorService.getYearsReduction(1, 1);
    }

    @Test(expected = NotFoundException.class)
    public void getYearsReductionWhenPrisonerNotExistInGame() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Set<Prisoner> prisoners = new HashSet<>();

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        prosecutorService.getYearsReduction(1, 1);
    }

    @Test(expected = PreconditionFailedException.class)
    public void getYearsReductionWhenOnlyThisPrisonerInGame() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setPrisonerId(1);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        prosecutorService.getYearsReduction(1, 1);
    }

    @Test(expected = PreconditionFailedException.class)
    public void getYearsReductionWhenThisPrisonerNotMadeDecision() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setPrisonerId(1);

        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameOne);
        prisonerTwo.setPrisonerId(2);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);
        prisoners.add(prisonerTwo);

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        prosecutorService.getYearsReduction(1, 1);
    }

    @Test(expected = PreconditionFailedException.class)
    public void getYearsReductionWhenOtherPrisonerNotMadeDecision() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setChoice(COOPERATE);
        prisonerOne.setPrisonerId(1);

        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameOne);
        prisonerTwo.setPrisonerId(2);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);
        prisoners.add(prisonerTwo);

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        prosecutorService.getYearsReduction(1, 1);
    }

    @Test
    public void getYearsReductionWhenBothBetray() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setChoice(BETRAY);
        prisonerOne.setPrisonerId(1);

        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameOne);
        prisonerTwo.setChoice(BETRAY);
        prisonerTwo.setPrisonerId(2);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);
        prisoners.add(prisonerTwo);

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        ProsecutorResponseModel response = prosecutorService.getYearsReduction(1, 1);
        assertEquals(1, response.getNumYearsReduction());
    }

    @Test
    public void getYearsReductionWhenBothCooperate() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setChoice(COOPERATE);
        prisonerOne.setPrisonerId(1);

        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameOne);
        prisonerTwo.setChoice(COOPERATE);
        prisonerTwo.setPrisonerId(2);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);
        prisoners.add(prisonerTwo);

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        ProsecutorResponseModel response = prosecutorService.getYearsReduction(1, 1);
        assertEquals(5, response.getNumYearsReduction());
    }

    @Test
    public void getYearsReductionWhenThisBetrayThatCooperate() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setChoice(BETRAY);
        prisonerOne.setPrisonerId(1);

        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameOne);
        prisonerTwo.setChoice(COOPERATE);
        prisonerTwo.setPrisonerId(2);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);
        prisoners.add(prisonerTwo);

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        ProsecutorResponseModel response = prosecutorService.getYearsReduction(1, 1);
        assertEquals(3, response.getNumYearsReduction());
    }

    @Test
    public void getYearsReductionWhenThisCooperateThatBetray() {
        Game gameOne = new Game();
        gameOne.setGameId(1);

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne.setChoice(COOPERATE);
        prisonerOne.setPrisonerId(1);

        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameOne);
        prisonerTwo.setChoice(BETRAY);
        prisonerTwo.setPrisonerId(2);

        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(prisonerOne);
        prisoners.add(prisonerTwo);

        gameOne.setPrisoners(prisoners);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(gameOne));

        ProsecutorResponseModel response = prosecutorService.getYearsReduction(1, 1);
        assertEquals(2, response.getNumYearsReduction());
    }
}