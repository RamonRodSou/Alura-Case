package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.exepctions.CourseException;
import br.com.alura.AluraFake.exepctions.TaskException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CourseRepository courseRepository;
    private final MapperTask mapper;
    private final TaskOrderManager taskOrderManager;

    @Autowired
    public TaskService (TaskRepository taskRepository, CourseRepository courseRepository, MapperTask mapper, TaskOrderManager taskOrderManager) {
        this.taskRepository = taskRepository;
        this.courseRepository = courseRepository;
        this.mapper = mapper;
        this.taskOrderManager = taskOrderManager;
    }

    public TaskResponse save(TaskDTO dto) {
        dto.validate();
        validateTask(dto);
        Task task = mapper.map(dto);
        Task saved = taskRepository.save(task);
        return mapper.mapResponse(saved);
    }

    public List<TaskResponse> findAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(mapper::mapResponse)
                .collect(Collectors.toList());    }

    private void validateTask(TaskDTO dto) {
        validateCourseStatus(dto.getCourseId());
        validateStatementDuplicate(dto);
        dto.validate();
        reorganizeOrder(dto.toEntity());
    }

    private void validateCourseStatus(Long courseId) {
        if (!isCourseBuilding(courseId))
            throw TaskException.badRequest("The course must have status " + Status.BUILDING + " to add a task.");
    }

    private void validateStatementDuplicate(TaskDTO dto) {
        if (isTaskStatementDuplicate(dto.toEntity()))
            throw TaskException.badRequest("Duplicate task statement: a task with this statement already exists.");
    }

    private void reorganizeOrder(Task task) {
        List<Task> tasks = taskRepository.findByCourseIdOrderByOrderAsc(task.getCourseId());
        taskOrderManager.reoganizeOrder(task, tasks);
        taskRepository.saveAll(tasks);
    }

    public boolean isCourseBuilding(Long courseId) {
        return courseRepository.findById(courseId)
                .map(course -> course.getStatus().equals(Status.BUILDING))
                .orElseThrow(() -> CourseException.notFound("Course not found with id " + courseId));
    }

    public boolean isTaskStatementDuplicate(Task task) {
        return taskRepository.existsByStatementIgnoreCaseAndCourseId(
                task.getStatement(),
                task.getCourseId()
        );
    }
}
