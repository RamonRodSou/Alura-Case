package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.task.multipleChoise.Multiplechoice;
import br.com.alura.AluraFake.task.openText.OpenText;
import br.com.alura.AluraFake.task.singleChoice.SingleChoice;
import br.com.alura.AluraFake.taskOption.TaskOption;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public TaskService (TaskRepository taskRepository, CourseRepository courseRepository) {
        this.taskRepository = taskRepository;
        this.courseRepository = courseRepository;
    }

    public Task save(Task task) {
        validateTask(task);
        return taskRepository.save(task);
    }

    public void reorganizeOrder(Task task) {
        List<Task> tasks = taskRepository.findByCourseIdOrderByOrderAsc(task.getCourseId());

        boolean isTaskOrder = tasks
                .stream()
                .anyMatch(t -> t.getOrder() >= task.getOrder());

        if (tasks.size() >= 5 && isTaskOrder) {
            throw new IllegalStateException("The course already has 5 tasks. It's not possible to add a new one with this order.");
        }

        for (int i = 1; i < task.getOrder(); i++) {
            final int current = i;
            boolean exists = tasks.stream().anyMatch(t -> t.getOrder() == current);
            if (!exists) {
                throw new IllegalArgumentException(
                        String.format("Invalid task order. You must add order %d before adding order %d.", current, task.getOrder()));
            }
        }

        for (int i = tasks.size() - 1; i >= 0; i--) {
            Task existing = tasks.get(i);
            if (existing.getOrder() >= task.getOrder()) {
                if (existing.getOrder() == 5) {
                    throw new IllegalStateException("Cannot shift tasks. Maximum task order (5) would be exceeded.");
                }
                existing.setOrder(existing.getOrder() + 1);
            }
        }

        taskRepository.saveAll(tasks);
    }

    private void validateTask(Task task) {
        validateCourseStatus(task.getCourseId());
        validateStatementDuplicate(task);
        reorganizeOrder(task);

        if (task instanceof OpenText text) {
            validateOpenText(text);
            return;
        }

        if (task instanceof SingleChoice single) {
            validateSingleChoice(single);
            validateChoiceOptions(single.getOptions(), task);
            return;
        }

        if (task instanceof Multiplechoice multiple) {
            validateMultiChoice(multiple);
            validateChoiceOptions(multiple.getOptions(), task);
        }
    }

    private void validateChoiceOptions(List<TaskOption> options, Task task) {
        validateDuplicateOption(options);
        validateOptionEqualsStatement(options, task);
    }

    private void validateCourseStatus(Long courseId) {
        if (!isCourseBuilding(courseId))
            throw new IllegalStateException("The course must have status BUILDING to add a task.");
    }

    private void validateStatementDuplicate(Task task) {
        if (isTaskStatementDuplicate(task))
            throw new IllegalStateException("Duplicate task statement: a task with this statement already exists.");
    }

    private void validateOpenText(OpenText task) {
        String statement = task.getStatement();

        if (statement.length() < 4 | statement.length() > 255)
            throw new IllegalArgumentException("The statement must be between 4 and 255 characters.");
    }

    private void validateSingleChoice(SingleChoice task) {
        long correctCount = task.getOptions().stream()
                .filter(TaskOption::isCorrect)
                .count();
        if (correctCount != 1)
            throw new IllegalArgumentException("Task must have exactly one correct option.");

        int taskOptionSize = task.getOptions().size();
        if (taskOptionSize < 2 || taskOptionSize > 5)
            throw new IllegalArgumentException("A task must have between 2 and 5 answer options, and exactly one of them must be marked as correct.");
    }

    private void validateMultiChoice(Multiplechoice task) {
        int optionCount = task.getOptions().size();

        long correctCount = task.getOptions().stream()
                .filter(TaskOption::isCorrect)
                .count();
        if (correctCount < 2)
            throw new IllegalArgumentException("Task must have at least two correct option.");

        if (correctCount == optionCount)
            throw new IllegalArgumentException("Task must have at least one incorrect option.");

        if (optionCount < 3 || optionCount > 5)
            throw new IllegalArgumentException("A task must have between 3 and 5 answer options, and exactly one of them must be marked as correct.");
    }

    private void validateDuplicateOption(List<TaskOption> options) {
        Set<String> seen = new HashSet<>();
        for (TaskOption option : options) {
            String normalized = option.getOption()
                    .trim()
                    .toUpperCase();
            if (!seen.add(normalized))
                throw new IllegalArgumentException("Duplicate option found: " + option.getOption());
        }
    }

    private void validateOptionEqualsStatement(List<TaskOption> options, Task task) {
        List<String> optionName = options
                .stream()
                .map(TaskOption::getOption )
                .toList();

        if (optionName.contains(task.getStatement()))
            throw new IllegalArgumentException("An option cannot have the same text as the task statement.");
    }

    public boolean isCourseBuilding(Long courseId) {
        return courseRepository.findById(courseId)
                .map(course -> course.getStatus().equals(Status.BUILDING))
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id " + courseId));
    }

    public boolean isTaskStatementDuplicate(Task task) {
        return taskRepository.existsByStatementIgnoreCaseAndCourseId(
                task.getStatement(),
                task.getCourseId()
        );
    }
}
