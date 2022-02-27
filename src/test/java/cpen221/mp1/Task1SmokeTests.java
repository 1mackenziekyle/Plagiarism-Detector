package cpen221.mp1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class Task1SmokeTests {

    private static Document testDocument1;
    private static Document testDocument2;
    private static Document myDoc1;

    @BeforeAll
    public static void setupTests() throws MalformedURLException {
        testDocument1 = new Document("The Ant and The Cricket", "resources/antcrick.txt");
        testDocument2 = new Document("The Ant and The Cricket", new URL("http://textfiles.com/stories/antcrick.txt"));
        myDoc1 = new Document("My Poem", "resources/myPoem.txt");
    }

    @Test
    public void testAvgSentenceLength() {
        Assertions.assertEquals(10.027, testDocument1.averageSentenceLength(), 0.005);
    }

    @Test
    public void testAvgSentenceComplexity() {
        Assertions.assertEquals(1.702, testDocument2.averageSentenceComplexity(), 0.005);
    }

    @Test
    public void testSentences() {
        Assertions.assertEquals(37, testDocument1.numSentences());
        Assertions.assertEquals("\"We can't do that,\" they said, \"We must store away food for the winter.", testDocument2.getSentence(5));
    }
}
