package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.multipleChoise.NewMultipleChoiceDTO;
import br.com.alura.AluraFake.task.openText.NewOpenTextDTO;
import br.com.alura.AluraFake.task.openText.OpenTextResponse;
import br.com.alura.AluraFake.task.singleChoice.NewSingleChoiceDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveOpenText__should_return_200()  throws Exception{
        NewOpenTextDTO dto = new NewOpenTextDTO();
        dto.setStatement("Fazendo teste do OpenText.");
        dto.setCourseId(1L);

        OpenTextResponse response = new OpenTextResponse();
        response.setStatement("Fazendo teste do OpenText.");

        when(taskService.save(dto)).thenReturn(response);

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void saveSingleChoice__should_return_200() throws Exception {
        NewSingleChoiceDTO dto = new NewSingleChoiceDTO();
        dto.setStatement("Teste");
        dto.setCourseId(1L);
        dto.setOrder(1);
        dto.setOptions(List.of());

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void saveMultipleChoice__should_return_200() throws Exception {
        NewMultipleChoiceDTO dto = new NewMultipleChoiceDTO();
        dto.setStatement("Teste");
        dto.setCourseId(1L);
        dto.setOrder(1);
        dto.setOptions(List.of());

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}