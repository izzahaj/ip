package mort.command;

import mort.exception.MortException;
import mort.storage.Storage;
import mort.task.TaskList;
import mort.ui.Ui;

/**
 * Represents a command to mark a task in the task list as complete.
 */
public class MarkCommand extends Command {
    /** Index of task to be marked as complete */
    private int taskNumber;
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws MortException {
        try {
            String msg = tasks.markTask(this.taskNumber - 1);
            storage.save(tasks);
            return msg; //abstract away to Ui class
        } catch (IndexOutOfBoundsException e) {
            throw new MortException(ui.getMissingTaskError(CommandWord.MARK, this.taskNumber));
        }
    }
    @Override
    public boolean isExit() {
        return false;
    }
}