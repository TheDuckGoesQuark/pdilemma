package dist.sys.pdilemma.services.impl;

import dist.sys.pdilemma.services.ProsecutorService;
import org.springframework.stereotype.Service;

@Service
public class ProsecutorServiceImpl implements ProsecutorService {
    @Override
    public String testConnection() {
        return "Hello from Server";
    }
}
