package cpen221.mp1;

import cpen221.mp1.exceptions.NoSuitableSentenceException;
import cpen221.mp1.sentiments.SentimentAnalysis;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

public class Task3SentimentTests {
    private static cpen221.mp1.Document testDocument1;
    private static cpen221.mp1.Document testDocument2;
    private static cpen221.mp1.Document testDocument3;
    private static cpen221.mp1.Document testDocument4;
    private static cpen221.mp1.Document testDocument5;

    @BeforeAll
    public static void setupTests() throws MalformedURLException {
        testDocument1 = new cpen221.mp1.Document("The Ant and The Cricket", "resources/antcrick.txt");
        testDocument2 = new cpen221.mp1.Document("The Ant and The Cricket", new URL("http://textfiles.com/stories/antcrick.txt"));
        testDocument3 = new cpen221.mp1.Document("Empty","resources/empty.txt");
        testDocument4 = new cpen221.mp1.Document("Happy","resources/happy.txt");
        testDocument5 = new cpen221.mp1.Document("Sad","resources/sad.txt");

    }

    @Test
    public void testSentences() {
        String text = "..."; // the text for analysis
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
            AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
            Sentiment sentiment = response.getDocumentSentiment();
            if (sentiment != null) {
                System.out.println(sentiment.getScore());
                System.out.println(sentiment.getMagnitude());
            }
        }
        catch (IOException ioe) {
            System.out.println(ioe);
            throw new RuntimeException("Unable to communicate with Sentiment Analyzer!");
        }
    }


    @Test
    public void sentimentTest1() throws NoSuitableSentenceException{
        System.out.println(testDocument3.getMostNegativeSentence());
        System.out.println(testDocument3.getMostPositiveSentence());

        //Expected result is an exception
    }

    @Test
    public void sentimentTest2() throws NoSuitableSentenceException{
        Assertions.assertEquals(testDocument1.getSentence(37),testDocument1.getMostPositiveSentence());
        Assertions.assertEquals(testDocument1.getSentence(17),testDocument1.getMostNegativeSentence());
        Assertions.assertEquals(testDocument1.getSentence(37),testDocument2.getMostPositiveSentence());
        Assertions.assertEquals(testDocument1.getSentence(17),testDocument2.getMostNegativeSentence());

    }

    @Test
    public void sentimentTask3() throws NoSuitableSentenceException{
        System.out.println(testDocument4.getMostPositiveSentence());
        System.out.println(testDocument4.getMostNegativeSentence());

        //Expected result is an exception
    }

    @Test
    public void sentimentTask4() throws NoSuitableSentenceException{
        System.out.println(testDocument5.getMostNegativeSentence());
        System.out.println(testDocument5.getMostPositiveSentence());

        //Expected result is an exception
    }

}
