package br.com.alura.AluraFake.task.multipleChoise;

import br.com.alura.AluraFake.task.Task;
import br.com.alura.AluraFake.task.TaskType;
import br.com.alura.AluraFake.taskOption.TaskOption;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
public class MultipleChoice extends Task {

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<TaskOption> options;

    public MultipleChoice() {
        super();
    }

    public MultipleChoice(Long id, String statement, int order, Long courseId, List<TaskOption> option, TaskType taskType) {
        super(id, statement, order, courseId, taskType);
        this.options = option;
    }

    public List<TaskOption> getOptions() {
        return options;
    }

    public void setOptions(List<TaskOption> options) {
        this.options = options;
        if (options != null) {
            options.forEach(opt -> opt.setTask(this));
        }
    }
}
