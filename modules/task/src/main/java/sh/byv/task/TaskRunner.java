package sh.byv.task;

public interface TaskRunner {

    TaskType getType();

    TaskResult execute(TaskItem task);
}
