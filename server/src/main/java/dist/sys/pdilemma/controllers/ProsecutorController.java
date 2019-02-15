package dist.sys.pdilemma.controllers;

import dist.sys.pdilemma.services.ProsecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prosecutor")
public class ProsecutorController {

    @Autowired
    private ProsecutorService prosecutorService;

    @RequestMapping("/test")
    public String testConnection() {
        return prosecutorService.testConnection();
    }

}
