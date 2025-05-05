package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.exepctions.TaskException;
import br.com.alura.AluraFake.taskOption.TaskOption;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class TaskDTO {

    private Long id;
    private String statement;
    private int order;
    private Long courseId;
    private TaskType taskType;

    public abstract Task toEntity();
    public abstract void validate();

    public Long getId() {
        return id;
    }

    public String getStatement() {
        return statement;
    }

    public int getOrder() {
        return order;
    }

    public Long getCourseId() {
        return courseId;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    protected static void validateChoiceOptions(String statement, List<TaskOption> options) {
        validateDuplicateOption(options);
        validateOptionEqualsStatement(statement, options);
    }

    private static void validateDuplicateOption(List<TaskOption> options) {
        Set<String> seen = new HashSet<>();
        for (TaskOption option : options) {
            String normalized = option.getOption().trim().toUpperCase();
            if (!seen.add(normalized))
                throw TaskException.badRequest("Duplicate option found: " + option.getOption());
        }
    }

    private static void validateOptionEqualsStatement(String statement, List<TaskOption> options) {
        List<String> optionNames = options
                .stream()
                .map(TaskOption::getOption)
                .toList();

        if (optionNames.contains(statement))
            throw TaskException.badRequest("An option cannot have the same text as the task statement.");
    }
}
