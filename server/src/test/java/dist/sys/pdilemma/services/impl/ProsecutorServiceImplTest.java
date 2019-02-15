package dist.sys.pdilemma.services.impl;

import dist.sys.pdilemma.services.ProsecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ProsecutorServiceImpl.class})
@EnableConfigurationProperties
public class ProsecutorServiceImplTest {

    @Autowired
    private ProsecutorService prosecutorService;

    @Test
    public void testConnectionTest() {
        String result = prosecutorService.testConnection();

        assertEquals(result, "Hello from Server");
    }

}