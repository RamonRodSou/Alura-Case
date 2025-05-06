package br.com.alura.AluraFake.task.openText;

import br.com.alura.AluraFake.exepctions.TaskException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NewOpenTextDTOTest {

    @Test
    void validateOpenText__should_throw_exception_if_stattement_length_is_less_than_4() {
        NewOpenTextDTO openText = new NewOpenTextDTO();
        openText.setStatement("ab");;

        TaskException exception = assertThrows(TaskException.class, openText::validate);
        assertEquals("The statement must be between 4 and 255 characters.", exception.getMessage());
    }

    @Test
    void validateOpenText__should_throw_exception_if_stattement_length_is_more_than_255() {
        StringBuilder maxCharacter = new StringBuilder("A");

        for(int i = 0;  i < 255; i++) {
            maxCharacter.append("a");
        }

        NewOpenTextDTO openText = new NewOpenTextDTO();
        openText.setStatement(maxCharacter.toString());

        TaskException exception = assertThrows(TaskException.class, openText::validate);
        assertEquals("The statement must be between 4 and 255 characters.", exception.getMessage());
    }

}