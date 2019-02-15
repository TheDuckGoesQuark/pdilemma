package dist.sys.pdilemma.services.impl;

import dist.sys.pdilemma.services.ProsecuterService;
import org.springframework.stereotype.Service;

@Service
public class ProsecuterServiceImpl implements ProsecuterService {
    @Override
    public String testConnection() {
        return "Hello from Server";
    }
}
