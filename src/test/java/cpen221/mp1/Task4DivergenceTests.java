package cpen221.mp1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cpen221.mp1.similarity.DocumentSimilarity;

import java.net.MalformedURLException;
import java.net.URL;

public class Task4DivergenceTests {

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
    public void testSameDocument() {
        DocumentSimilarity testSameDoc = new DocumentSimilarity();
        Assertions.assertEquals(0, testSameDoc.jsDivergence(testDocument1, testDocument1));
    }

    @Test 
    public void testLog2() {
        DocumentSimilarity testLog2 = new DocumentSimilarity();
        Assertions.assertEquals(1.0, testLog2.log2(2.0));
        Assertions.assertEquals(0.0, testLog2.log2(1.0));
        Assertions.assertEquals(2.0, testLog2.log2(4.0));
        Assertions.assertTrue(Math.abs(6.64385618977 - testLog2.log2(100.0)) < 0.0001);
    }

    @Test
    public void testDoc1andMyDoc() {
        DocumentSimilarity testDoc1andMyDoc = new DocumentSimilarity(); 
        System.out.println("JsDivergence: ");
        System.out.println(testDoc1andMyDoc.jsDivergence(testDocument1, myDoc1));
    }

    @Test 
    public void testDoc1andDoc2() {
        DocumentSimilarity testDoc1and2 = new DocumentSimilarity();
        System.out.println("JsDivergence of doc1 and doc2");
        System.out.println(testDoc1and2.jsDivergence(testDocument1, testDocument2));
    }

    @Test
    public void testDoc1and2Divergence() {
        DocumentSimilarity testDoc1and2Divergence = new DocumentSimilarity();
        double docDiv = testDoc1and2Divergence.documentDivergence(testDocument1, testDocument2);
        System.out.println(docDiv);
    }

    @Test
    public void testSameDocDivergence() {
        DocumentSimilarity testSameDocDivergence = new DocumentSimilarity();
        Assertions.assertEquals(0.0, testSameDocDivergence.documentDivergence(testDocument1, testDocument1));
    }
}
