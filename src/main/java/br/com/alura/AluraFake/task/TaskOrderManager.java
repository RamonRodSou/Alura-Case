package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.exepctions.TaskException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskOrderManager {

    public void reoganizeOrder(Task task, List<Task> tasks){
        validateMaxTasksNotExceeded(task, tasks);
        incrementOrdersFromTask(task, tasks);
    }

    private void validateMaxTasksNotExceeded(Task task, List<Task> tasks) {
        boolean isTaskOrder = tasks
                .stream()
                .anyMatch(t -> t.getOrder() >= task.getOrder());

        if (tasks.size() >= 5 && isTaskOrder)
            throw TaskException.badRequest("The course already has 5 tasks. It's not possible to add a new one with this order.");

        for (int i = 1; i < task.getOrder(); i++) {
            final int current = i;
            boolean exists = tasks
                    .stream()
                    .anyMatch(t -> t.getOrder() == current);

            if (!exists)
                throw TaskException.badRequest(
                        String.format("Invalid task order. You must add order %d before adding order %d.", current, task.getOrder()));
        }
    }

    private void incrementOrdersFromTask(Task task, List<Task> tasks) {
        for (int i = tasks.size() - 1; i >= 0; i--) {
            Task previousTask = tasks.get(i);
            if (previousTask.getOrder() >= task.getOrder()) {
                if (previousTask.getOrder() == 5) {
                    throw TaskException.badRequest("Cannot shift tasks. Maximum task order (5) would be exceeded.");
                }
                previousTask.setOrder(previousTask.getOrder() + 1);
            }
        }
    }
}
