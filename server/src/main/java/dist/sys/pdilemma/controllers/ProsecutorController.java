package dist.sys.pdilemma.controllers;

import dist.sys.pdilemma.models.ChoiceRequestModel;
import dist.sys.pdilemma.models.ProsecutorResponseModel;
import dist.sys.pdilemma.services.ProsecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prosecutor")
public class ProsecutorController {

    private static final Logger LOG = LoggerFactory.getLogger(ProsecutorController.class);

    @Autowired
    private ProsecutorService prosecutorService;

    @GetMapping("/test")
    public String testConnection() {
        return prosecutorService.testConnection();
    }

    @PutMapping("/games/1/prisoners/1")
    public ProsecutorResponseModel chooseOption(@RequestBody ChoiceRequestModel choice) {
        return prosecutorService.chooseOption(choice);
    }
}
