package cpen221.mp1.similarity;

import cpen221.mp1.Document;

import java.util.*;


import cpen221.mp1.similarity.DocumentSimilarity;

public class GroupingDocuments {

    /* ------- Task 5 ------- */

    /**
     * Group documents by similarity
     * @param allDocuments the set of all documents to be considered,
     *                     is not null
     * @param numberOfGroups the number of document groups to be generated
     * @return groups of documents, where each group (set) contains similar
     * documents following this rule: if D_i is in P_x, and P_x contains at
     * least one other document, then P_x contains some other D_j such that
     * the divergence between D_i and D_j is smaller than (or at most equal
     * to) the divergence between D_i and any document that is not in P_x.
     */
    public static Set<Set<Document>> groupBySimilarity(Set<Document> allDocuments, int numberOfGroups) {
        DocumentSimilarity documentSimilarityFinder = new DocumentSimilarity();
        HashMap<HashSet<Document>,Double> documentSimilarityMap = new HashMap<>();
        ArrayList<HashSet<Document>> sortedDocumentPairs = new ArrayList<>();
        List<Set<Document>> similarityGrouperList = new ArrayList<>();


        for (Document document1 : allDocuments){
            for (Document document2 : allDocuments){
                if (document1 != document2){
                    HashSet<Document> documentPair = new HashSet<>();
                    documentPair.add(document1);
                    documentPair.add(document2);

                    double similarityScore = documentSimilarityFinder.documentDivergence(document1, document2);

                    documentSimilarityMap.put(documentPair, similarityScore);
                    if (!sortedDocumentPairs.contains(documentPair)) {
                        sortedDocumentPairs.add(documentPair);
                    }
                }
            }
        }

        // Now, all document pairs are in the list, and their score can be accessed using the 

        /*
        Insert Sorting Method Here for sortedDocumentPairs List
         */

         /**
          * Sorting Method
          * MAP: documentSimilarityMap<Set<Document>, Double>   ... holds all doucment pairs as keys and their divergence
          * ARRAYLIST: sortedDocumentPairs
          */
        boolean isSorted = false;
        while (!isSorted){
            isSorted = true;
            for (int pairIterator = 1; pairIterator < sortedDocumentPairs.size(); pairIterator++) {
                if (documentSimilarityMap.get(sortedDocumentPairs.get(pairIterator)) < documentSimilarityMap.get(sortedDocumentPairs.get(pairIterator - 1)))  { // if score is smaller than previous in list
                    isSorted = false;
                    HashSet<Document> tempSet = new HashSet<>(sortedDocumentPairs.get(pairIterator)); // store current
                    sortedDocumentPairs.remove(pairIterator); // remote current
                    sortedDocumentPairs.add(pairIterator - 1, tempSet);
                }
            }
        }

        for (Document document: allDocuments){
            HashSet<Document> singleDocumentSet = new HashSet<>();
            singleDocumentSet.add(document);
            similarityGrouperList.add(singleDocumentSet);
        }

        while (similarityGrouperList.size() > numberOfGroups){ // similarityGrouperList is list with partitions
            // pick lowest scoring pair (int i = 0)
            for (int sortedIndex = 0; sortedIndex < sortedDocumentPairs.size(); sortedIndex++) {
                int indexOfDoc1 = 0;
                int indexOfDoc2 = 0;
                ArrayList<Document> currentDocs = new ArrayList<>(sortedDocumentPairs.get(sortedIndex)); // list of the set of 2 docuemtns
                Document doc1 = currentDocs.get(0);
                Document doc2 = currentDocs.get(1);

                for (int setListIndex = 0; setListIndex < similarityGrouperList.size(); setListIndex++) {
                    if (similarityGrouperList.get(setListIndex).contains(doc1)) {
                        indexOfDoc1 = setListIndex;
                    }
                    else if (similarityGrouperList.get(setListIndex).contains(doc2)) {
                        indexOfDoc2 = setListIndex;
                    }
                }
                // indices found.
                if (indexOfDoc1 != indexOfDoc2) {
                    similarityGrouperList.get(indexOfDoc2).remove(doc2); // remove doc 2 from list
                    similarityGrouperList.get(indexOfDoc1).add(doc2); // add doc 2 to 1's partition
                    if (similarityGrouperList.get(indexOfDoc2).size() == 0) {
                        similarityGrouperList.remove(indexOfDoc2);
                    }
                }
                if (similarityGrouperList.size() == numberOfGroups) {
                    break;
                }
            }

                // find doc1's index in grouperList
                // find doc1's index in grouperList
                // if index in same, move onto next pair
                // else
                    // add doc2 to doc1
                    // pop doc2 from list
                // if size = k
                    // break;
        }

        Set<Set<Document>> finalSet = new HashSet<>();

        for (int i = 0; i < similarityGrouperList.size(); i++){
            finalSet.add(similarityGrouperList.get(i));
        }


        return finalSet;
    }


}
