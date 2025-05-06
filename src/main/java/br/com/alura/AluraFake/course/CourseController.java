package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.exepctions.CourseException;
import br.com.alura.AluraFake.exepctions.TaskException;
import br.com.alura.AluraFake.task.Task;
import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.task.TaskType;
import br.com.alura.AluraFake.user.User;
import br.com.alura.AluraFake.user.UserRepository;
import br.com.alura.AluraFake.util.ErrorItemDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository, UserRepository userRepository, TaskRepository taskRepository){
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {

        //Caso implemente o bonus, pegue o instrutor logado
        Optional<User> possibleAuthor = userRepository
                .findByEmail(newCourse.getEmailInstructor())
                .filter(User::isInstructor);

        if(possibleAuthor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("emailInstructor", "Usuário não é um instrutor"));
        }

        Course course = new Course(newCourse.getTitle(), newCourse.getDescription(), possibleAuthor.get());

        courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/course/all")
    public ResponseEntity<List<CourseListItemDTO>> listAllCourses() {
        List<CourseListItemDTO> courses = courseRepository.findAll().stream()
                .map(CourseListItemDTO::new)
                .toList();
        return ResponseEntity.ok(courses);
    }

    @Transactional
    @PostMapping("/course/{id}/publish")
    public ResponseEntity<CourseListItemDTO> publishCourse(@PathVariable("id") Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> CourseException.notFound("Course not found with id " + id));

        validateCourse(course);

        course.publish();
        courseRepository.save(course);
        return ResponseEntity.ok(new CourseListItemDTO(course));
    }

    private void validateCourse(Course course) {
        validateTask(course);
        validateCourseStatus(course);
    }

    private void validateTask(Course course) {
        List<Task> task = taskRepository.findByCourseIdOrderByOrderAsc(course.getId());
        if (task.isEmpty())
            throw TaskException.notFound("No tasks found for course with id " + course.getId());

        validateTaskTypes(task);
    }

    private void validateTaskTypes(List<Task> task) {
        List<TaskType> taskType = task
                .stream()
                .map(Task::getTaskType)
                .toList();

        if (!hasAllRequiredTaskTypes(task))
            throw CourseException.notFound("The course must have at least one of each type of statement");
    }

    private void validateCourseStatus(Course course) {
        if (course.getStatus().equals(Status.PUBLISHED))
             throw CourseException.bedRequest("The course must have status: " + Status.BUILDING);
    }

    private boolean hasAllRequiredTaskTypes(List<Task> task) {
        List<TaskType> types = task.stream().map(Task::getTaskType).toList();
        return new HashSet<>(types).containsAll(List.of(TaskType.OPEN_TEXT, TaskType.SINGLE_CHOICE, TaskType.MULTIPLE_CHOICE));
    }
}
