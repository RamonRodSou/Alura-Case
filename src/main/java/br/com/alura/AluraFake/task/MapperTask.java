package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.multipleChoise.MultipleChoice;
import br.com.alura.AluraFake.task.multipleChoise.MultipleChoiceResponse;
import br.com.alura.AluraFake.task.openText.OpenText;
import br.com.alura.AluraFake.task.openText.OpenTextResponse;
import br.com.alura.AluraFake.task.singleChoice.SingleChoice;
import br.com.alura.AluraFake.task.singleChoice.SingleChoiceResponse;
import org.springframework.stereotype.Component;

@Component
public class MapperTask {

    public Task map(TaskDTO dto) {
        dto.validate();
        return dto.toEntity();
    }

    public TaskResponse mapResponse(Task entity) {
        if (entity instanceof OpenText openText)
            return toEntityOpenText(openText);

        if (entity instanceof SingleChoice singleChoice)
            return toEntitySingleChoise(singleChoice);

        if (entity instanceof MultipleChoice multipleChoice)
            return toEntityMultipleChoise(multipleChoice);

        throw new IllegalArgumentException("Tipo de tarefa desconhecido: " + entity.getClass());

    }

    private OpenTextResponse toEntityOpenText(OpenText entity) {
        OpenTextResponse openText = new OpenTextResponse();
        taskResponseField(openText, entity);
        return openText;
    }

    private SingleChoiceResponse toEntitySingleChoise(SingleChoice entity) {
        SingleChoiceResponse singleChoice = new SingleChoiceResponse();
        taskResponseField(singleChoice, entity);
        return singleChoice;
    }

    private MultipleChoiceResponse toEntityMultipleChoise(MultipleChoice entity) {
        MultipleChoiceResponse multipleChoice = new MultipleChoiceResponse();
        taskResponseField(multipleChoice, entity);
        return multipleChoice;
    }

    private static void taskResponseField(TaskResponse dto, Task entity) {
        dto.setId(entity.getId());
        dto.setStatement(entity.getStatement());
        dto.setOrder(entity.getOrder());
        dto.setCourseId(entity.getCourseId());
    }

}
