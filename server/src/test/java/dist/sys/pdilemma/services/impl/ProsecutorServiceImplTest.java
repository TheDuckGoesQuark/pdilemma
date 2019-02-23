package dist.sys.pdilemma.services.impl;

import dist.sys.pdilemma.entities.Game;
import dist.sys.pdilemma.models.GameModel;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ProsecutorServiceImpl.class})
@EnableConfigurationProperties
public class ProsecutorServiceImplTest {

    @Autowired
    private ProsecutorService prosecutorService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PrisonerRepository prisonerRepository;

    @Test
    public void testConnection() {
        String result = prosecutorService.testConnection();
        assertEquals(result, "Hello from Server");
    }

    @Test
    public void startNewGame() {
        GameModel response = prosecutorService.startNewGame();

        assertNotEquals(0, response.getGameId());
        assertNotNull(response.getPrisoners());
        assertEquals(0, response.getPrisoners().size());
    }

    @Test
    public void getAllGamesWhenNoGames() {
                
    }

}