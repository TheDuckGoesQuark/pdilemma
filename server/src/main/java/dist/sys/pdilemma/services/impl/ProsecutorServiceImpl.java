package dist.sys.pdilemma.services.impl;

import dist.sys.pdilemma.entities.Game;
import dist.sys.pdilemma.entities.Prisoner;
import dist.sys.pdilemma.exceptions.NotFoundException;
import dist.sys.pdilemma.models.*;
import dist.sys.pdilemma.repositories.GameRepository;
import dist.sys.pdilemma.repositories.PrisonerRepository;
import dist.sys.pdilemma.services.ProsecutorService;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProsecutorServiceImpl implements ProsecutorService {

    private final GameRepository gameRepository;
    private final PrisonerRepository prisonerRepository;

    @Autowired
    public ProsecutorServiceImpl(GameRepository gameRepository, PrisonerRepository prisonerRepository) {
        this.gameRepository = gameRepository;
        this.prisonerRepository = prisonerRepository;
    }

    @Override
    public String testConnection() {
        return "Hello from Server";
    }

    private Game getGame(int gameId) throws NotFoundException {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new NotFoundException(String.format("Game with Id %d not found.", gameId)));
    }

    private Prisoner getPrisonerFromGame(final Game game, int prisonerId) throws NotFoundException {
        return game.getPrisoners()
                .stream()
                .filter(p -> p.getPrisonerId() == prisonerId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Prisoner with Id %d not found in game with Id %d", prisonerId, game.getGameId())));
    }

    @Override
    @Transactional
    public PrisonerModel setPrisonersChoice(int gameId, int prisonerId, ChoiceRequestModel choiceRequest) throws NotFoundException {
        final Prisoner prisoner = getPrisonerFromGame(getGame(gameId), prisonerId);

        prisoner.setChoice(choiceRequest.getChoice());

        return prisoner.toModel();
    }

    @Override
    public GameModel startNewGame() {
        final Game game = gameRepository.save(new Game());
        return game.toModel();
    }

    @Override
    public Set<GameModel> getAllGames() {
        final Set<GameModel> games = new HashSet<>();
        for (Game game : gameRepository.findAll()) {
            games.add(game.toModel());
        }
        return games;
    }

    @Override
    public GameModel getGameById(int gameId) throws NotFoundException {
        return getGame(gameId).toModel();
    }

    @Override
    public PrisonerModel addPrisonerToGame(int gameId) throws NotFoundException {
        final Game game = getGame(gameId) ;

        final Prisoner prisoner = new Prisoner();
        prisoner.setGame(game);

        return prisonerRepository.save(prisoner).toModel();
    }

    @Override
    public PrisonerModel getPrisonerById(int gameId, int prisonerId) throws NotFoundException {
        return getPrisonerFromGame(getGame(gameId), prisonerId).toModel();
    }

    @Override
    public Set<PrisonerModel> getAllPrisonersFromGame(int gameId) throws NotFoundException {
        return getGame(gameId).getPrisoners().stream().map(Prisoner::toModel).collect(Collectors.toSet());
    }

    @Override
    public void deleteGameById(int gameId) throws NotFoundException {
        final Game game = getGame(gameId);
        gameRepository.delete(game);
    }
}
