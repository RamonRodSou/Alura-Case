package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.multipleChoise.Multiplechoice;
import br.com.alura.AluraFake.task.multipleChoise.NewMultiplechoiceDTO;
import br.com.alura.AluraFake.task.openText.NewOpenTextDTO;
import br.com.alura.AluraFake.task.openText.OpenText;
import br.com.alura.AluraFake.task.singleChoice.SingleChoice;
import br.com.alura.AluraFake.task.singleChoice.NewSingleChoiceDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/task/new/opentext")
    public ResponseEntity<OpenText> save(@RequestBody @Valid NewOpenTextDTO taskDTO) {
        OpenText openText = taskDTO.toEntity();
        return ResponseEntity.ok((OpenText) taskService.save(openText));
    }

    @PostMapping("/task/new/singlechoice")
    public ResponseEntity<SingleChoice> save(@RequestBody @Valid NewSingleChoiceDTO taskDTO) {
        SingleChoice singleChoice = taskDTO.toEntity();
        return ResponseEntity.ok((SingleChoice) taskService.save(singleChoice));
    }

    @PostMapping("/task/new/multiplechoice")
    public ResponseEntity<Multiplechoice> save(@RequestBody @Valid NewMultiplechoiceDTO taskDTO) {
        Multiplechoice  multiplechoice = taskDTO.toEntity();
        return ResponseEntity.ok((Multiplechoice) taskService.save(multiplechoice));
    }
}
