package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.multipleChoise.MultipleChoiceResponse;
import br.com.alura.AluraFake.task.multipleChoise.NewMultipleChoiceDTO;
import br.com.alura.AluraFake.task.openText.NewOpenTextDTO;
import br.com.alura.AluraFake.task.openText.OpenTextResponse;
import br.com.alura.AluraFake.task.singleChoice.NewSingleChoiceDTO;
import br.com.alura.AluraFake.task.singleChoice.SingleChoiceResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Transactional
    @PostMapping("/new/opentext")
    public ResponseEntity<OpenTextResponse> save(@RequestBody @Valid NewOpenTextDTO openTextDTO) {
        return ResponseEntity.ok((OpenTextResponse) taskService.save(openTextDTO));

    }

    @Transactional
    @PostMapping("/new/singlechoice")
    public ResponseEntity<SingleChoiceResponse> save(@RequestBody @Valid NewSingleChoiceDTO singleDTO) {
        return ResponseEntity.ok((SingleChoiceResponse) taskService.save(singleDTO));
    }

    @Transactional
    @PostMapping("/new/multiplechoice")
    public ResponseEntity<MultipleChoiceResponse> save(@RequestBody @Valid NewMultipleChoiceDTO multipleDTO) {
        return ResponseEntity.ok((MultipleChoiceResponse) taskService.save(multipleDTO));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> listAllTasks() {
        List<TaskResponse> tasks = taskService.findAll();
        return ResponseEntity.ok(tasks);
    }
}
