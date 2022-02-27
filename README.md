# MP1: Analyzing Text Documents



For a detailed description, see [the associated page](https://cpen221ubc.notion.site/MP1-Analyzing-Text-Documents-9765295da59046c79767d68ae85ebb71) on Notion.


### MiniProject 1 for Fall 2021

# Background

We can analyze a text document using multiple features, and these features allow us to make certain inferences about a collection of documents. In some situations, we may be able to detect who the author of a document is ... and even speculate about whether a document was plagiarized or not.

In this miniproject, you will compute different features of a document, determine similarity between documents, and also attempt to understanding something about encrypted documents.

This miniproject is divided into several tasks. These tasks will help you improve on your ability to develop a small-to-medium-sized application that can perform some sophisticated analysis. You will work in a team of 3 and develop your skills at group work and project management. One of the tasks also involves using the Google Cloud AI Services and this task will expose you to some of the recent advances in artificial intelligence and natural language processing.

From our perspective, a `Document` is a **datatype** that represents a piece of text and supports certain operations or analyses. You will be given a list of URLs to documents on the Internet. You will process these documents (and this is very easy, if we use features that most modern programming languages support).

---

## Task 1: Sentence-Level Metrics

For this task, you will have to split a document into its constituent sentences and determine:

1. the average sentence length: the average number of words in a sentence ($m_1$);
2. the sentence complexity: the average number of **phrases** per sentence ($m_2$).

A **phrase** is a **non-empty** (empty = empty string or whitespace only) part of a sentence that is separated from another phrase by commas, colons and semi-colons.

One of the challenges with text processing is the fact that there are subtleties so we need to establish some rules.

- **How to read from a file or URL in Java**
    
    **Reading from a file**
    
    There are several ways in which one can read from a file. Here is *one* approach that prints each line (not each sentence) to `System.out`. We will use a special Java object called a `BufferedReader` to help with this:
    
    ```java
    try {
      String fileName = "myFile.txt"; 
    	// myFile.txt should be in the root directory for your project
    
    	BufferedReader reader = new BufferedReader(new FileReader(fileName));
    	for (String fileLine = reader.readLine(); 
    				fileLine != null; 
    				fileLine = reader.readLine()) {
    		System.out.println(fileLine);
    	}
    	reader.close();
    }
    catch (IOException ioe) {
    	System.out.println("Problem reading file!");
    }
    ```
    
    **Reading from a URL**
    
    One can also directly read from a URL with a code snippet such as this, which uses a different Java approach to input that is a `Scanner`:
    
    ```java
    try {
    	String documentURL = "http://textfiles.com/stories/antcrick.txt";
    	Scanner urlScanner = new Scanner(new URL(documentURL).openStream());
    	while (urlScanner.hasNext()) {
    		System.out.println(urlScanner.nextLine());
    	}
    }
    catch (IOException ioe) {
    	System.out.println("Problem reading from URL!");
    }
    ```
    
- **Dealing with whitespace**
    
    Text documents use whitespace (regular spaces, new line characters, ...) to provide some information but, for simplicity, you should replace multiple consecutive occurrences of whitespace with a single space character. We lose information like paragraph-level information and that is acceptable for this miniproject. (We could use two consecutive line breaks or new line characters to identify a new paragraph, although that is not needed for this miniproject.)
    
- **What really is a sentence?**
    
    We will consider a sentence to be a sequence of characters that **(1) is terminated by (but doesn't include) the characters ! ? . or the end of the file, (2) excludes whitespace on either end, and (3) is not empty.** In fact, the definition of a sentence is subtle and it may be convenient to use a `[BreakIterator](https://docs.oracle.com/javase/7/docs/api/java/text/BreakIterator.html)` to identify sentences. Here is an example using a `BreakIterator`:
    
    ```java
    String text = "..."; // the text to split into sentences
    BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
    iterator.setText(text);
    int start = iterator.first();
    for (int end = iterator.next();
    			end != BreakIterator.DONE;
    			start = end, end = iterator.next()) {
          
    		String sentence = text.substring(start, end);
    		System.out.println(sentence);
    }
    ```
    
- **What is a word?**
    - We will convert all text to lowercase for the purpose of **all** our analysis.
    - **What is a word?** We can start by defining the **tokens** in a document to be the `String`s that we obtain when we split the `String` that represents the complete text around whitespaces. A **word** is a non-empty token that is not completely made up of punctuation. If a token begins or ends with punctuation then a word can be obtained by removing the starting and trailing punctuation.
    - Specifically, the start of word should not contain any of **! " $ % & ' ( ) * + , - . / : ; < = > ? @ [ \ ] ^ _ ` { | } ~** and the end of  a word should not contain any of **! # " $ % & ' ( ) * + , - . / : ; < = > ? @ [ \ ] ^ _ ` { | } ~**. A word can being with a `#` to support hashtags as separate words.
    - We will consider hyphenated words to be one word (e.g., "real-time").
    - When reading text, on occasion, two words may be separated by a newline character (`\n`) and not other whitespace characters. If you do not account for this then you may end up combining two words as one word; make sure you separate out words that may only be separated by the newline character.
    - You should analyze everything in the text retrieved from the URLs provided, including boilerplate headers about copyright. This will keep our analysis simple and, again with larger textual content, not affect our results significantly.
    - The broader message is that we do not need to perform data cleaning, which is actually an expensive and context-sensitive task.

---

## Task 2: Word-Level Metrics

To get started, you will implement the `Document` datatype to create instances of this datatype and to support three operations related to word-level metrics:

1. average word length ($m_3$);
2. unique word ratio: the number of unique words to the total number of words ($m_4$);
3. **[Hapax Legomana](https://en.wikipedia.org/wiki/Hapax_legomenon) ratio**: the number of words occurring **exactly once** to the total number of words ($m_5$).

---

## Task 3: Document-Level Sentiment Analysis

You will use the Google Cloud Natural Language client library to detect the sentiment of each sentence in the given document and determine:

1. the most positive sentence;
2. the most negative sentence.

How do we determine if a sentence is sufficiently positive or negative in its tone/sentiment? Using Google's Natural Language AI system, we can obtain a sentiment score and a sentiment magnitude. A sentence is **positive** if the sentiment score is ≥ 0.3. A sentence is **negative** is the sentiment score is ≤ -.03. After having determined a set of positive sentences, the most positive sentence is the sentence with the highest sentiment score. In the case of a tie between multiple sentences, we will use the sentence that is **later** in the document as the most positive sentence. The approach to the most negative sentence is analogous.

> You can use Google's Natural Language library for free as long as you do not make too many requests of the system. You can analyze up to 5000 sentences (per month) for free as long as each sentence is no more than 1000 characters. Keep this in mind when you are testing your work.
> 

- **Using Google's Sentiment Analysis**
    
    To use Google Cloud's sentiment analysis functionality, you will need to sign up for Google Cloud account. If you already have a Google account then this is very simple: you go to [https://cloud.google.com/](https://cloud.google.com/) and sign in. Sentiment analysis is part of the [Natural Language AI](https://cloud.google.com/natural-language). You can follow the [Quick Start](https://cloud.google.com/natural-language/docs/setup) documentation. If you are using Google Cloud for the first time then you will get $300 as free usage credits and this is more than sufficient for this mini-project. **With a bit of planning, you will not have to spend any of the free credits.**
    
    The important step in using Google's sentiment analysis is to obtain the `JSON` file with your Google account credentials. Each of you should keep these credentials private and not share it with anyone else. In a team, each student should use their own credentials.
    
    Once you have the `JSON` credentials file, if you are going to run your program from the command line then you should set the `GOOGLE_APPLICATION_CREDENTIALS` environment variable as shown [here](https://cloud.google.com/natural-language/docs/setup#auth-env). If you are running your program from IntelliJ IDEA then you can set this environment variable in IntelliJ IDEA by using the `Run -> Edit Configurations ...` menu and providing the environment variable in the Run/Debug configurations.
    
    ![Screen Shot 2021-09-22 at 3.14.00 PM.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/311de9d0-0406-4d1d-96ee-4f4fc7f4b5a2/Screen_Shot_2021-09-22_at_3.14.00_PM.png)
    
    To use sentiment analysis in your programs, you would need to import the following libraries:
    
    ```java
    import com.google.cloud.language.v1.AnalyzeSentimentResponse;
    import com.google.cloud.language.v1.Document;
    import com.google.cloud.language.v1.Document.Type;
    import com.google.cloud.language.v1.LanguageServiceClient;
    import com.google.cloud.language.v1.Sentiment;
    ```
    
    You will also need to add the following dependencies to the Gradle build file (`build.gradle`) although this has been done for you:
    
    ```groovy
    implementation platform('com.google.cloud:libraries-bom:23.0.0')
    implementation 'com.google.cloud:google-cloud-language'
    ```
    
    The actual sentiment analysis is then easily done with code such as this:
    
    ```java
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
    ```
    
    You should also read about [interpreting the sentiment analysis](https://cloud.google.com/natural-language/docs/basics#interpreting_sentiment_analysis_values) response. Note that the `Document` type in the code snippet above is not the same as what you have been asked to implement. This is the `Document` type that is part of Google Cloud's Natural Language API (Application Programming Interface).
    

---

## Task 4: Comparing Documents

To determine how similar two documents, $D_1$ and $D_2$, are, you will start by determining the **Jensen-Shannon divergence ($\delta_{js}$)** between the two documents. You will them combine this metric with other metrics computed earlier as follows:

> **Document Divergence** $\Delta_{1,2}$ = $(\sum_{i = 1}^{5} w_i\delta_i)+w_{js}\delta_{js}$, 
where $\delta_i = ||m_{i,1} - m_{i,2}||$,
$m_{i,j}$ is the value of $m_i$ for document $D_j$,
and $w_i$ and $w_{js}$ are given weights.
> 

The larger $\Delta_{1,2}$ is the more **dissimilar** $D_1$ and $D_2$ are.

### Jensen-Shannon Divergence

The Jensen-Shannon Divergence is a method for determining if two probability distributions are similar or not.

[Jensen-Shannon divergence - Wikipedia](https://en.wikipedia.org/wiki/Jensen%E2%80%93Shannon_divergence)

We will use the Jensen-Shannon Divergence to determine if two textual documents are similar or not. A low Jensen-Shannon Divergence score will indicate that documents are similar (two identical documents will have a divergence score of 0).

The Jensen-Shannon Divergence is defined as follows, where we will use $P$ and $Q$ to refer to the two documents we seek to compare: $JSD(P || Q) = \frac{1}{2}\sum_{i=1}^{n}\left( p_i \lg\frac{p_i}{m_i} + q_i \lg\frac{q_i}{m_i} \right),$

and in our context we define $p_i$ as the probability of word $w_i$ appearing in document $P$ and $q_i$ as the probability of word $w_i$ appearing in document $Q$. Further,

$$m_i = \frac{p_i+q_i}{2},$$

and $\lg{x}$ is the logarithm of $x$ to the base 2.

For the JSD, the summation is over all words that appear in the two documents together. If $w_i$ appears in $P$ and not in $Q$ then $q_i = 0$. By definition: $p \lg\frac{p}{m} = 0$ when $p = 0$.

Consider the following text, which is an excerpt from Shelley's poem **Mutability:**

> We rest. A dream has power to poison sleep;
We rise. One wandering thought pollutes the day;
We feel, conceive or reason, laugh or weep;
Embrace fond woe, or cast our cares away:
> 

Here is a count of the words that appear in the text above:

```
we: 3; rest: 1; a: 1; dream: 1; has: 1; power: 1; to: 1; poison: 1;
sleep: 1; rise: 1; one: 1; wandering: 1; thought: 1; pollutes: 1;
the: 1; day: 1; feel: 1; conceive: 1; or: 3; reason: 1; laugh: 1;
weep: 1; embrace: 1; fond: 1; woe: 1; cast: 1; our: 1; cares: 1; away: 1
```

From this, and using the fact that there are 33 words in total, if we say that `we` is the first word ($w_1$) then

$$p_1 = \textrm{ probability that a word is } w_1 = \frac{1}{11}.$$

The Jensen-Shannon Divergence, in our case, uses the frequency with which words appear to determine if two documents are divergent or not.

---

## Task 5: Grouping Documents by Similarity

In this task, you will split a list of documents into groups using the similarity/divergence scores. This operation may allow us to group documents by their author even though we may have little insight up front into who the author is.

Suppose we have $n$ documents and we want to split the documents into $k$ non-empty partitions. $k$ can be between $1$ and $n$. When $k=1$, all the documents are in one group, and when $k=n$, each document is in its own group.

The criterion for partitioning the documents is as follows: if we have partitioned the documents into $k$ partitions, $1 < k < n$, then each document is more similar to some document in its partition than to a document in some other partition. To state this formally, let $D_i$ be a document and let it be in partition $P_x$ that also contains other documents**,** then we must have

$$\exists D_j \in P_x \textrm{ such that } j \ne i \textrm{ and } \Delta_{i,j} \le \Delta_{i,k}~\forall D_k \in P_y, y \ne x.$$

What the mathematically quantified proposition above states is: if $D_i$ is in $P_x$, and $P_x$ contains at least one other document, then $P_x$ contains some other $D_j$ such that the divergence between $D_i$ and $D_j$ is smaller than (or at most equal to) the divergence between $D_i$ and any document that is not in $P_x$.

To find such partitions, we can use the following algorithm:

1. Compute the document divergence for all pairs of documents.
2. Place each document in its own partition.
3. Sort the document pairs on the basis of their document divergence.
4. While the number of partitions is not $k$, merge partitions as follows:
    - Repeatedly select the pair of documents with the least document divergence:
        - **These documents may already be in the same partition** because of an earlier step. In this case, move on to the next document pair.
        - **If the two documents are in different partitions**, merge the partitions they are in.

Notice that we reduce the number of partitions from $n$ until $k$. *The insight is that the two most similar documents must be in the same partition.* We can prove that this algorithm is correct but we will not delve into that detail here. The solution produced by this algorithm will be unique as long as no two document pairs have the same divergence.

---

## Task 6: The Fourier Transform and Cryptanalysis

**To be added.**

---

## Implementation and Testing

- Follow the **CPEN 221 Java Style Guide** when you write code.
- You can check that your code conforms to the guidelines by using the **Check Style** feature of the Checkstyle Plugin.
- **Construct test cases that will help you test all aspects of your implementation.** As you write tests, use IntelliJ's code coverage tool to evaluate the coverage of the tests. You should achieve 100% class coverage and at least 90% lines-of-code coverage and at least 85% branch coverage via your tests. See the provided test cases as examples. Add your test cases in a **different** Java file. For obtaining test coverage information using IntelliJ IDEA, see this [documentation page](https://www.jetbrains.com/help/idea/running-test-with-coverage.html#read-coverage%20report), which also discusses how you can enable branch coverage.
- You may **only use standard Java datatypes** that you can import from `java.util`, `java.text`, `[java.net](http://java.net)` and `java.io`. (No `javafx` or other imports.)
- You are expected to write helper methods (usually, though not always, private methods) to keep individual methods small.

## Specifications

Some methods have imprecise or under-determined specifications. You are expected to write clear specifications for all methods that you have to implement unless a complete specification has been provided.

## Directory Structure and Gradle

We use `gradle` as a tool to compile your programs and run test cases. [Gradle is a build system](https://gradle.org/) that handles a variety of tasks that enable us to compile, test (and even deploy) our applications.

The MP1 implementation depends on a few libraries that are not part of the Java SDK: JUnit, which is used to manage tests, and the Google Cloud Language libraries. These libraries are declared as dependencies during the compilation process (see the `build.gradle` file) and so when the first build occurs, the library is actually downloaded for you and then included in the compilation process. If we did not use Gradle then we would have to download these libraries and tell the Java compiler that they should be included during code compilation. Using Gradle saves us some effort.

**You should preserve the directory structure for the codebase.** All your main implementation must be in the directory `src/main/java/mp1`. If you introduce new classes then they should also be in the same directory and would need the statement `package mp1;` at the top of the Java file. All your tests should be in the `src/test/java/mp1` directory. Changing the directory structure will break the build process and your work will not receive any credit if your alter the directory structure.

## Submission

You will submit your work via GitHub, by pushing your work to the `main` branch of repository that was created for you.

### Intermediate Milestone

This is an opportunity to get some feedback from the TAs. We are not looking for perfect implementations at the intermediate milestone but *sufficient* progress (e.g., most of the requested methods work but have minor bugs; testing coverage is not at the desired level). *Insufficient progress* will result in a lowered grade.

You should aim to complete -- at the least -- Tasks 1 and 2. 

You should push your work to the GitHub repository that you are using. Create a new branch called `milestone1` and push this branch to GitHub for the intermediate milestone. Read the module on git and branching (on Canvas). This [tutorial](https://www.atlassian.com/git/tutorials/using-branches) is an additional resource.

## Teamwork

- You will be working in teams of 3 for this MP. Read the note on **Teamwork** that has been included in the course syllabus. Plan the division of work.
- Your final commit before the submission deadline **must** include files named `CONTRIB-ghusername.md` where `ghusername` should be replaced by the GitHub username of a team member, and these files should include a list of contributions made by that team member.
(Your repository should, therefore, have three such files.) These files must be in the top-level directory of your repository, where the `README.md` file is.
- Academic integrity also involves claiming credit only for the work one did and not for the work of others. We expect contributions from each team member and will occasionally use the record of contributions to the GitHub repository as an indicator of participation level.
- The staff reserve the right to adjust scores for the miniproject based on contribution levels.

## Hints

- **Start early!**
- **MPs involve a non-trivial amount of work.** 
The MPs are structured to help you maximize your learning, and to expose you to new ideas. Don't be daunted by the scope of the MPs.
- **Think before you start writing code.** 
With some planning, you will not have to write a lot of code.
- Be prepared for the design discussion.
- **Debug systematically.** 
Learn to use the debugger. You will save time.
- Start writing test cases early.
- Read about Java's `Map` and `HashMap` types.
- **Complete a task fully and thoroughly** rather than having partial success with the tasks. *Partial implementations of tasks may not get any credit.*
- **Some of the tasks will have a level of ambiguity.**
This is deliberate. Any engineering problem will have aspects that are not clearly specified and you will have to work to resolve those issues. You would do so via consultations with clients or end-users. In the case of this mini-project, you should talk to the course staff about the ambiguities.