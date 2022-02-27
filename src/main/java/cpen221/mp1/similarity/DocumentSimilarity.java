package cpen221.mp1.similarity;

import java.util.*;

import cpen221.mp1.Document;

public class DocumentSimilarity {

    /* DO NOT CHANGE THESE WEIGHTS */
    private final int WT_AVG_WORD_LENGTH      = 5;
    private final int WT_UNIQUE_WORD_RATIO    = 15;
    private final int WT_HAPAX_LEGOMANA_RATIO = 25;
    private final int WT_AVG_SENTENCE_LENGTH  = 1;
    private final int WT_AVG_SENTENCE_CPLXTY  = 4;
    private final int WT_JS_DIVERGENCE        = 50;
    /* ---- END OF WEIGHTS ------ */

    /* ------- Task 4 ------- */
    /**
     * 
     * @param x, {@code x > 0}
     * @return the log base 2 value of x
     */
    public double log2(double x) {
        return (double) (Math.log(x) / Math.log(2.0));
    }
    /**
     * Compute the Jensen-Shannon Divergence between the given documents
     * @param doc1 the first document, is not null
     * @param doc2 the second document, is not null
     * @return the Jensen-Shannon Divergence between the given documents
     */
    public double jsDivergence(Document doc1, Document doc2) {
        // 1. Add all words that appear in either document to a set to iterate over
        double jsDivergenceScore = 0;
        double doc1wordProbability;
        double doc2wordProbability;
        double doc1logPOverM;
        double doc2logPOverM;
        double averageWordProbability;
        Map<String, Integer> doc1wordCounts = new HashMap<>(doc1.getWordCounts());
        Map<String, Integer> doc2wordCounts = new HashMap<>(doc2.getWordCounts());
        Set<String> doc1uniqueWords = new HashSet<>(doc1.getUniqueWords());
        Set<String> doc2uniqueWords = new HashSet<>(doc2.getUniqueWords());
        Set<String> wordsInEither = new HashSet<>();
        wordsInEither.addAll(doc1uniqueWords);
        wordsInEither.addAll(doc2uniqueWords);
        
        // 2. calculate each value for each word
        for (String word : wordsInEither) {
            // if either doc doesnt contain word, set p to zero
            doc1wordProbability = (doc1uniqueWords.contains(word) ? (double) doc1wordCounts.get(word) / doc1uniqueWords.size() : 0);
            doc2wordProbability = (doc2uniqueWords.contains(word) ? (double) doc2wordCounts.get(word) / doc2uniqueWords.size() : 0);
            averageWordProbability = (doc1wordProbability + doc2wordProbability) / 2;
            doc1logPOverM = (doc1wordProbability != 0 ? doc1wordProbability*log2(doc1wordProbability/averageWordProbability) : 0);
            doc2logPOverM = (doc2wordProbability != 0 ? doc2wordProbability*log2(doc2wordProbability/averageWordProbability) : 0);
            double summationItem = doc1wordProbability*doc1logPOverM + doc2wordProbability*doc2logPOverM;
            jsDivergenceScore += summationItem;
        }
        jsDivergenceScore/=2.0;
        return jsDivergenceScore;
        
    }

    /**
     * Compute the Document Divergence between the given documents
     * @param doc1 the first document, is not null
     * @param doc2 the second document, is not null
     * @return the Document Divergence between the given documents
     */
    public double documentDivergence(Document doc1, Document doc2) {
        double documentDivergence = 0;
        
        documentDivergence += WT_AVG_WORD_LENGTH * Math.abs(doc1.averageWordLength() - doc2.averageWordLength()); 
        documentDivergence += WT_UNIQUE_WORD_RATIO * Math.abs(doc1.uniqueWordRatio()- doc2.uniqueWordRatio()); 
        documentDivergence += WT_HAPAX_LEGOMANA_RATIO * Math.abs(doc1.hapaxLegomanaRatio() - doc2.hapaxLegomanaRatio());
        documentDivergence += WT_AVG_SENTENCE_LENGTH * Math.abs(doc1.averageSentenceLength() - doc2.averageSentenceLength());
        documentDivergence += WT_AVG_SENTENCE_CPLXTY * Math.abs(doc1.averageSentenceComplexity() - doc2.averageSentenceComplexity());
        
        documentDivergence += jsDivergence(doc1,doc2) * WT_JS_DIVERGENCE;

        return documentDivergence;
    }

}
