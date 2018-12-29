/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WordNetTest {
    @Test
    public void checkNounPresence() {
        WordNet tester = new WordNet("synsets3.txt", "hypernams.txt");
        assertTrue(tester.isNoun("a"));
        assertFalse(tester.isNoun("Not present"));
    }
}
