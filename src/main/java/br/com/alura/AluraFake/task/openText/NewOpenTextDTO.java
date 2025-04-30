package br.com.alura.AluraFake.task.openText;

import br.com.alura.AluraFake.task.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NewOpenTextDTO {

    @NotBlank
    private String statement;
    @NotNull
    private int order;
    @NotNull
    private Long courseId;
    private TaskType taskType;

    public OpenText toEntity() {
        OpenText openText = new OpenText();
        openText.setStatement(statement);
        openText.setOrder(order);
        openText.setCourseId(courseId);
        openText.setTaskType(TaskType.OPEN_TEXT);
        return openText;
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
}
