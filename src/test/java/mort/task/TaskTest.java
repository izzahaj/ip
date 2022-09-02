package mort.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TaskTest {
    private final Task task1 = new Task("buy apples", true);
    private final Task task2 = new Task("buy 6 apples & chicken", false);
    @Test
    public void testGetStatusIcon() {
        assertEquals("X", task1.getStatusIcon());
        assertEquals(" ", task2.getStatusIcon());
    }
    @Test
    public void testGetSaveFormat() {
        assertEquals("| 1 | buy apples", task1.getSaveFormat());
        assertEquals("| 0 | buy 6 apples & chicken", task2.getSaveFormat());
    }

    @Test
    public void testToString() {
        assertEquals("[X] buy apples", task1.toString());
        assertEquals("[ ] buy 6 apples & chicken", task2.toString());
    }
    @Test
    public void testIsMatch() {
        assertEquals(true, task2.isMatch("appl"));
        assertEquals(true, task2.isMatch("6 APpl"));
        assertEquals(true, task2.isMatch("buy 6 Appl"));
        assertEquals(true, task2.isMatch("appl"));
        assertEquals(false, task2.isMatch("[X]"));
        assertEquals(false, task2.isMatch("homework"));
        assertEquals(false, task1.isMatch("[ ]"));
    }
}
