/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph implements Graph<String> {
    
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();
    
    // Abstraction function:
    //   represents graph with vertex set equal to vertices and
    //   edge set equal to the set containing every edge found in edges
    // Representation invariant:
    //   vertices must contain every vertex incident to an Edge in edges
    //   For any vertices h , t, only one Edge in edges can have h as head and t as tail
    // Safety from rep exposure:
    //   All fields are private. String and Edge objects contained in Set and List objects pointed to by the fields are immutable.
    //   The Set and List objects themselves are mutable but they are never passed in by any operation, as all operations eigher have
    //   immutable parameters or none at all. The rep is never returned either as vertices() returns a fresh defensive copy. The mutable Map
    //   objects returned do not have access to vertices and edges in their rep, as they are newly constructed HashMaps.
    
    // TODO constructor
    public ConcreteEdgesGraph(){
        checkRep();
    }
    
    // TODO checkRep
    private void checkRep(){
        assertEdgeVertexCompatibility();
        assertNoDuplicateEdges();
    }
    
    private void assertEdgeVertexCompatibility(){
        for(Edge e : edges){
            assert vertices.contains(e.tail()) && 
                vertices.contains(e.head());
        }
    }
    
    private void assertNoDuplicateEdges(){
        Map<String,Set<String>> targets = new HashMap<>();
        
        for(Edge e : edges){
            Set<String> adjacentHeads = targets.getOrDefault(e.tail(), new HashSet<>());
            
            assert !adjacentHeads.contains(e.head());
            adjacentHeads.add(e.head());
            targets.put(e.tail(), adjacentHeads);
        }
    }
    
    @Override public boolean add(String vertex) {
        final boolean containsVertex = vertices.add(vertex);
        checkRep();
        return containsVertex;
    }
    
    @Override public int set(String source, String target, int weight) {
        int prevWeight = 0;
        for(Edge e : edges){
            if(e.tail().equals(source) && 
                    e.head().equals(target)){
                prevWeight = e.weigt();
                edges.remove(e);
                break;
            }
        }
        
        if(weight > 0){
            edges.add(new Edge(source, target, weight));
            vertices.add(source);
            vertices.add(target);
        }
        
        checkRep();
        return prevWeight;
    }
    
    @Override public boolean remove(String vertex) {
        Iterator<Edge> listItr = edges.iterator();
        
        while(listItr.hasNext()){
            Edge e = listItr.next();
            
            if(e.tail().equals(vertex) || 
                    e.head().equals(vertex)){
                listItr.remove();
            }
        }
        
        final boolean containsVertex = vertices.remove(vertex);
        checkRep();
        return containsVertex;
    }
    
    @Override public Set<String> vertices() {
        return new HashSet<String>(vertices);
    }
    
    @Override public Map<String, Integer> sources(String target) {
        final Map<String, Integer> sources = new HashMap<>();
        
        for(Edge e : edges){
            if(e.head().equals(target)){
                sources.put(e.tail(), e.weigt());
            }
        }
        
        checkRep();
        return sources;
    }
    
    @Override public Map<String, Integer> targets(String source) {
        final Map<String, Integer> targets = new HashMap<>();
        
        for(Edge e : edges){
            if(e.tail().equals(source)){
                targets.put(e.head(), e.weigt());
            }
        }
        
        checkRep();
        return targets;
    }
    
    /**
     * Returns a string representation of this Graph. The representation is of form:
     * "(V,E)" where V is a list of vertex labels of this graph appearing exactly once in unspecified order and enclosed by "{}",
     * and E is a list of all edges with same form and constraints. Each edge in E is of form "(tail, head, weight)".
     * 
     * example:
     * suppose graph G is constructed as such:
     *  G = emptyGraph(); G.set(b,a,1); G.set(a,c,2); G.add(d);
     * then a possible return string rep is:
     *  G.toString() = ({b, a, c, d}, {(a, c, 2), (b , c, 1)})
     *  
     *  @return a string representation of this graph
     */
    @Override public String toString() {
        return ("(" + vertices.toString() + ", " +
                edges.toString() + ")").replace('[', '{').replace(']', '}');
    }

    
    
}

/**

 * An Immutable weighted directed edge with labeled vertices. An edge is represented by ordered pair 
 * of vertices (x,y) representing an edge x-->y with positive weight. x is referred to as tail of the 
 * edge and y is referred to as head of the edge. 
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge {
    
    private final  String tail;
    private final String head;
    private final int weight;
    
    // Abstraction function:
    //   represents directed edge tail --> head
    // Representation invariant:
    //   weight > 0
    // Safety from rep exposure:
    //   all fields are private and immutable
    
    /**
     * construct an weighted edge tail --> head
     * @param tail label representing tail of this edge
     * @param head label representing head of this edge
     * @param weight weight of this edge; must be a positive value (>0)  
     */
    public Edge(String tail, String head, int weight){
        this.tail = tail;
        this.head = head;
        this.weight = weight;
        
        checkRep();
    }
    
    private void checkRep(){
        assert weight > 0;
    }
    
    /**
     * 
     * @return a label representing the head of this edge
     */
    public String head(){
        return head;
    };
    
    /**
     * 
     * @return a label representing the tail of this edge
     */
    public String tail(){
        return tail;
    }
    /**
     * 
     * @return a positive integer representing weight of this edge
     */
    public int weigt(){
        return weight;
    }
    
    /**
     * Returns a string representation of this edge in the form: "(tail, head, weight)"
     * @return a string representation of this edge
     */
    @Override public String toString() {
        return String.format("(%s, %s, %s)", tail, head, weight);
    }; 
}
