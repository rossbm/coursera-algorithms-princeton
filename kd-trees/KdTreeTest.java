import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import edu.princeton.cs.algs4.Point2D;

public class KdTreeTest {

    @Test
    public void checkEmpty() {
        KdTree tester = new KdTree();
        assertTrue(tester.isEmpty(), "Newly initialized tree must be empty");
        assertEquals(0, tester.size(), "Newly initialized tree must be empty");
    }
    @Test
    public void checkSingleton() {
        KdTree tester = new KdTree();
        tester.insert(new Point2D(1,2));
        assertEquals(1, tester.size());
    }
    @Test
    public void checkDuplicate() {
        KdTree tester = new KdTree();
        tester.insert(new Point2D(1,2));
        tester.insert(new Point2D(1,2));
        assertEquals(1, tester.size(), "No duplicates allowed!");
    }
    @Test
    public void checkSize() {
        KdTree tester = new KdTree();
        tester.insert(new Point2D(1,2));
        tester.insert(new Point2D(2,1));
        tester.insert(new Point2D(1,2));
        tester.insert(new Point2D(2,2));
        assertEquals(3, tester.size(), "No duplicates allowed!");
    }
    @Test
    public void checkContains() {
        KdTree tester = new KdTree();
        tester.insert(new Point2D(1,2));
        tester.insert(new Point2D(2,1));
        tester.insert(new Point2D(1,2));
        tester.insert(new Point2D(2,2));
        assertTrue(tester.contains(new Point2D(2, 1)), "Missing a point!");
        assertFalse(tester.contains(new Point2D(5, 5)), "Extra point!");
    }

    @Test
    public void checkNearest() {
        KdTree tester = new KdTree();
        tester.insert(new Point2D(0.1, 0.2));
        tester.insert(new Point2D(0.2, 0.1));
        tester.insert(new Point2D(0.2, 0.2));
        Point2D expectedAnswer = new Point2D(0.1, 0.1);
        tester.insert(expectedAnswer);
        Point2D answer = tester.nearest(new Point2D(0.05, 0.05));
        assertEquals(answer, expectedAnswer);

    }

}