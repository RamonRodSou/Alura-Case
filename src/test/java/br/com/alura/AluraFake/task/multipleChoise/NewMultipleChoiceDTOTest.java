package br.com.alura.AluraFake.task.multipleChoise;

import br.com.alura.AluraFake.exepctions.TaskException;
import br.com.alura.AluraFake.task.singleChoice.NewSingleChoiceDTO;
import br.com.alura.AluraFake.taskOption.TaskOption;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NewMultipleChoiceDTOTest {

    @Test
    void validateMultipleChoice__should_throw_exception_if_less_than_two_option_is_marked_as_correct() {
        List<TaskOption> options = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            TaskOption op = new TaskOption();
            op.setOption("op-" + i);
            op.setIsCorrect(false);
            options.add(op);
        }

        NewMultipleChoiceDTO dto = new NewMultipleChoiceDTO();
        dto.setStatement("Qual a resposta?");
        dto.setOrder(1);
        dto.setCourseId(1L);
        dto.setOptions(options);

        TaskException exception = assertThrows(TaskException.class, dto::validate);

        assertEquals("Task must have at least two correct option.", exception.getMessage());
    }

    @Test
    void validateMultipleChoice__should_throw_exception_if_have_at_least_option_is_as_incorrect() {
        List<TaskOption> options = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            TaskOption op = new TaskOption();
            op.setOption("op-" + i);
            op.setIsCorrect(true);
            options.add(op);
        }

        NewMultipleChoiceDTO dto = new NewMultipleChoiceDTO();
        dto.setStatement("Qual a resposta?");
        dto.setOrder(1);
        dto.setCourseId(1L);
        dto.setOptions(options);

        TaskException exception = assertThrows(TaskException.class, dto::validate);

        assertEquals("Task must have at least one incorrect option.", exception.getMessage());
    }

    @Test
    void validateMultipleChoice__should_throw_exception_when_option_size_is_less_than_two() {
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
    void validateMultipleChoice__should_throw_exception_when_option_size_is_less_than_five() {
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