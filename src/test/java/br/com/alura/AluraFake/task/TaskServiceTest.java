package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.exepctions.CourseException;
import br.com.alura.AluraFake.exepctions.TaskException;
import br.com.alura.AluraFake.task.openText.NewOpenTextDTO;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private MapperTask mapper;

    @MockBean
    private TaskOrderManager taskOrderManager;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        courseRepository = mock(CourseRepository.class);
        mapper = mock(MapperTask.class);
        taskOrderManager = mock(TaskOrderManager.class);
        taskService = new TaskService(taskRepository, courseRepository, mapper, taskOrderManager);
    }

    @Test
    void newTask__should_throw_exception_if_course_not_found() {
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
        CourseException ex = assertThrows(CourseException.class, () -> {
            taskService.isCourseBuilding(courseId);
        });
        assertEquals("Course not found with id 1", ex.getMessage());
    }

    @Test
    void newTask__should_return_false_if_course_status_is_not_building() {
        Long courseId = 1L;
        Course course = new Course();
        course.publish();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        boolean result = taskService.isCourseBuilding(courseId);
        assertFalse(result);
    }

    @Test
    void newTask__should_return_true_if_course_status_is_building() {
        Long courseId = 1L;
        User paulo = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Aprenda Java com Alura", paulo);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        boolean result = taskService.isCourseBuilding(courseId);
        assertTrue(result);
    }

    @Test
    void save__should_throw_exception_if_task_statement_is_duplicate() {
        User paulo = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Aprenda Java com Alura", paulo);
        NewOpenTextDTO dto = new NewOpenTextDTO();
        dto.setStatement("Duplicada");
        dto.setCourseId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.existsByStatementIgnoreCaseAndCourseId("Duplicada", 1L)).thenReturn(true);

        TaskException exception = assertThrows(TaskException.class, () -> {
            taskService.save(dto);
        });
        assertEquals("Duplicate task statement: a task with this statement already exists.", exception.getMessage());
    }
}
