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

import java.util.List;

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

        int order = findNextAvailableOrder(task.getCourseId());
        task.setOrder(order);
        validateCourseStatus(task.getCourseId());
        validateStatementDuplicate(task);
        validateTask(task);
        return taskRepository.save(task);
    }

    public int findNextAvailableOrder(Long courseId) {
        List<Integer> order = taskRepository.findTaskOrdersByCourse(courseId);
        for (int i = 1; i <= 5; i++) {
            if (!order.contains(i)) {
                return i;
            }
        }
        throw new IllegalStateException("The course already has 5 tasks assigned.");
    }

    public boolean isCourseBuilding(Long courseId) {
        return courseRepository.findById(courseId)
                .map(course -> course.getStatus().equals(Status.BUILDING))
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id " + courseId));
    }

    public boolean isTaskStatementDuplicate(Task task) {
        return taskRepository.findAll().stream()
                .anyMatch(t -> t.getStatement().equalsIgnoreCase(task.getStatement()));
    }

    private void validateCourseStatus(Long courseId) {
        if (!isCourseBuilding(courseId))
            throw new IllegalStateException("The course must have status BUILDING to add a task.");
    }

    private void validateStatementDuplicate(Task task) {
        if (isTaskStatementDuplicate(task))
            throw new IllegalStateException("Duplicate task statement: a task with this statement already exists.");
    }

    private void validateTask(Task task) {
        if (task instanceof OpenText text) validateOpenText(text);
        else if (task instanceof SingleChoice single) validateSingleChoice(single);
        else if (task instanceof Multiplechoice multiple) validateMultiChoice(multiple);
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
        long correctCount = task.getOptions().stream()
                .filter(TaskOption::isCorrect)
                .count();
        if (correctCount < 1) {
            throw new IllegalArgumentException("Task must have at least one correct option.");
        }

        int alternative = task.getOptions().size();
        if (alternative < 3 || alternative > 5)
            throw new IllegalArgumentException("A task must have between 3 and 5 answer options, and exactly one of them must be marked as correct.");
    }
}
