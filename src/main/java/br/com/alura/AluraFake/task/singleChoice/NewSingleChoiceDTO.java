package br.com.alura.AluraFake.task.singleChoice;

import br.com.alura.AluraFake.task.TaskType;
import br.com.alura.AluraFake.taskOption.TaskOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class NewSingleChoiceDTO {

    @NotBlank
    private String statement;
    @NotNull
    private int order;
    @NotNull
    private Long courseId;
    private TaskType taskType;
    private List<TaskOption> options;

    public SingleChoice toEntity() {
        SingleChoice singleChoice = new SingleChoice();
        singleChoice.setStatement(statement);
        singleChoice.setOrder(order);
        singleChoice.setCourseId(courseId);
        singleChoice.setTaskType(TaskType.SINGLE_CHOICE);
        singleChoice.setOptions(options);
        return singleChoice;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public List<TaskOption> getOptions() {
        return options;
    }

    public void setOptions(List<TaskOption> options) {
        this.options = options;
        for (TaskOption option : options) {
            option.setTask(this.toEntity());
        }
    }
}
