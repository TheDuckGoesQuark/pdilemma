package dist.sys.pdilemma.services;

import dist.sys.pdilemma.models.Choice;

public interface RandomChoiceService {

    /**
     * Generates a random choice
     * @return a random choice
     */
    Choice getRandomChoice();

}
