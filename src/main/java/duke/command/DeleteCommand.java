package duke.command;

import duke.exception.DukeException;
import duke.storage.Storage;
import duke.task.Task;
import duke.task.TaskList;
import duke.ui.Ui;

public class DeleteCommand extends Command {
    private int taskNumber;
    
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        try {
            Task task = tasks.deleteTask(this.taskNumber - 1);
            storage.save(tasks);
            ui.showDeleteMessage(task, tasks.getSize());
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException(ui.getMissingTaskError(CommandWord.DELETE, this.taskNumber));
        }
    }
    
    @Override
    public boolean isExit() {
        return false;
    }
}