package br.com.alura.AluraFake.taskOption;

import br.com.alura.AluraFake.task.Task;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
public class TaskOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "option_name", nullable = false)
    @Size(min = 4, max = 80)
    String option;

    boolean isCorrect;

    @ManyToOne
    @JsonBackReference
    private Task task;

    public TaskOption() { }

    public TaskOption(Long id, String option, boolean isCorrect, Task task) {
        this.id = id;
        this.option = option;
        this.isCorrect = isCorrect;
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean correct) {
        this.isCorrect = correct;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
