package mort.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import mort.command.AddCommand;
import mort.command.Command;
import mort.command.CommandWord;
import mort.command.DeleteCommand;
import mort.command.ExitCommand;
import mort.command.FindCommand;
import mort.command.ListCommand;
import mort.command.MarkCommand;
import mort.command.UnmarkCommand;
import mort.exception.MortException;
import mort.task.TaskType;
import mort.ui.Ui;

/**
 * Parser for parsing user inputs, dates and tasks.
 */
public class Parser {
    /**
     * Parses and validates user input and returns a command to be executed.
     * @param fullCommand The command input by the user.
     * @return The Command to be executed.
     * @throws MortException If command is invalid.
     */
    public static Command parse(String fullCommand) throws MortException {
        String strippedCommand = fullCommand.stripTrailing();
        String[] parsedCommand = strippedCommand.split(" ", 2);
        try {
            switch (parsedCommand[0]) {
            case "":
                throw new MortException("Somehow you're even more unbearable when you're silent.");
            case "todo":
                return validateToDo(parsedCommand);
            case "deadline":
                return validateDeadline(parsedCommand);
            case "event":
                return validateEvent(parsedCommand);
            case "delete":
                return validateDelete(parsedCommand);
            case "mark":
                return validateMark(parsedCommand);
            case "unmark":
                return validateUnmark(parsedCommand);
            case "find":
                return validateFind(parsedCommand);
            case "list":
                return validateList(strippedCommand);
            case "bye":
                return validateBye(strippedCommand);
            default:
                throw new MortException(Ui.getUnknownCommandMessage(strippedCommand));
            }
        } catch (NumberFormatException e) {
            throw new MortException("Do you need me to teach you what a number is?\n"
                    + "'" + parsedCommand[1] + "' is not a number.");
        }
    }

