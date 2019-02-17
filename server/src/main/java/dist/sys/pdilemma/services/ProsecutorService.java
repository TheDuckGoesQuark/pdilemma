package dist.sys.pdilemma.services;

import dist.sys.pdilemma.models.ChoiceRequestModel;
import dist.sys.pdilemma.models.ProsecutorResponseModel;

public interface ProsecutorService {
    /**
     * Testing method that can be used to ensure the service is being reached.
     * @return A friendly hello message on success.
     */
    String testConnection();

    /**
     * Processes the given choice and returns the result of the choice.
     * @param choice prisoners choice
     * @return number of years reduction due to choice
     */
    ProsecutorResponseModel chooseOption(ChoiceRequestModel choice);
}
