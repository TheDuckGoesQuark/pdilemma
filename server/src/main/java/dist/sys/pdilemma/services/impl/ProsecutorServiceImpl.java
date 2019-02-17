package dist.sys.pdilemma.services.impl;

import dist.sys.pdilemma.models.Choice;
import dist.sys.pdilemma.models.ChoiceRequestModel;
import dist.sys.pdilemma.models.ProsecutorResponseModel;
import dist.sys.pdilemma.services.ProsecutorService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ProsecutorServiceImpl implements ProsecutorService {

    @Override
    public String testConnection() {
        return "Hello from Server";
    }

    @Override
    public ProsecutorResponseModel chooseOption(ChoiceRequestModel choice) {
        Choice otherPrisonersChoice = getRandomChoice();
        return null;
    }

    private Choice getRandomChoice() {
        Random ran = new Random();
        if (ran.nextInt(1) > 0.5) {
            return Choice.BETRAY;
        } else {
            return Choice.COOPERATE;
        }
    }
}
