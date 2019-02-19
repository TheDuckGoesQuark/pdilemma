package dist.sys.pdilemma.controllers;

import dist.sys.pdilemma.models.ProsecutorResponseModel;
import dist.sys.pdilemma.services.ProsecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProsecutorController.class)
public class ProsecutorControllerTest {

    @MockBean
    private ProsecutorService prosecutorService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testConnection() throws Exception {
        when(prosecutorService.testConnection()).thenReturn("Hello from Server");

        MvcResult result = mockMvc.perform(get("/prosecutor/test")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("Hello from Server", content);
    }

    @Test
    public void chooseOption() throws Exception {
        final ProsecutorResponseModel response = new ProsecutorResponseModel(5);
        when(prosecutorService.setPrisonersChoice(gameId, prisonerId, any())).thenReturn(response);

        MvcResult result = mockMvc.perform(put("/prosecutor/games/1/prisoners/1")
                .content("{\"choice\":\"C\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numYearsReduction").value(5))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }
}