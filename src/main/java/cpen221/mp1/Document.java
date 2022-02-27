package cpen221.mp1;

import cpen221.mp1.exceptions.NoSuitableSentenceException;
import cpen221.mp1.sentiments.SentimentAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.*;

public class Document {

    /* ------- Task 0 ------- */
    /*  all the basic things  */
    private String docId;
    private URL docURL;
    private String fileName;
    private List<String> words;
    protected Set<String> uniqueWords;
    protected Map<String, Integer> wordCountMap;
    private List<String> sentences;
    static final char[] illegalWordStarters = {'!', '\"', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?',
            '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~', ' ', '\n'};
    static final char[] illegalWordEnders = {'!', '\"', '$', '%', '&', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?',
            '@', '[', '\\', ']', '^', '_', '{', '|', '}', '~', '#', ' ', '\n'};

    /**
     * Create a new document using a URL
     * @param docId: String containing the document identifier
     * @param docURL: String containing the URL with the contents of the document. Must be a valid URL
     */
    public Document(String docId, URL docURL) {
        this.docId = docId;
        this.docURL = docURL;
        this.sentences = new ArrayList<>(); // creating a new object requires the 'new' keyword.

        // Load website file into sentences and push to arrayList
        try {
            Scanner urlScanner = new Scanner(docURL.openStream());
            while (urlScanner.hasNext()) {
                String urlLine = urlScanner.nextLine();
                spliceLineToSentenceArray(urlLine);
            }
            getWords();
        }
        catch (IOException ioe) {
            System.out.println(ioe);
            System.out.println("Problem reading from URL!");
        }
    }

    /**
     *
     * @param docId the document identifier
     * @param fileName the name of the file with the contents of
     *                 the document
     */
    public Document(String docId, String fileName) {
        this.docId = docId;
        this.fileName = fileName;
        this.sentences = new ArrayList<>(); // creating a new object requires the 'new' keyword.
        // Load file into sentences arrayList
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine(); fileLine != null; fileLine = reader.readLine()) {
                
                spliceLineToSentenceArray(fileLine);
            }
            reader.close();

