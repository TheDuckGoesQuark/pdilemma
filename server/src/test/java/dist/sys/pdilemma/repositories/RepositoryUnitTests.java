package dist.sys.pdilemma.repositories;

import com.google.common.collect.Iterables;
import dist.sys.pdilemma.entities.Game;
import dist.sys.pdilemma.entities.Prisoner;
import dist.sys.pdilemma.exceptions.ConflictException;
import dist.sys.pdilemma.exceptions.NotFoundException;
import dist.sys.pdilemma.exceptions.PreconditionFailedException;
import dist.sys.pdilemma.models.ChoiceRequestModel;
import dist.sys.pdilemma.models.GameModel;
import dist.sys.pdilemma.models.PrisonerModel;
import dist.sys.pdilemma.models.ProsecutorResponseModel;
import dist.sys.pdilemma.repositories.GameRepository;
import dist.sys.pdilemma.repositories.PrisonerRepository;
import dist.sys.pdilemma.services.ProsecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static dist.sys.pdilemma.models.Choice.BETRAY;
import static dist.sys.pdilemma.models.Choice.COOPERATE;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Integration tests for service and persistence layer
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryUnitTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PrisonerRepository prisonerRepository;

    @Test
    public void findAllFullWhenTwoGamesWithNoPrisoners() {
        Game gameOne = gameRepository.save(new Game());
        Game gameTwo = gameRepository.save(new Game());

        entityManager.flush();

        Iterable<Game> result = gameRepository.findAllFull();

        assertEquals(0, Iterables.size(result));
    }

    @Test
    public void findAllAvailableWhenTwoGamesWithNoPrisoners() {
        Game gameOne = gameRepository.save(new Game());
        Game gameTwo = gameRepository.save(new Game());

        entityManager.flush();

        Iterable<Game> result = gameRepository.findAllJoinable();

        assertEquals(2, Iterables.size(result));
        assertTrue(Iterables.contains(result, gameOne));
        assertTrue(Iterables.contains(result, gameTwo));
    }

    @Test
    public void findAllFullWhenTwoGamesWithOnePrisoner() {
        Game gameOne = gameRepository.save(new Game());
        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne = prisonerRepository.save(prisonerOne);
        gameOne.addPrisoner(prisonerOne);

        Game gameTwo = gameRepository.save(new Game());
        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameTwo);
        prisonerTwo = prisonerRepository.save(prisonerTwo);
        gameTwo.addPrisoner(prisonerTwo);

        entityManager.flush();

        Iterable<Game> result = gameRepository.findAllFull();

        assertEquals(0, Iterables.size(result));
    }

    @Test
    public void findAllAvailableWhenTwoGamesWithOnePrisoner() {
        Game gameOne = gameRepository.save(new Game());
        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne = prisonerRepository.save(prisonerOne);
        gameOne.addPrisoner(prisonerOne);

        Game gameTwo = gameRepository.save(new Game());
        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameTwo);
        prisonerTwo = prisonerRepository.save(prisonerTwo);
        gameTwo.addPrisoner(prisonerTwo);

        entityManager.flush();

        Iterable<Game> result = gameRepository.findAllJoinable();

        assertEquals(2, Iterables.size(result));
        for (Game game : result) {
            if (game.equals(gameOne)) {
                assertEquals(1, game.getPrisoners().size());
                assertEquals(prisonerOne, game.getPrisoners().iterator().next());
            } else if (game.equals(gameTwo)) {
                assertEquals(1, game.getPrisoners().size());
                assertEquals(prisonerTwo, game.getPrisoners().iterator().next());
            } else {
                fail(String.format("Game with unknown ID returned %d", game.getGameId()));
            }

        }
    }

    @Test
    public void findAllFullWhenTwoGamesWithTwoPrisoners() {
        Game gameOne = gameRepository.save(new Game());

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne = prisonerRepository.save(prisonerOne);
        gameOne.addPrisoner(prisonerOne);

        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameOne);
        prisonerTwo = prisonerRepository.save(prisonerTwo);
        gameOne.addPrisoner(prisonerTwo);

        Game gameTwo = gameRepository.save(new Game());
        Prisoner prisonerThree = new Prisoner();
        prisonerThree.setGame(gameTwo);
        prisonerThree = prisonerRepository.save(prisonerThree);
        gameTwo.addPrisoner(prisonerThree);

        Prisoner prisonerFour = new Prisoner();
        prisonerFour.setGame(gameTwo);
        prisonerFour = prisonerRepository.save(prisonerFour);
        gameTwo.addPrisoner(prisonerFour);

        entityManager.flush();

        Iterable<Game> result = gameRepository.findAllFull();

        assertEquals(2, Iterables.size(result));
        assertTrue(Iterables.contains(result, gameOne));
        assertTrue(Iterables.contains(result, gameTwo));

        for (Game game : result) {
            if (game.equals(gameOne)) {
                assertEquals(2, game.getPrisoners().size());
                assertTrue(Iterables.contains(game.getPrisoners(), prisonerOne));
                assertTrue(Iterables.contains(game.getPrisoners(), prisonerTwo));
            } else if (game.equals(gameTwo)) {
                assertEquals(2, game.getPrisoners().size());
                assertTrue(Iterables.contains(game.getPrisoners(), prisonerThree));
                assertTrue(Iterables.contains(game.getPrisoners(), prisonerFour));
            } else {
                fail(String.format("Game with unknown ID returned %d", game.getGameId()));
            }

        }
    }

    @Test
    public void findAllAvailableWhenTwoGamesWithTwoPrisoners() {
        Game gameOne = gameRepository.save(new Game());

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne = prisonerRepository.save(prisonerOne);
        gameOne.addPrisoner(prisonerOne);

        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameOne);
        prisonerTwo = prisonerRepository.save(prisonerTwo);
        gameOne.addPrisoner(prisonerTwo);

        Game gameTwo = gameRepository.save(new Game());
        Prisoner prisonerThree = new Prisoner();
        prisonerThree.setGame(gameTwo);
        prisonerThree = prisonerRepository.save(prisonerThree);
        gameTwo.addPrisoner(prisonerThree);

        Prisoner prisonerFour = new Prisoner();
        prisonerFour.setGame(gameTwo);
        prisonerFour = prisonerRepository.save(prisonerFour);
        gameTwo.addPrisoner(prisonerFour);

        entityManager.flush();

        Iterable<Game> result = gameRepository.findAllJoinable();

        assertEquals(0, Iterables.size(result));
    }

    @Test
    public void findAllFullWhenOneFullOneAvailable() {
        Game gameOne = gameRepository.save(new Game());

        Prisoner prisonerOne = new Prisoner();
        prisonerOne.setGame(gameOne);
        prisonerOne = prisonerRepository.save(prisonerOne);
        gameOne.addPrisoner(prisonerOne);

        Prisoner prisonerTwo = new Prisoner();
        prisonerTwo.setGame(gameOne);
        prisonerTwo = prisonerRepository.save(prisonerTwo);
        gameOne.addPrisoner(prisonerTwo);

        Game gameTwo = gameRepository.save(new Game());
        Prisoner prisonerThree = new Prisoner();
        prisonerThree.setGame(gameTwo);
        prisonerThree = prisonerRepository.save(prisonerThree);
        gameTwo.addPrisoner(prisonerThree);

        entityManager.flush();

        Iterable<Game> result = gameRepository.findAllFull();

        assertEquals(1, Iterables.size(result));
        assertTrue(Iterables.contains(result, gameOne));

        Game game = result.iterator().next();
        assertEquals(2, game.getPrisoners().size());
        assertTrue(Iterables.contains(game.getPrisoners(), prisonerOne));
        assertTrue(Iterables.contains(game.getPrisoners(), prisonerTwo));
    }

}