package cpen221.mp1.sentiments;

import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import cpen221.mp1.exceptions.NoSuitableSentenceException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import cpen221.mp1.*;

public class SentimentAnalysis {

    /**
     * Obtains the most positive sentence in a document
     * @param document
     * @return the most positive sentence in a document
     * @throws NoSuitableSentenceException if there are no positive sentences
     */
    public static String getMostPositiveSentence(cpen221.mp1.Document document)
            throws NoSuitableSentenceException {

        int mostPositiveSentenceID;
        List<Float> sentimentScores = new ArrayList<>();
        String mostPositiveSentence;

        try (LanguageServiceClient language = LanguageServiceClient.create()) {

            for (int i = 1; i < document.numSentences()+1; i++){
                String text = document.getSentence(i);

                Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
                AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
                Sentiment sentiment = response.getDocumentSentiment();

                sentimentScores.add(sentiment.getScore());
            }

            mostPositiveSentenceID = returnMaxIndex(sentimentScores);

            if (document.numSentences() == 0 || sentimentScores.get(mostPositiveSentenceID) < 0.3){
                throw new NoSuitableSentenceException();
            }

            mostPositiveSentence = document.getSentence(mostPositiveSentenceID+1);

        }
        catch (IOException ioe) {
            System.out.println(ioe);
            throw new RuntimeException("Unable to communicate with Sentiment Analyzer!");
        }


        return mostPositiveSentence;
    }

    /**
     * Obtain the most negative sentence in a document
     * @param document
     * @return the most negative sentence in a document
     * @throws NoSuitableSentenceException if there are no negative sentences
     */
    public static String getMostNegativeSentence(cpen221.mp1.Document document)
            throws NoSuitableSentenceException {

        int mostNegativeSentenceID;
        List<Float> sentimentScores = new ArrayList<>();
        String mostNegativeSentence;

        try (LanguageServiceClient language = LanguageServiceClient.create()) {

            for (int i = 1; i < document.numSentences()+1; i++){
                String text = document.getSentence(i);

                Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
                AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
                Sentiment sentiment = response.getDocumentSentiment();

                sentimentScores.add(sentiment.getScore());
            }

            mostNegativeSentenceID = returnMinIndex(sentimentScores);

            if (document.numSentences() == 0 || sentimentScores.get(mostNegativeSentenceID) > -0.3){
                throw new NoSuitableSentenceException();
            }

            mostNegativeSentence = document.getSentence(mostNegativeSentenceID+1);

        }
        catch (IOException ioe) {
            System.out.println(ioe);
            throw new RuntimeException("Unable to communicate with Sentiment Analyzer!");
        }


        return mostNegativeSentence;
    }

    /**
     * requires: list != null && list.size() > 0
     * Obtain the latest index of the highest value in the list
     * @param list
     * @return the latest index of the highest value in the list
     */
    public static int returnMaxIndex(List<Float> list){
        if (list.size() == 0){
            return 0;
        }

        float max = list.get(0);
        int maxID = -1;


        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) >= max) {
                max = list.get(i);
                maxID = i;
            }
        }

        return maxID;
    }

    /**
     * requires: list != null && list.size() > 0
     * Obtain the latest index of the lowest value in the list
     * @param list
     * @return the latest index of the lowest value in the list
     */
    public static int returnMinIndex(List<Float> list){
        if (list.size() == 0){
            return 0;
        }

        float min = list.get(0);
        int minID = -1;


        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) <= min) {
                min = list.get(i);
                minID = i;
            }
        }

        return minID;
    }

}
