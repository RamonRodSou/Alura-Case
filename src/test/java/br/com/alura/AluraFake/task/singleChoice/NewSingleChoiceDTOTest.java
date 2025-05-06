package br.com.alura.AluraFake.task.singleChoice;

import br.com.alura.AluraFake.exepctions.TaskException;
import br.com.alura.AluraFake.taskOption.TaskOption;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NewSingleChoiceDTOTest {

    @Test
    void validateSingleChoice__should_throw_exception_if_more_than_one_option_is_marked_as_correct() {
        TaskOption op1 = new TaskOption();
        op1.setOption("op-1");
        op1.setIsCorrect(true);

        TaskOption op2 = new TaskOption();
        op2.setOption("op-2");
        op2.setIsCorrect(true);

        TaskOption op3 = new TaskOption();
        op3.setOption("op-3");
        op3.setIsCorrect(false);

        List<TaskOption> options = Arrays.asList(op1, op2, op3);

        NewSingleChoiceDTO dto = new NewSingleChoiceDTO();
        dto.setStatement("Qual a resposta?");
        dto.setOrder(1);
        dto.setCourseId(1L);
        dto.setOptions(options);

        TaskException exception = assertThrows(TaskException.class, dto::validate);
        assertEquals("Task must have exactly one correct option.", exception.getMessage());
    }

    @Test
    void validateSingleChoice__should_throw_exception_when_option_size_is_less_than_two() {
        TaskOption op1 = new TaskOption();
        op1.setOption("op-1");
        op1.setIsCorrect(true);

        List<TaskOption> options = Arrays.asList(op1);

        NewSingleChoiceDTO dto = new NewSingleChoiceDTO();
        dto.setStatement("Qual a resposta?");
        dto.setOrder(1);
        dto.setCourseId(1L);
        dto.setOptions(options);

        TaskException exception = assertThrows(TaskException.class, dto::validate);
        assertEquals("A task must have between 2 and 5 answer options, and exactly one of them must be marked as correct.", exception.getMessage());
    }

    @Test
    void validateSingleChoice__should_throw_exception_when_option_size_is_less_than_five() {
        List<TaskOption> options = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            TaskOption op = new TaskOption();
            op.setOption("op-" + i);
            op.setIsCorrect(i == 1);
            options.add(op);
        }

        NewSingleChoiceDTO dto = new NewSingleChoiceDTO();
        dto.setStatement("Qual a resposta?");
        dto.setOrder(1);
        dto.setCourseId(1L);
        dto.setOptions(options);

        TaskException exception = assertThrows(TaskException.class, dto::validate);
        assertEquals("A task must have between 2 and 5 answer options, and exactly one of them must be marked as correct.", exception.getMessage());
    }
}