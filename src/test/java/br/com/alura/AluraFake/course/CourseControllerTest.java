package br.com.alura.AluraFake.course;
import br.com.alura.AluraFake.task.Task;
import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.task.TaskType;
import br.com.alura.AluraFake.task.multipleChoise.MultipleChoice;
import br.com.alura.AluraFake.task.openText.OpenText;
import br.com.alura.AluraFake.task.singleChoice.SingleChoice;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import br.com.alura.AluraFake.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void newCourseDTO__should_return_bad_request_when_email_is_invalid() throws Exception {

        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("paulo@alura.com.br");
        newCourseDTO.setStatus(Status.BUILDING);

        doReturn(Optional.empty()).when(userRepository)
                .findByEmail(newCourseDTO.getEmailInstructor());

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("emailInstructor"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void newCourseDTO__should_return_bad_request_when_email_is_no_instructor() throws Exception {

        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("paulo@alura.com.br");

        User user = mock(User.class);
        doReturn(false).when(user).isInstructor();

        doReturn(Optional.of(user)).when(userRepository)
                .findByEmail(newCourseDTO.getEmailInstructor());

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("emailInstructor"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void newCourseDTO__should_return_created_when_new_course_request_is_valid() throws Exception {

        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("paulo@alura.com.br");

        User user = mock(User.class);
        doReturn(true).when(user).isInstructor();

        doReturn(Optional.of(user)).when(userRepository).findByEmail(newCourseDTO.getEmailInstructor());

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isCreated());

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void listAllCourses__should_list_all_courses() throws Exception {
        User paulo = new User("Paulo", "paulo@alua.com.br", Role.INSTRUCTOR);

        Course java = new Course("Java", "Curso de java", paulo);
        Course hibernate = new Course("Hibernate", "Curso de hibernate", paulo);
        Course spring = new Course("Spring", "Curso de spring", paulo);

        when(courseRepository.findAll()).thenReturn(Arrays.asList(java, hibernate, spring));

        mockMvc.perform(get("/course/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java"))
                .andExpect(jsonPath("$[0].description").value("Curso de java"))
                .andExpect(jsonPath("$[1].title").value("Hibernate"))
                .andExpect(jsonPath("$[1].description").value("Curso de hibernate"))
                .andExpect(jsonPath("$[2].title").value("Spring"))
                .andExpect(jsonPath("$[2].description").value("Curso de spring"));
    }

    @Test
    void publishCourse_should_return_bad_request_when_course_not_found() throws Exception {
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/course/{id}/publish", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Course not found with id " + courseId));
    }

    @Test
    void publishCourse_should_return_bad_request_when_no_tasks_found() throws Exception {
        Long courseId = 1L;
        Course course = new Course("Java", "Curso de Java", new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR));
        course.setId(courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseIdOrderByOrderAsc(courseId)).thenReturn(List.of());

        mockMvc.perform(post("/course/{id}/publish", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.message").value("No tasks found for course with id " + courseId));
    }


    @Test
    void publishCourse_should_return_bad_request_when_task_types_are_incomplete() throws Exception {
        Long courseId = 1L;
        Course course = new Course("Java", "Curso de Java", new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR));
        course.setId(courseId);

        Task task = new OpenText();
        task.setStatement("Task 1");
        task.setTaskType(TaskType.OPEN_TEXT);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseIdOrderByOrderAsc(courseId)).thenReturn(Arrays.asList(task));

        mockMvc.perform(post("/course/{id}/publish", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("The course must have at least one of each type of statement"));

    }

    @Test
    void publishCourse_should_return_bad_request_when_course_is_already_published() throws Exception {
        Long courseId = 1L;

        Course course = new Course("Java", "Curso de Java", new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR));
        course.setId(courseId);
        course.publish();

        Task open = new OpenText();
        open.setStatement("Task 1");
        open.setTaskType(TaskType.OPEN_TEXT);
        open.setCourseId(courseId);

        Task single = new SingleChoice();
        single.setStatement("Task 2");
        single.setTaskType(TaskType.SINGLE_CHOICE);
        single.setCourseId(courseId);

        Task multiple = new MultipleChoice();
        multiple.setStatement("Task 3");
        multiple.setTaskType(TaskType.MULTIPLE_CHOICE);
        multiple.setCourseId(courseId);

        List<Task> tasks = Arrays.asList(open, single, multiple);
        when(taskRepository.findByCourseIdOrderByOrderAsc(courseId)).thenReturn(tasks);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);

        mockMvc.perform(post("/course/{id}/publish", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The course must have status: " + Status.BUILDING));
    }

    @Test
    void publishCourse_should_publish_course_successfully() throws Exception {
        Long courseId = 1L;
        Course course = new Course("Java", "Curso de Java", new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR));
        course.setId(courseId);

        Task open = new OpenText();
        open.setStatement("Task 1");
        open.setTaskType(TaskType.OPEN_TEXT);
        open.setCourseId(courseId);

        Task single = new SingleChoice();
        single.setStatement("Task 2");
        single.setTaskType(TaskType.SINGLE_CHOICE);
        single.setCourseId(courseId);

        Task multiple = new MultipleChoice();
        multiple.setStatement("Task 3");
        multiple.setTaskType(TaskType.MULTIPLE_CHOICE);
        multiple.setCourseId(courseId);

        List<Task> tasks = Arrays.asList(open, single, multiple);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseIdOrderByOrderAsc(courseId)).thenReturn(tasks);

        mockMvc.perform(post("/course/{id}/publish", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publishedAt").isNotEmpty());

        verify(courseRepository, times(1)).save(course);
    }

}