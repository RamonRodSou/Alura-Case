package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.task.multipleChoise.Multiplechoice;
import br.com.alura.AluraFake.task.openText.OpenText;
import br.com.alura.AluraFake.task.singleChoice.SingleChoice;
import br.com.alura.AluraFake.taskOption.TaskOption;
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

        if (task instanceof OpenText text) validateOpenText(text);
        else if (task instanceof SingleChoice single) validateSingleChoice(single);
        else if (task instanceof Multiplechoice multiple) validateMultiChoice(multiple);
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

    private void validateOpenText(OpenText task) {
        String statement = task.getStatement();

        if (statement.length() < 4 | statement.length() > 255) {
            throw new IllegalArgumentException("The statement must be between 5 and 255 characters.");
        }
    }

    private void validateSingleChoice(SingleChoice task) {
        long correctCount = task.getOptions().stream()
                .filter(TaskOption::isCorrect)
                .count();
        if (correctCount != 1) {
            throw new IllegalArgumentException("Task must have exactly one correct option.");
        }
    }

    private void validateMultiChoice(Multiplechoice task) {
        long correctCount = task.getOptions().stream()
                .filter(TaskOption::isCorrect)
                .count();
        if (correctCount < 1) {
            throw new IllegalArgumentException("Task must have at least one correct option.");
        }
    }
}
