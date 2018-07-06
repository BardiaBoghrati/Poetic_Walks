/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph implements Graph<String> {
    
    private final List<Vertex> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   Represents a graph with vertex set determined by set of labels of elements found in vertices
    //   and edge set determined by every pair of labels whose their corresponding elements in vertices have an edge between them.
    //   The weight of those edges is determined by the weight of the edges between the corresponding elements of vertices.
    // Representation invariant:
    //   Each element of vertices has a distinct label from the rest.
    //   Any element in vertices can only have an edge between it and an element in vertices
    // Safety from rep exposure:
    //   vertices is a private field, pointing to a mutable list with mutable elements. All parameters of public methods are
    //   immutable. Mutable objects returned by vertices(), sources(), and targets(), are fresh HashMap/HashSet constructions with 
    //   immutable parameters.
    
    public ConcreteVerticesGraph(){
        checkRep();
    }
    
    private void checkRep(){
        checkDistinctLabels();
        checkEdgeInvariant();
    }
    
    //asserts that every element in vertices has a label distinct from other labels
    private void checkDistinctLabels(){
        Set<String> labelSet = new HashSet<>();
        
        for(Vertex vertex : vertices){
            assert !labelSet.contains(vertex.label());
            labelSet.add(vertex.label());
        }
    }
    
    // asserts that every element in vertices has only edges to elements in vertices
    private void checkEdgeInvariant(){
        for(Vertex vertex : vertices){
            for(Vertex target : vertex.targets().keySet()){
                assert vertices.contains(target);
            }
        }
    }
    
    @Override public boolean add(String vertex) {
        if(indexOf(vertex) >= 0){
            return false;
        }
        
        vertices.add(new Vertex(vertex));
        checkRep();
        return true;
    }
    
    @Override public int set(String source, String target, int weight) {
        final int indexOfSource = indexOf(source);
        
        if(weight > 0){
            if(indexOfSource >= 0){
                Vertex src = vertices.get(indexOfSource);
                final int indexOfTarget = indexOf(target);
                
                if(indexOfTarget >= 0){
                    Vertex trg = vertices.get(indexOfTarget);
                    int prevWeight = src.setTarget(trg, weight);
                    checkRep();
                    return prevWeight;
                }else{
                    Vertex trg = new Vertex(target);
                    vertices.add(trg);
                    int prevWeight = src.setTarget(trg, weight);
                    checkRep();
                    return prevWeight;
                }
            }else{
                Vertex src = new Vertex(source);
                vertices.add(src);
                final int indexOfTarget = indexOf(target);
                
                if(indexOfTarget >= 0){
                    Vertex trg = vertices.get(indexOfTarget);
                    int prevWeight = src.setTarget(trg, weight);
                    checkRep();
                    return prevWeight;
                }else{
                    Vertex trg = new Vertex(target);
                    vertices.add(trg);
                    int prevWeight = src.setTarget(trg, weight);
                    checkRep();
                    return prevWeight;
                }
            }
        }else{
            final int indexOfTarget = indexOf(target);
            
            if(indexOfSource >= 0 && indexOfTarget >= 0){
                Vertex src = vertices.get(indexOfSource);
                Vertex trg = vertices.get(indexOfTarget);
                
                int prevWeight = src.setTarget(trg, weight);
                checkRep();
                return prevWeight;
            }
            
            checkRep();
            return 0;
        }

    }
    
    @Override public boolean remove(String vertex) {
        final int indexOfVertex = indexOf(vertex);
        
        if(indexOfVertex < 0){
            checkRep();
            return false;
        }
        
        Vertex v = vertices.get(indexOfVertex);
        
        for(Vertex target : v.targets().keySet()){
            v.setTarget(target, 0);
        }
        
        for(Vertex source : v.sources().keySet()){
            source.setTarget(v, 0);
        }
        
        vertices.remove(indexOfVertex);
        
        checkRep();
        return true;
    }
    
    @Override public Set<String> vertices() {
        final Set<String> labelSet = new HashSet<>();
        
        for(Vertex vertex : vertices){
            labelSet.add(vertex.label());
        }
        
        checkRep();
        return labelSet;
    }
    
    @Override public Map<String, Integer> sources(String target) {
        final Map<String, Integer> sourcesMap = new HashMap<>();
        final int indexOfTarget = indexOf(target);
        
        if(indexOfTarget >= 0){
            final Vertex trg = vertices.get(indexOfTarget);
            
            for(Entry<Vertex, Integer> entry : trg.sources().entrySet()){
                sourcesMap.put(entry.getKey().label(), entry.getValue());
            }
        }
        
        checkRep();
        return sourcesMap;
        
        
    }
    
    @Override public Map<String, Integer> targets(String source) {
        final Map<String, Integer> targetsMap = new HashMap<>();
        final int indexOfSource = indexOf(source);
        
        if(indexOfSource >= 0){
            final Vertex src = vertices.get(indexOfSource);
            
            for(Entry<Vertex, Integer> entry : src.targets().entrySet()){
                targetsMap.put(entry.getKey().label(), entry.getValue());
            }
        }
        
        checkRep();
        return targetsMap;
        
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
    @Override
    public String toString() {
        final Set<String> edgeSet = new HashSet<>();
        
        for(Vertex vertex : vertices){
            for(Entry<Vertex, Integer> entry : 
                vertex.targets().entrySet()){
                
                final String edgeTuple = String.format("(%s, %s, %s)", vertex.label(),
                        entry.getKey().label(), entry.getValue());
                        
                edgeSet.add(edgeTuple);
            }
        }
        
        final String toStringEdgeSet = edgeSet.toString().replace('[', '{').replace(']', '}');
        final String toStringVertexSet = vertices().toString().replace('[', '{').replace(']', '}');
        final String toString = "(" + toStringVertexSet + ", " +
                toStringEdgeSet + ")";
        
        checkRep();
        return toString;
        
    }
    
    //Effects: returns index i of an element in vertices with label equal to
    //  vertex, if there is any; else, returns -1.
    private int indexOf(String vertex){
        for(int i = 0; i < vertices.size(); i++){
            if(vertices.get(i).label().equals(vertex)){
                return i;
            }
        }
        return -1;
    }
}

/**
 * Vertex is a mutable type representing a labeled vertex in directed weighted graph. Each instance of vertex
 * has an associated immutable label. Only a single edge can exist between any pair of vertices, and each edge has exactly one corresponding weight.
 * For any v, u in Vertex and integer weight w, (v,w) is in u.sources() iff. (u,w) is in v.targets(). An edge v --> u with weight w exist iff. (u,w) is 
 * in v.targets().
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex {
    
    private final String label;
    private final Map<Vertex, Integer> targets = new HashMap<>();
    private final Map<Vertex, Integer> sources = new HashMap<>();
    
    // Abstraction function:
    //   represents a vertex with label = this.label and where
    //   targets represents {(v,z) in Vertex * Integer: this --> v and z = weight(this --> v)}
    //   sources represents {(v,z) in Vertex * Integer: v --> this and z = weight(v --> this)}
    // Representation invariant:
    //   for any entry (v,z) in this.targets, (this,z) is in v.sources
    //   for any entry (v,z) in this.sources, (this,z) is in v.targets
    //   the values of the map entries are non-negative integers
    // Safety from rep exposure:
    //   All fields are private. this.lable is an immutable string. this.targets and this.sources are mutable maps, but 
    //   these maps are never directly passed in and on every return of data of type map a defensive copy is made before the return.
    //   The only threat remaining is the key parameters of these maps are mutable and exposed to the client; but,
    //   although Vertices are mutable, they inherit the Object Equal() and hashCode() methods which remain unchanged with mutation, and 
    //   hence their exposer to clients does not those not threaten the rep invariant of these maps. Also, rep invariant of this
    //   ensures that changes made to any vertex u through u.setTarget(this,w) is reflected in representation of this, and vice versa. Therefore,
    //   the adjacency relationship between this and any of the keys in its rep remains consistent, even when these keys are
    //   changed independently.
    
    /**
     * Creates a labeled vertex with no edges attached to it
     * @param label the label assigned to this vertex
     */
    public Vertex(String label){
        this.label = label;
    }
    
    private void checkRep(){
        checkTargetEntries();
        checkSourceEntries();
        
    }
    
    //check rep invariant constraints imposed on targets of this vertex
    private void checkTargetEntries(){
        for(Entry<Vertex, Integer> e : targets.entrySet()){
            assert e.getValue() > 0;
            Vertex target = e.getKey();
            assert target.sources.get(this).equals(e.getValue());
        } 
    }
    
    // check rep invariant constraints imposed on sources of this vertex
    private void checkSourceEntries(){
        for(Entry<Vertex, Integer> e : sources.entrySet()){
            assert e.getValue() > 0;
            Vertex source = e.getKey();
            assert source.targets.get(this).equals(e.getValue());
        } 
    }
    
    /**
     *<pre>
     * add, change, or remove edge from this to v
     * 
     * 
     * case weight > 0:
     *  if there is already an edge this --> v, change weight(this-->v) = weight;
     *  else add edge this --> v and set weight(this-->v) = weight.
     * case weight = 0:
     *  remove edge this --> v. 
     *</pre>
     *     
     * @param v target vertex at the head of the edge
     * @param weight non-negative weight of the edge
     * @return previous weight of edge this --> v or 0 if there was no such edge
     */
    public int setTarget(Vertex v, int weight){
        final int prevWeight = targets.getOrDefault(v, 0);
        if(weight > 0){
            targets.put(v, weight);
            v.sources.put(this, weight);
        }else{
            targets.remove(v);
            v.sources.remove(this);
        }
        checkRep();
        return prevWeight;
    }

    /**
     * Get all the Vertices with an edge to this.
     * @return
     * A Map with keys containing set of all Vertices that are tails of an edge to this with corresponding value
     * representing the weight of that edge.
     */
    public Map<Vertex,Integer> sources(){
         final Map<Vertex,Integer> sources = new HashMap<>(this.sources);
         checkRep();
         return sources;
    }
    
    /**
     * Get all the Vertices with an edge to this
     * @return
     * A Map with keys containing set of all Vertices that are head of an edge from this with corresponding value
     * representing the weight of that edge.
     */
    public Map<Vertex,Integer> targets(){
        final Map<Vertex,Integer> targets = new HashMap<>(this.targets);
        checkRep();
        return targets;
    }
    
    /**
     * @return the label associated with this vertex
     */
    public String label(){
        return label;
    }
    
    /**
     * The string representation of this Vertex is a 3 element tuple bracketed with "()". First element is the label of this Vertex. Second element is 
     * list of pairs (u,w) --in unspecified order-- where u is label of a vertex such this --> u with weight w, all enclosed with "[]". Third is a list of pairs (u,w) where u --> this with weight w.
     * <br>
     * Example:
     * suppose 3 distinct vertices v,u,x with v.label() = "a", u.label() = "b", x.label() = "b" and consider what follows:<br>
     * v.setTarget(u,1); v.setTarget(x,1);
     * This is an possible outcome of calling toString on this vertices
     * v.toString() = "(a, [(b,1), (b,1)], [])"
     * u.toString() = "(b, [], [(a,1)])"
     * x.toString() = "(b, [], [(a,1)])"
     * <br>
     * notices that repeats are allowed since u and x are distinct vertices. They just have the same label.
     * @return a string representation of this Vertex
     */
    @Override public String toString(){
        final List<String> targetList = new ArrayList<>();
        for(Entry<Vertex, Integer> e : targets.entrySet()){
            targetList.add(String.format("(%s, %s)", e.getKey().label, e.getValue()));
        }
        
        final List<String> sourceList = new ArrayList<>();
        for(Entry<Vertex, Integer> e : sources.entrySet()){
            sourceList.add(String.format("(%s, %s)", e.getKey().label, e.getValue()));
        }
        
        final String toString = String.format("(%s, %s, %s)", label, targetList, sourceList);
        checkRep();
        return toString;
    }
    
}
