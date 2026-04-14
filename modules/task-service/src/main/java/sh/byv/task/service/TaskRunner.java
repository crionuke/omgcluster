package sh.byv.task.service;

public interface TaskRunner {

    TaskType getType();

    TaskResult execute(TaskItem task);
}
