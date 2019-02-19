package dist.sys.pdilemma.controllers;

import dist.sys.pdilemma.exceptions.NotFoundException;
import dist.sys.pdilemma.models.ChoiceRequestModel;
import dist.sys.pdilemma.models.GameModel;
import dist.sys.pdilemma.models.PrisonerModel;
import dist.sys.pdilemma.services.ProsecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/prosecutor")
public class ProsecutorController {

    private static final Logger LOG = LoggerFactory.getLogger(ProsecutorController.class);

    @Autowired
    private ProsecutorService prosecutorService;

    @GetMapping("/test")
    public String testConnection() {
        return prosecutorService.testConnection();
    }

    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    public Set<GameModel> getAllGames() {
        return prosecutorService.getAllGames();
    }

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    public GameModel startNewGame() {
        return prosecutorService.startNewGame();
    }

    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GameModel getGameById(@PathVariable("gameId") int gameId) {
        return prosecutorService.getGameById(gameId);
    }

    @DeleteMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGameById(@PathVariable("gameId") int gameId) {
        prosecutorService.deleteGameById(gameId);
    }

    @GetMapping("/games/{gameId}/prisoners")
    @ResponseStatus(HttpStatus.OK)
    public Set<PrisonerModel> getAllPrisonersFromGame(@PathVariable("gameId") int gameId) {
        return prosecutorService.getAllPrisonersFromGame(gameId);
    }

    @PostMapping("/games/{gameId}/prisoners")
    @ResponseStatus(HttpStatus.CREATED)
    public PrisonerModel addPrisonerToGame(@PathVariable("gameId") int gameId) {
        return prosecutorService.addPrisonerToGame(gameId);
    }

    @GetMapping("/games/{gameId}/prisoners/{prisonerId}")
    @ResponseStatus(HttpStatus.OK)
    public PrisonerModel getPrisonerById(@PathVariable("gameId") int gameId, @PathVariable("prisonerId") int prisonerId) {
        return prosecutorService.getPrisonerById(gameId, prisonerId);
    }

    @PutMapping("/games/{gameId}/prisoners/{prisonerId}")
    @ResponseStatus(HttpStatus.OK)
    public PrisonerModel setPrisonersChoice(@PathVariable("gameId") int gameId,
                                                @PathVariable("prisonerId") int prisonerId,
                                                @RequestBody ChoiceRequestModel choice) throws NotFoundException {
        return prosecutorService.setPrisonersChoice(gameId, prisonerId, choice);
    }
}
