package br.com.alura.AluraFake.task.singleChoice;

import br.com.alura.AluraFake.task.TaskDTO;
import br.com.alura.AluraFake.task.TaskResponse;
import br.com.alura.AluraFake.task.TaskType;
import br.com.alura.AluraFake.taskOption.TaskOption;

import java.util.List;

public class SingleChoiceResponse extends TaskResponse {

    private List<TaskOption> options;

    public List<TaskOption> getOptions() {
        return options;
    }

    public void setOptions(List<TaskOption> options) {
        this.options = options;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SINGLE_CHOICE;
    }

    @Override
    public TaskDTO toDTO() {
        NewSingleChoiceDTO dto = new NewSingleChoiceDTO();
        dto.setStatement(getStatement());
        dto.setOrder(getOrder());
        dto.setCourseId(getCourseId());
        dto.setTaskType(TaskType.SINGLE_CHOICE);
        dto.setOptions(getOptions());
        return dto;
    }
}
