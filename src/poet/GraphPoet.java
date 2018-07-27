/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;


import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   this.graph represents its self
    // Representation invariant:
    //   All vertex labels must be lower case, non-empty, and contain no white space.
    //   graph is weakly connected and there is a path
    //   traversing each edge e of the graph exactly weight(e) times.
    // Safety from rep exposure:
    //   The graph is a private field, and return values and method parameters are immutable strings, except for the constructor.
    //   The constructor takes in a file object is used to read from a file and create the rep, and after constructor returns there is
    //   no way for it to access or modify the rep through this file object.
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        final Scanner sc = new Scanner(corpus);
        String current;
        String prev;
        
        if(!sc.hasNext()){
            sc.close();
            return;
        }
        
        current = sc.next().toLowerCase();
        graph.add(current);
        
        while(sc.hasNext()){
            prev = current;
            current = sc.next().toLowerCase();
            int previousEdgeWeight = graph.targets(prev).getOrDefault(current, 0);
            
            graph.set(prev, current, previousEdgeWeight + 1);
        }
        
        sc.close();
        checkRep();
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        final String[] words = input.trim().split("\\s+");
        final StringBuilder poemBuilder = new StringBuilder();
        
        if(words.length > 0) poemBuilder.append(words[0]);
        
        for(int i = 0; i+1 < words.length; i++){
            String bridgeWord = getMaximalBridgeWord(words[i].toLowerCase(), words[i+1].toLowerCase());
            if(!bridgeWord.isEmpty()){
                poemBuilder.append(" " + bridgeWord);
            }
            poemBuilder.append(" " + words[i+1]);
        }
        
        final String poem = poemBuilder.toString();
        checkRep();
        
        return poem;
    }
    
    /**
     * A String representing the word affinity graph derive from the corpus as described above. The string has the same format
     * of weighted graph, which is pair enclose in "(vertices, weighted-edges)". Here vertices represent mathematical set of 
     * words found in the corpus, as defined above, enclosed in "{}". Weighted-edges represent a mathematical set containing
     * 3-tuples "(w1, w2, positive-integer)" with positive-integer indicating the number of times the word w1 is followed by w2
     * in the corpus. The ordering of elements in the sets is unspecified, and all words appear in lower case.
     * <p>for example from this corpus:
     * <pre> Hello, HELLO, hello, goodbye!    </pre>
     * <p>a affinity graph with this string format can be derived:
     * <pre> "({hello,, goodbye!}, {(hello,, hello,, 2), (hello,, goodbye!, 1)})" </pre>
     * 
     */
    @Override public String toString() {
        return graph.toString();
    }
    
    private void checkRep(){
        assert allVertexLabelsAreNonEmptyNonWhitesSpaceLowerCaseStrings();
        assert isWeaklyConnected();
        assert hasWeightedEulerianPath();
    };
    
    private boolean allVertexLabelsAreNonEmptyNonWhitesSpaceLowerCaseStrings(){
        for(String vertex : graph.vertices()){
            if(vertex.isEmpty()) return false;
            if(vertex.length() > vertex.replaceAll("\\s+", "").length()) return false; //contains white space
            if(!vertex.equals(vertex.toLowerCase())) return false; //its not lower case
        }
        return true;
    }
    
    //Requires: this.graph must be weakly connected
    //Effects: returns true iff. there is a path in this.graph for which
    //         every edge e is traversed exactly weight(e) times.
    private boolean hasWeightedEulerianPath(){
        int numberOfVerticesWithPlusOneNetFlow = 0;
        int numberOfVerticesWithNegativeOneNetFlow = 0;
        int numberofVerticesWithZeroNetFlow = 0;
        
        for(String vertex : graph.vertices()){
            int netFlow = sumIntCollection(graph.sources(vertex).values()) - 
                    sumIntCollection(graph.targets(vertex).values());
            if(netFlow == 1){
                numberOfVerticesWithPlusOneNetFlow++;
            }else if(netFlow == 0){
                numberofVerticesWithZeroNetFlow++;
            }else if(netFlow == -1){
                numberOfVerticesWithNegativeOneNetFlow++;
            }            
        }
        
        //Check equivalent condition for Eulerian path of weakly connected graph.
        return numberOfVerticesWithPlusOneNetFlow <= 1 &&
                numberOfVerticesWithNegativeOneNetFlow <= 1 &&
                numberOfVerticesWithPlusOneNetFlow + numberOfVerticesWithNegativeOneNetFlow + numberofVerticesWithZeroNetFlow ==
                graph.vertices().size(); 
        
    }
    
    //Effects: returns sum of all integers found in c, if c not empty;
    //         else, returns zero.
    private static int sumIntCollection(Collection<Integer> c){
        int sum = 0;
        for(Integer i : c ){
            sum += i;
        }
        return sum;
    }
    
    //Effects: returns true iff. this.graph is weakly connected.
    private boolean isWeaklyConnected(){
        final Set<String> unvisitedVertices = this.graph.vertices();
        final Queue<String> queque = new ArrayDeque<>();
        
        if(unvisitedVertices.isEmpty()) return true;
        
        String source = unvisitedVertices.iterator().next(); //get any vertex in this.graph
        unvisitedVertices.remove(source);
        queque.add(source);
        
        while(!queque.isEmpty()){
            String src = queque.remove();
            Set<String> adjVertices = new HashSet<>();
            adjVertices.addAll(graph.sources(src).keySet());
            adjVertices.addAll(graph.targets(src).keySet());
            
            for(String adjVertex : adjVertices){
                if(unvisitedVertices.contains(adjVertex)){
                    queque.add(adjVertex);
                    unvisitedVertices.remove(adjVertex);
                }
            }
        }
        
        return unvisitedVertices.isEmpty();
    }
    
    //Requires: w1 and w2 to be non empty lower case
    //Effects: returns bridge word connecting w1 to w2 in this.graph as defined in the spec, if there is any; else, 
    //         returns empty string
    private String getMaximalBridgeWord(String w1, String w2){
        String bridgeWord = "";
        int maxPathWeight = 0;
        
        Set<String> bridges = new HashSet<>(graph.targets(w1).keySet());
        bridges.retainAll(graph.sources(w2).keySet());

        for(String b : bridges){
            final int pathWeight = 
                    graph.sources(b).get(w1) + graph.targets(b).get(w2);
            
            if(pathWeight > maxPathWeight){
                bridgeWord = b;
                maxPathWeight = pathWeight;
            }
            
        }
        
        return bridgeWord;
    }
}