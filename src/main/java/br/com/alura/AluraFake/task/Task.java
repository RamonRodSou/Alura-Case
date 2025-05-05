package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.multipleChoise.MultipleChoice;
import br.com.alura.AluraFake.task.openText.OpenText;
import br.com.alura.AluraFake.task.singleChoice.SingleChoice;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;

@JsonSubTypes({
        @JsonSubTypes.Type(value = OpenText.class, name = "OPEN_TEXT"),
        @JsonSubTypes.Type(value = SingleChoice.class, name = "SINGLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoice.class, name = "MULTIPLE_CHOICE")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "task_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String statement;
    @Column(name = "task_order")
    private int order;
    @Column(name = "course_id")
    private Long courseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", insertable = false, updatable = false)
    private TaskType taskType;

    public Task() { }

    public Task(Long id, String statement, int order, Long courseId, TaskType taskType) {
        this.id = id;
        this.statement = statement;
        this.order = order;
        this.courseId = courseId;
        this.taskType = taskType;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
}
