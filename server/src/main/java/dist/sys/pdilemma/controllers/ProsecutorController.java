package dist.sys.pdilemma.controllers;

import dist.sys.pdilemma.services.ProsecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prosecutor")
public class ProsecutorController {

    private static final Logger LOG = LoggerFactory.getLogger(ProsecutorController.class);

    @Autowired
    private ProsecutorService prosecutorService;

    @RequestMapping("/test")
    public String testConnection() {
        LOG.debug("Test connection reached");
        return prosecutorService.testConnection();
    }

}
