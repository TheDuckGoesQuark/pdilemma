package dist.sys.pdilemma.controllers;

import dist.sys.pdilemma.services.ProsecuterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProsecutorController.class)
public class ProsecutorControllerTest {

    @MockBean
    private ProsecuterService prosecuterService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testConnection() throws Exception {

        when(prosecuterService.testConnection()).thenReturn("Hello from Server");

        MvcResult result = mockMvc.perform(get("/prosecutor/test")
        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("Hello from Server", content);
    }

}