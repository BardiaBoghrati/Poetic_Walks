/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<String>();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    //  number of vertices: 0, 1 , >1;
    //
    //  number of edges: 0, 1, >1; 
    
    //tests for ConcreteEdgesGraph.toString():
    
    @Test
    public void testNoVerticesNoEdges(){
        Graph<String> graph = emptyInstance();
        
        assertEquals(graph.toString(),"({}, {})");
    }
    
    @Test
    public void testOneVertexNoEdges(){
        Graph<String> graph = emptyInstance();
        
        graph.add("a");
        
        assertEquals(graph.toString(),"({a}, {})");
    }
    
    @Test
    public void testOneVertexOneEdge(){
        Graph<String> graph = emptyInstance();
        
        graph.set("a", "a", 1);
        
        assertEquals(graph.toString(),"({a}, {(a, a, 1)})");
    }
    
    @Test
    public void testTwoVerticesNoEdges(){
        Graph<String> graph = emptyInstance();
        
        graph.add("a");
        graph.add("b");
        
        assertTrue(graph.toString().equals("({a, b}, {})") ||
                graph.toString().equals("({b, a}, {})")); 
    }
    
    @Test
    public void testTwoVerticesOneEdge(){
        Graph<String> graph = emptyInstance();
        
        graph.set("a", "b", 1);
        
        assertTrue(graph.toString().equals("({a, b}, {(a, b, 1)})") ||
                graph.toString().equals("({b, a}, {(a, b, 1)})"));
    }
    
    @Test
    public void testTwoVerticesTwoEdges(){
        Graph<String> graph = emptyInstance();
        
        graph.set("a", "b", 1);
        graph.set("b", "a", 2);
        
        assertTrue(graph.toString().equals("({a, b}, {(a, b, 1), (b, a, 2)})") ||
                graph.toString().equals("({a, b}, {(b, a, 2), (a, b, 1)})") ||
                graph.toString().equals("({b, a}, {(a, b, 1), (b, a, 2)})") ||
                graph.toString().equals("({b, a}, {(b, a, 2), (a, b, 1)})"));
    }
    
    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    //  partition on vertices: tail = head, tail != head
    //  partition on weight: 1, >1
    
    @Test
    public void testDistinctVerticesWeightOne(){
        Edge<String> e = new Edge<>("a","b",1);
        
        assertEquals(e.head(),"b");
        assertEquals(e.tail(),"a");
        assertEquals(e.weigt(),1);
        assertEquals(e.toString(),"(a, b, 1)");
    }
    
    @Test
    public void testSameVerticesWeightGreaterThanOne(){
        Edge<String> e = new Edge<>("a","a",10);
        
        assertEquals(e.head(),"a");
        assertEquals(e.tail(),"a"); 
        assertEquals(e.weigt(),10);
        assertEquals(e.toString(),"(a, a, 10)"); 
    }
    
}
