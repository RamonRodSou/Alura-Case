package br.com.alura.AluraFake.task.multipleChoise;

import br.com.alura.AluraFake.exepctions.TaskException;
import br.com.alura.AluraFake.task.TaskDTO;
import br.com.alura.AluraFake.task.TaskType;
import br.com.alura.AluraFake.taskOption.TaskOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class NewMultipleChoiceDTO extends TaskDTO {

    @NotBlank
    private String statement;
    @NotNull
    private int order;
    @NotNull
    private Long courseId;
    private TaskType taskType;
    private List<TaskOption> options;

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

    @Override
    public MultipleChoice toEntity() {
        MultipleChoice multiplechoice = new MultipleChoice();
        multiplechoice.setStatement(statement);
        multiplechoice.setOrder(order);
        multiplechoice.setCourseId(courseId);
        multiplechoice.setTaskType(TaskType.MULTIPLE_CHOICE);
        multiplechoice.setOptions(options);
        return multiplechoice;
    }

    @Override
    public void validate() {
        validateMultipleChoice();
        TaskDTO.validateChoiceOptions(this.getStatement(), this.getOptions());
    }

    private void validateMultipleChoice() {
        int optionCount = this.getOptions().size();

        long correctCount = this.getOptions().stream()
                .filter(TaskOption::isCorrect)
                .count();
        if (correctCount < 2)
            throw TaskException.badRequest("Task must have at least two correct option.");

        if (correctCount == optionCount)
            throw TaskException.badRequest("Task must have at least one incorrect option.");

        if (optionCount < 3 || optionCount > 5)
            throw TaskException.badRequest("A task must have between 3 and 5 answer options, and exactly one of them must be marked as correct.");
    }

}
