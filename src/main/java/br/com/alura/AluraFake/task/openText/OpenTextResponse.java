package br.com.alura.AluraFake.task.openText;

import br.com.alura.AluraFake.task.TaskDTO;
import br.com.alura.AluraFake.task.TaskResponse;
import br.com.alura.AluraFake.task.TaskType;

public class OpenTextResponse extends TaskResponse {

    @Override
    public TaskType getTaskType() {
        return TaskType.OPEN_TEXT;
    }

    @Override
    public TaskDTO toDTO() {
        NewOpenTextDTO dto = new NewOpenTextDTO();
        dto.setStatement(getStatement());
        dto.setOrder(getOrder());
        dto.setCourseId(getCourseId());
        dto.setTaskType(TaskType.OPEN_TEXT);
        return dto;
    }
}