    /**
     * Converts a given text string to a LocalDateTime object.
     *
     * @param dateTime The text string with the format dd/M/yyyy HHmm.
     * @return The LocalDateTime object representing the date-time given in the string.
     * @throws MortException If the input string does not follow the format or contains an invalid date-time.
     */
    public static LocalDateTime convertStringToDateTime(String dateTime) throws MortException {
        try {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("d/M/yyyy HHmm"));
        } catch (DateTimeParseException e) {
            throw new MortException("Invalid date or time: '" + dateTime + "'\n"
                    + "Please enter a date and time with the following format: 'dd/mm/yyyy HHMM'");
        }
    }

    /**
     * Converts a given text string to a LocalDate object.
     *
     * @param date The text string with the format dd/M/yyyy.
     * @return The LocalDate object representing the date given in the string.
     * @throws MortException If the input string does not follow the format or contains an invalid date.
     */
    public static LocalDate convertStringToDate(String date) throws MortException {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yyyy"));
        } catch (DateTimeParseException e) {
            throw new MortException("Invalid date: '" + date + "'\n"
                    + "Please enter a date with the following format: 'dd/mm/yyyy'");
        }
    }

    /**
     * Parses tasks from the saved text file.
     * @param savedTask The text string to be parsed.
     * @return An array of strings containing the details of the task.
     */
    public static String[] parseSavedTask(String savedTask) {
        return savedTask.split(" \\| ", 4);
    }
    private static Command validateToDo(String[] parsedCommand) throws MortException {
        if (parsedCommand.length < 2) {
            throw new MortException("It seems you've invented a way to do nothing. Typical...\n"
                    + Ui.getCommandHelp(CommandWord.TODO));
        } else if (parsedCommand[1].contains("|")) {
            throw new MortException("'|' characters are not allowed in the task description.");
        } else {
            return new AddCommand(TaskType.TODO, parsedCommand[1]);
        }
    }

    private static Command validateDeadline(String[] parsedCommand) throws MortException {
        if (parsedCommand.length < 2) {
            throw new MortException("It seems you've invented a way to do nothing. Typical...\n"
                    + Ui.getCommandHelp(CommandWord.DEADLINE));
        } else {
            String[] parsedTask = parsedCommand[1].split(" /by ", 2);
            if (parsedTask[0].startsWith("/by") || parsedTask[0].isBlank()) {
                throw new MortException("It seems you've invented a way to do nothing. Typical...\n"
                        + Ui.getCommandHelp(CommandWord.DEADLINE));
            } else if (parsedTask.length < 2) {
                throw new MortException("You do realise deadlines and events usually have a time or date,"
                        + " right?\n" + Ui.getCommandHelp(CommandWord.DEADLINE));
            } else if (parsedTask[0].contains("|")) {
                throw new MortException("'|' characters are not allowed in the task description.");
            } else {
                return new AddCommand(TaskType.DEADLINE, parsedTask[0], parsedTask[1]);
            }
        }
    }

    private static Command validateEvent(String[] parsedCommand) throws MortException {
        if (parsedCommand.length < 2) {
            throw new MortException("It seems you've invented a way to do nothing. Typical...\n"
                    + Ui.getCommandHelp(CommandWord.EVENT));
        } else {
            String[] parsedTask = parsedCommand[1].split(" /at ", 2);
            if (parsedTask[0].startsWith("/at") || parsedTask[0].isBlank()) {
                throw new MortException("It seems you've invented a way to do nothing. Typical...\n"
                        + Ui.getCommandHelp(CommandWord.EVENT));
            } else if (parsedTask.length < 2) {
                throw new MortException("You do realise deadlines and events usually have a time or date,"
                        + " right?\n" + Ui.getCommandHelp(CommandWord.EVENT));
            } else if (parsedTask[0].contains("|")) {
                throw new MortException("'|' characters are not allowed in the task description.");
            } else {
                return new AddCommand(TaskType.EVENT, parsedTask[0], parsedTask[1]);
            }
        }
    }

    private static Command validateDelete(String[] parsedCommand) throws MortException {
        if (parsedCommand.length < 2) { // blank task number
            throw new MortException("Enter a task number, nitwit.\n"
                    + Ui.getCommandHelp(CommandWord.DELETE));
        } else {
            int num = Integer.parseInt(parsedCommand[1]);
            return new DeleteCommand(num);
        }
    }

    private static Command validateMark(String[] parsedCommand) throws MortException {
        if (parsedCommand.length < 2) { // blank task number
            throw new MortException("Enter a task number, nitwit.\n"
                    + Ui.getCommandHelp(CommandWord.MARK));
        } else {
            int num = Integer.parseInt(parsedCommand[1]);
            return new MarkCommand(num);
        }
    }

    private static Command validateFind(String[] parsedCommand) throws MortException {
        if (parsedCommand.length < 2) {
            throw new MortException("You're so helpful. What exactly am I supposed to look for?\n"
                    + Ui.getCommandHelp(CommandWord.FIND));
        } else {
            return new FindCommand(parsedCommand[1]);
        }
    }
    private static Command validateUnmark(String[] parsedCommand) throws MortException {
        if (parsedCommand.length < 2) { // blank task number
            throw new MortException("Enter a task number, nitwit.\n"
                    + Ui.getCommandHelp(CommandWord.UNMARK));
        } else {
            int num = Integer.parseInt(parsedCommand[1]);
            return new UnmarkCommand(num);
        }
    }

    private static Command validateList(String strippedCommand) throws MortException {
        if (strippedCommand.equals("list")) {
            return new ListCommand();
        } else {
            throw new MortException(Ui.getUnknownCommandMessage(strippedCommand));
        }
    }

    private static Command validateBye(String strippedCommand) throws MortException {
        if (strippedCommand.equals("bye")) {
            return new ExitCommand();
        } else {
            throw new MortException(Ui.getUnknownCommandMessage(strippedCommand));
        }
    }

}