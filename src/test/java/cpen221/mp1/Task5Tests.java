package cpen221.mp1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cpen221.mp1.similarity.GroupingDocuments;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Task5Tests {

    private static Document ant;
    private static Document similar;
    private static Document different;
    private static Document unrelated;
    private static Document allApple;
    private static Document allBanana;
    private static Document oneApple;
    private static Document oneBanana;
    private static Set<Set<Document>> similaritySet;
    

    @BeforeAll
    public static void setupTests() throws MalformedURLException {
        ant = new Document("The Ant and The Cricket", "resources/antcrick.txt");
        similar = new Document("Similar to Ant", "resources/antcrick.txt");
        different = new Document("Different", "resources/different.txt");
        unrelated = new Document("Unrelated", "resources/myPoem.txt");
        allApple = new Document("All Apple", "resources/a1.txt");
        oneBanana = new Document("One Banana", "resources/a2.txt");
        allBanana = new Document("All Banana", "resources/a3.txt");
        oneApple = new Document("One Apple", "resources/a4.txt");
        
    }

    @Test
    public void testListSorting() {
        Set<Document> allDocs = new HashSet<>();
        allDocs.add(ant);
        allDocs.add(similar);
        allDocs.add(different);
        allDocs.add(unrelated);

        
        similaritySet = GroupingDocuments.groupBySimilarity(allDocs, 2);
        HashSet<Document> set1 = new HashSet<Document>();
        set1.add(ant);
        set1.add(similar);
        set1.add(different);
        HashSet<Document> set2 = new HashSet<Document>();
        set2.add(unrelated);
        HashSet<HashSet<Document>> bigSet = new HashSet<HashSet<Document>>();
        bigSet.add(set1);
        bigSet.add(set2);

        Assertions.assertEquals(bigSet, similaritySet);

        
    }

    @Test
    public void testMaxGroup() {
        Set<Document> allDocs = new HashSet<>();
        allDocs.add(oneApple);
        allDocs.add(oneBanana);
        allDocs.add(allApple);
        allDocs.add(allBanana);
        Set<Set<Document>> docGroup = new HashSet<>();

        similaritySet = GroupingDocuments.groupBySimilarity(allDocs, 4);

        for (Document doc: allDocs){
            Set<Document> docSet = new HashSet<>();
            docSet.add(doc);
            docGroup.add(docSet);
        }

        System.out.println(similaritySet);
        System.out.println(docGroup);

        Assertions.assertEquals(docGroup, similaritySet);
    }

    @Test
    public void testOneGroup() {
        Set<Document> allDocs = new HashSet<>();
        allDocs.add(oneApple);
        allDocs.add(oneBanana);
        allDocs.add(allApple);
        allDocs.add(allBanana);
        Set<Set<Document>> docGroup = new HashSet<>();
        Set<Document> docSet = new HashSet<>();

        similaritySet = GroupingDocuments.groupBySimilarity(allDocs, 1);

        for (Document doc: allDocs){
            docSet.add(doc);
        }

        docGroup.add(docSet);

        System.out.println(similaritySet);
        System.out.println(docGroup);

        Assertions.assertEquals(docGroup, similaritySet);
    }

    @Test
    public void testTwoGroup() {
        Set<Document> allDocs = new HashSet<>();
        allDocs.add(oneApple);
        allDocs.add(oneBanana);
        allDocs.add(allApple);
        allDocs.add(allBanana);
        Set<Set<Document>> docGroup = new HashSet<>();


        similaritySet = GroupingDocuments.groupBySimilarity(allDocs, 2);

        Set<Document> set1 = new HashSet<>();
        Set<Document> set2 = new HashSet<>();

        set1.add(allApple);
        set1.add(oneBanana);

        set2.add(oneApple);
        set2.add(allBanana);

        docGroup.add(set1);
        docGroup.add(set2);

        System.out.println(similaritySet);
        System.out.println(docGroup);

        Assertions.assertEquals(docGroup, similaritySet);
    }

}
