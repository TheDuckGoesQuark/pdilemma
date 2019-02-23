package dist.sys.pdilemma.services;

import dist.sys.pdilemma.exceptions.NotFoundException;
import dist.sys.pdilemma.models.ChoiceRequestModel;
import dist.sys.pdilemma.models.GameModel;
import dist.sys.pdilemma.models.PrisonerModel;
import dist.sys.pdilemma.models.ProsecutorResponseModel;

import java.util.Set;

public interface ProsecutorService {
    /**
     * Testing method that can be used to ensure the service is being reached.
     *
     * @return A friendly hello message on success.
     */
    String testConnection();

    /**
     * Sets the given prisoner from the given games choice
     *
     * @param gameId     id of game prisoner belongs to
     * @param prisonerId id of prisoner in game
     * @param choice     choice made by prisoner
     * @return updated prisoner with new choice
     */
    PrisonerModel setPrisonersChoice(int gameId, int prisonerId, ChoiceRequestModel choice) throws NotFoundException;

    /**
     * Creates a new game and returns a description of that game
     *
     * @return the game that has been created
     */
    GameModel startNewGame();

    /**
     * Returns all games
     *
     * @return all games that have been created
     */
    Set<GameModel> getAllGames();

    /**
     * Returns game with the given Id
     *
     * @param gameId id of game to get
     * @return the game with given Id
     */
    GameModel getGameById(int gameId) throws NotFoundException;

    /**
     * Adds a prisoner to the game with the given Id
     *
     * @param gameId id of game to add a prisoner to
     * @return description of prisoner that has been added to the game
     */
    PrisonerModel addPrisonerToGame(int gameId) throws NotFoundException;

    /**
     * Gets the prisoner from the given game that has the given Id
     * @param gameId id of game to fetch prisoner from
     * @param prisonerId id of prisoner to fetch
     * @return prisoner with the given id from the game with the given id
     */
    PrisonerModel getPrisonerById(int gameId, int prisonerId) throws NotFoundException;

    /**
     * Gets all the prisoners taking part in the game with the given id
     * @param gameId id of game to fetch prisoners for
     * @return All prisoners taking part in the game with the given Id
     */
    Set<PrisonerModel> getAllPrisonersFromGame(int gameId) throws NotFoundException;

    /**
     * Deletes the game with the given id
     * @param gameId id of game to delete
     */
    void deleteGameById(int gameId) throws NotFoundException;

    /**
     * Gets the number of years reduction resulting from a prisoners choice.
     *
     * @param gameId id of game prisoner belongs to
     * @param prisonerId id of prisoner to get number of years reduction for
     * @return number of years reduction for prisoner
     */
    ProsecutorResponseModel getYearsReduction(int gameId, int prisonerId);
}