            getWords();

        } catch (IOException inputOutputException) {
            System.out.println(inputOutputException);
            System.out.println("Error loading doc from file.");
        }
    }


    /**
     * Initializes words, uniqueWords and wordCountMap
     * @param words is filled with all words in the document in order.
     * @param uniqueWords is a set filled with only unique words
     * @param wordCountMap is filled with key-value pairs of each word in the document and its number of instances in the document
     */
   
    private void getWords(){
        words = new ArrayList<>();

        boolean hasStart = false;
        boolean hasEnd = false;
        int start = 0;
        int end = 0;

        for (String sentence: sentences){
            for (int i = 0; i < sentence.length() - 1; i++){
                boolean illegalStart = false;
                boolean illegalEnd = false;

                if (!hasStart){
                    for (int j = 0; j < illegalWordStarters.length; j++){
                        if (sentence.charAt(i) == illegalWordStarters[j]){
                            illegalStart = true;
                            break;
                        }
                    }

                    if (!illegalStart){
                        start = i;
                        hasStart = true;
                    }

                }

                if (!hasEnd && hasStart){
                    for (int j = 0; j < illegalWordEnders.length; j++){
                        if (sentence.charAt(i+1) == illegalWordEnders[j]) {
                            illegalEnd = true;
                            break;
                        }
                    }

                    if (!illegalEnd){
                        for (int j = 0; j < illegalWordEnders.length; j++){
                            if ( i <(sentence.length()-3) && ((sentence.charAt(i+1)) == '\''|| (sentence.charAt(i+1)) == '`') && sentence.charAt(i+2) == illegalWordEnders[j]) {
                                illegalEnd = true;
                                break;
                            }
                        }
                    }

                    if(illegalEnd){
                        end = i + 1;
                        hasEnd = true;
                    }
                }

                if (hasEnd && hasStart){
                    if (start <= end){
                        String word = sentence.substring(start,end);
                        word = word.toLowerCase(Locale.ROOT);
                        words.add(word);
                        hasEnd = false;
                        hasStart = false;
                    }
                }
            }

        }

        uniqueWords = new HashSet<>();

        for (String word: words){
            uniqueWords.add(word);
        }

        wordCountMap = new HashMap<>();

        for (String word: uniqueWords){
            wordCountMap.put(word, 0);
        }

        for (String word: words){
            wordCountMap.put(word, wordCountMap.get(word)+1);
        }

        return;
    }

    /**
     * Obtain the identifier for this document
     * @return the identifier for this document
     */
    public String getDocId() {
        return this.docId;
    }


    /**
     * 
     * 
     * @param lineOfText is a string argument that represesnts a line from the document being read. This line is broken into sentences and added to the sentences arrayList
     * @param sentences
     */
    public void spliceLineToSentenceArray(String lineOfText) {
        // Split 'lineOfText' into sentences and then add to sentences
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(lineOfText);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            String sentence = lineOfText.substring(start, end).strip();
            // case 1: first addition
            if (sentences.isEmpty()) { // empty or only title
                sentences.add(sentence);
            // case 2: Not first addition
            } else { 
                String prevItemStored = sentences.get(sentences.size() - 1);
                char finalCharOnPrevItemStored = prevItemStored.charAt(prevItemStored.length() - 1);
                char secondLastCharOnPrevItemStored = prevItemStored.charAt(prevItemStored.length() - 2);
                // if previous line ended a sentence, add new item for current line
                if (finalCharOnPrevItemStored == '.' || finalCharOnPrevItemStored == '?' || finalCharOnPrevItemStored == '!' || secondLastCharOnPrevItemStored == '.' || secondLastCharOnPrevItemStored == '!' || secondLastCharOnPrevItemStored == '?') {
                    sentences.add(sentence);
                }
                else { // Else, append to previous sentence fragment
                    sentences.set(sentences.size() - 1, sentences.get(sentences.size() - 1) + " " + sentence);
                }
            }
        }
    }

    public HashSet<String> getUniqueWords() {
        return new HashSet<String>(uniqueWords);
    }

    public HashMap<String, Integer> getWordCounts() {
        return new HashMap<String, Integer>(wordCountMap);
    }

    /* ------- Task 1 ------- */

    /**
     * Obtain the average word length of the document
     * @return Double {@code: return value > 0 and not null} representing the average word length of the document.
     */
    public double averageWordLength() {
        double totalWordLength = 0;

        for (String word: words){
            totalWordLength += word.length();
        }

        return totalWordLength/words.size();
    }

    /**
     * Obtain the unique word ratio of the document
     * @return the unique word ratio of the document {@code: return value > 0 and not null}
     */
    public double uniqueWordRatio() { return (double)uniqueWords.size()/words.size(); }

    /**
     * Obtain the Hapax Legomana ratio of the document
     * @return double: the Hapax Legomana ratio of the document  {@code: return value > 0 and not null}
     */
    public double hapaxLegomanaRatio() {
        int hapaxWords = 0;

        for (String word: uniqueWords){
            if (wordCountMap.get(word) == 1){
                hapaxWords++;
            }
        }

        return (double)hapaxWords/words.size();
    }


    /* ------- Task 2 ------- */

    /**
     * Obtain the number of sentences in the document
     * @return int: the number of sentences in the document {@code: return value > 0 and not null}
     */
    public int numSentences() {
       return this.sentences.size();
    }

    /**
     * Obtain a specific sentence from the document.
     * Sentences are numbered starting from 1.
     *
     * @param sentence_number the position of the sentence to retrieve,
     * {@code 1 <= sentence_number <= this.getSentenceCount()}
     * @return the sentence indexed by {@code 0 <= return value < this.numSentences}
     */
    public String getSentence(int sentence_number) {
        return sentences.get(sentence_number - 1);
    }

    /**
     * Return the average sentence length
     * 
     * @return float: average sentence length {@code return value > 0 and not null}
     */
    public double averageSentenceLength() {       
        return (double)words.size() / (double)numSentences();
    }

    public double averageSentenceComplexity() {
        int phraseCount = 0;
        for (String sentence : sentences) {
            String[] phrasesInSentence = sentence.split("[,;:]");
            phraseCount += phrasesInSentence.length;
        }
        return (double)phraseCount / (double)numSentences();
    }

    /* ------- Task 3 ------- */

//    To implement these methods while keeping the class
//    small in terms of number of lines of code,
//    implement the methods fully in sentiments.SentimentAnalysis
//    and call those methods here. Use the getSentence() method
//    implemented in this class when you implement the methods
//    in the SentimentAnalysis class.

//    Further, avoid using the Google Cloud AI multiple times for
//    the same document. Save the results (cache them) for
//    reuse in this class.

//    This approach illustrates how to keep classes small and yet
//    highly functional.

    /**
     * Obtain the sentence with the most positive sentiment in the document
     * @return the sentence with the most positive sentiment in the
     * document; when multiple sentences share the same sentiment value
     * returns the sentence that appears later in the document
     * @throws NoSuitableSentenceException if there is no sentence that
     * expresses a positive sentiment
     */
    public String getMostPositiveSentence() throws NoSuitableSentenceException {
        return SentimentAnalysis.getMostPositiveSentence(Document.this);
    }

    /**
     * Obtain the sentence with the most negative sentiment in the document
     * @return the sentence with the most negative sentiment in the document;
     * when multiple sentences share the same sentiment value, returns the
     * sentence that appears later in the document
     * @throws NoSuitableSentenceException if there is no sentence that
     * expresses a negative sentiment
     */
    public String getMostNegativeSentence() throws NoSuitableSentenceException {
        return SentimentAnalysis.getMostNegativeSentence(Document.this);
    }

}


