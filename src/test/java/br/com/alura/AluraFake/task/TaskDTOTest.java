package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.exepctions.TaskException;
import br.com.alura.AluraFake.taskOption.TaskOption;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskDTOTest {

    @Test
    void validateChoiceOptions__should_throw_exception_if_duplicate_option_exists() {
        TaskOption op1 = new TaskOption();
        op1.setOption("op-1");

        TaskOption op2 = new TaskOption();
        op2.setOption("op-1");

        List<TaskOption> options = Arrays.asList(op1, op2);

        TaskException exception = assertThrows(TaskException.class, () -> {
            TaskDTO.validateChoiceOptions("Test", options);
        });
        assertEquals("Duplicate option found: " + op1.getOption(), exception.getMessage());
    }

    @Test
    void validateOptionEqualsStatement__should_throw_exception_if_statement_is_equal() {
        TaskOption op1 = new TaskOption();
        op1.setOption("op-1");

        TaskOption op2 = new TaskOption();
        op2.setOption("op-2");
        List<TaskOption> opt = Arrays.asList(op1, op2);

        TaskException exception = assertThrows(TaskException.class, () -> {
            TaskDTO.validateChoiceOptions("op-1", opt);
        });
        assertEquals("An option cannot have the same text as the task statement.", exception.getMessage());
    }

}