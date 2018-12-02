package pl.put.poznan.networkanalyzer.controller;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.put.poznan.networkanalyzer.model.Node;
import pl.put.poznan.networkanalyzer.service.NodeService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(NodeController.class)
public class NodeControllerTest {
    private static final String ENDPOINT = "/nodes";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NodeService nodeService;

    @Test
    public void getById_whenFound_returnsThisNode() throws Exception {
        Node node = new Node();
        node.setId(5L);

        when(nodeService.getById(5L)).thenReturn(node);

        mockMvc.perform(get(ENDPOINT + "/5"))
                .andExpect(jsonPath("$.id", Matchers.equalTo(5)));
    }

    @Test
    public void saveAll_whenValid_responds201() throws Exception {
        mockMvc.perform(post(ENDPOINT).content("[]").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void saveAll_whenThrowsException_responds500() throws Exception {
        doThrow(RuntimeException.class).when(nodeService).saveAll(any());
        mockMvc.perform(post(ENDPOINT).content("[]").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void update_whenBodyEmpty_responds500() throws Exception {
        mockMvc.perform(put(ENDPOINT + "/5").content("").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void update_whenInputValid_responds200() throws Exception {
        mockMvc.perform(put(ENDPOINT + "/6").content("{\"id\": 6}").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
}
