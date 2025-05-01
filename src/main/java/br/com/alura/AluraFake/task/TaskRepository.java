package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.taskOption.TaskOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCourseId(Long courseId);

    boolean existsByStatementIgnoreCaseAndCourseId(String statement, Long courseId);
}
