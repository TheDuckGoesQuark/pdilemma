package dist.sys.pdilemma.services.impl;

import dist.sys.pdilemma.services.ProsecuterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class ProsecuterServiceImplTest {

    @Autowired
    private ProsecuterService prosecuterService;

    @Test
    public void testConnectionTest() {
        String result = prosecuterService.testConnection();

        assertEquals(result, "Hello from Server");
    }

}