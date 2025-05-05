package br.com.alura.AluraFake.task.multipleChoise;

import br.com.alura.AluraFake.task.TaskDTO;
import br.com.alura.AluraFake.task.TaskResponse;
import br.com.alura.AluraFake.task.TaskType;
import br.com.alura.AluraFake.taskOption.TaskOption;

import java.util.List;

public class MultipleChoiceResponse extends TaskResponse {

    private List<TaskOption> options;

    public List<TaskOption> getOptions() {
        return options;
    }

    public void setOptions(List<TaskOption> options) {
        this.options = options;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MULTIPLE_CHOICE;
    }

    @Override
    public TaskDTO toDTO() {
        NewMultipleChoiceDTO dto = new NewMultipleChoiceDTO();
        dto.setStatement(getStatement());
        dto.setOrder(getOrder());
        dto.setCourseId(getCourseId());
        dto.setTaskType(TaskType.MULTIPLE_CHOICE);
        dto.setOptions(getOptions());
        return dto;
    }
}
