package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.multipleChoise.Multiplechoice;
import br.com.alura.AluraFake.task.multipleChoise.NewMultiplechoiceDTO;
import br.com.alura.AluraFake.task.openText.NewOpenTextDTO;
import br.com.alura.AluraFake.task.openText.OpenText;
import br.com.alura.AluraFake.task.singleChoice.NewSingleChoiceDTO;
import br.com.alura.AluraFake.task.singleChoice.SingleChoice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Transactional
    @PostMapping("/task/new/opentext")
    public ResponseEntity<OpenText> save(@RequestBody @Valid NewOpenTextDTO taskDTO) {
        OpenText openText = taskDTO.toEntity();
        return ResponseEntity.ok((OpenText) taskService.save(openText));
    }

    @Transactional
    @PostMapping("/task/new/singlechoice")
    public ResponseEntity<SingleChoice> save(@RequestBody @Valid NewSingleChoiceDTO taskDTO) {
        SingleChoice singleChoice = taskDTO.toEntity();
        return ResponseEntity.ok((SingleChoice) taskService.save(singleChoice));
    }

    @Transactional
    @PostMapping("/task/new/multiplechoice")
    public ResponseEntity<Multiplechoice> save(@RequestBody @Valid NewMultiplechoiceDTO taskDTO) {
        Multiplechoice  multiplechoice = taskDTO.toEntity();
        return ResponseEntity.ok((Multiplechoice) taskService.save(multiplechoice));
    }
}
