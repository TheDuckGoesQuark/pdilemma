package dist.sys.pdilemma.services.impl;

import dist.sys.pdilemma.models.Choice;
import dist.sys.pdilemma.models.ChoiceRequestModel;
import dist.sys.pdilemma.models.ProsecutorResponseModel;
import dist.sys.pdilemma.services.ProsecutorService;
import dist.sys.pdilemma.services.RandomChoiceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ProsecutorServiceImpl.class})
@EnableConfigurationProperties
public class ProsecutorServiceImplTest {

    @Autowired
    private ProsecutorService prosecutorService;

    @MockBean
    private RandomChoiceService randomChoiceService;

    @Test
    public void testConnection() {
        String result = prosecutorService.testConnection();
        assertEquals(result, "Hello from Server");
    }

    @Test
    public void chooseOptionBothBetray() {
        when(randomChoiceService.getRandomChoice()).thenReturn(Choice.BETRAY);

        final ChoiceRequestModel request = new ChoiceRequestModel();
        request.setChoice(Choice.BETRAY);

        ProsecutorResponseModel response = prosecutorService.chooseOption(request);
        assertEquals(1, response.getNumYearsReduction());
    }

    @Test
    public void chooseOptionP1BetrayP2Cooperate() {
        when(randomChoiceService.getRandomChoice()).thenReturn(Choice.COOPERATE);

        final ChoiceRequestModel request = new ChoiceRequestModel();
        request.setChoice(Choice.BETRAY);

        ProsecutorResponseModel response = prosecutorService.chooseOption(request);
        assertEquals(3, response.getNumYearsReduction());
    }

    @Test
    public void chooseOptionBothCooperate() {
        when(randomChoiceService.getRandomChoice()).thenReturn(Choice.COOPERATE);

        final ChoiceRequestModel request = new ChoiceRequestModel();
        request.setChoice(Choice.COOPERATE);

        ProsecutorResponseModel response = prosecutorService.chooseOption(request);
        assertEquals(5, response.getNumYearsReduction());
    }

    @Test
    public void chooseOptionP1CooperateP2Betray() {
        when(randomChoiceService.getRandomChoice()).thenReturn(Choice.BETRAY);

        final ChoiceRequestModel request = new ChoiceRequestModel();
        request.setChoice(Choice.COOPERATE);

        ProsecutorResponseModel response = prosecutorService.chooseOption(request);
        assertEquals(2, response.getNumYearsReduction());
    }
}