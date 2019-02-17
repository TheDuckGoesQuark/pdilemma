package dist.sys.pdilemma.services.impl;

import dist.sys.pdilemma.models.Choice;
import dist.sys.pdilemma.services.RandomChoiceService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomChoiceServiceImpl implements RandomChoiceService {

    private static final Random random = new Random();

    @Override
    public Choice getRandomChoice() {
        if (random.nextBoolean()) return Choice.BETRAY;
        else return Choice.COOPERATE;
    }
}
