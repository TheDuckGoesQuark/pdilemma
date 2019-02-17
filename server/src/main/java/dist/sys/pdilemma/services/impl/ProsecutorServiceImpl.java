package dist.sys.pdilemma.services.impl;

import dist.sys.pdilemma.models.Choice;
import dist.sys.pdilemma.models.ChoiceRequestModel;
import dist.sys.pdilemma.models.ProsecutorResponseModel;
import dist.sys.pdilemma.services.ProsecutorService;
import dist.sys.pdilemma.services.RandomChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProsecutorServiceImpl implements ProsecutorService {

    private final RandomChoiceService randomChoiceService;

    @Autowired
    public ProsecutorServiceImpl(RandomChoiceService randomChoiceService) {
        this.randomChoiceService = randomChoiceService;
    }

    @Override
    public String testConnection() {
        return "Hello from Server";
    }

    @Override
    public ProsecutorResponseModel chooseOption(ChoiceRequestModel choiceRequest) {
        Choice p1Choice = choiceRequest.getChoice();
        Choice p2Choice = randomChoiceService.getRandomChoice();

        if (p1Choice == Choice.BETRAY) {
            if (p2Choice == Choice.BETRAY) return new ProsecutorResponseModel(1);
            else return new ProsecutorResponseModel(3);
        } else {
            if (p2Choice == Choice.BETRAY) return new ProsecutorResponseModel(2);
            else return new ProsecutorResponseModel(5);
        }
    }
}
