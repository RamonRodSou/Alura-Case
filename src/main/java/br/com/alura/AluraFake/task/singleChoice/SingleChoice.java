package br.com.alura.AluraFake.task.singleChoice;

import br.com.alura.AluraFake.task.Task;
import br.com.alura.AluraFake.task.TaskType;
import br.com.alura.AluraFake.taskOption.TaskOption;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
@DiscriminatorValue("SINGLE_CHOICE")
public class SingleChoice extends Task {

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    List<TaskOption> options;

    public SingleChoice() {
        super();
    }

    public SingleChoice(Long id, String statement, int order, Long courseId, List<TaskOption> option, TaskType taskType) {
        super(id, statement, order, courseId, taskType );
        this.options = option;
    }

    public List<TaskOption> getOptions() {
        return options;
    }

    public void setOptions(List<TaskOption> options) {
        this.options = options;
        for (TaskOption option : options) {
            option.setTask(this);
        }
    }
}
