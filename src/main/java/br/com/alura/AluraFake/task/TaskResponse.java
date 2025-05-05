package br.com.alura.AluraFake.task;

public abstract class TaskResponse {
    private Long id;
    private String statement;
    private int order;
    private Long courseId;
    private TaskType taskType;

    public abstract TaskDTO toDTO();

    public Long getId() { return id; }
    protected void setId(Long id) { this.id = id; }

    public String getStatement() { return statement; }
    protected void setStatement(String statement) { this.statement = statement; }

    public int getOrder() { return order; }
    protected void setOrder(int order) { this.order = order; }

    public Long getCourseId() { return courseId; }
    protected void setCourseId(Long courseId) { this.courseId = courseId; }

    public abstract TaskType getTaskType();
}
