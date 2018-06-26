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
        return new ConcreteEdgesGraph();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    //  number of vertices: 0, 1 , >1;
    //
    //  number of edges: 0, 1, >1; 
    //
    //  most recent mutating operations producing the graph are:
    //      note: z+ denotes positive integer
    //
    //      1. graph g does not contain x: g.add(x);
    //      2. graph g does not contain x: g.add(x); g.add(x);
    //      3. graph g does not contain x: g.set(a,b,z+); g.add(x); where either a or b = x
    //      4. graph g does not contain x: g.remove(x);
    //      5. graph g contains x and x has no edges incident to it: g.remove(x);
    //      6. graph g contains x and x has at least one edge incident to it: g.remove(x);
    //      7. graph g does not contains x and y: set(x,y,z+);
    //          1. x = y
    //          2. x != y
    //      8. graph g contains only one of x or y: set(x,y,z+); x != y
    //      9. graph g contains x,y but they are not adjacent: set(x,y,z+);
    //          1. x = y
    //          2. x != y
    //      10. graph contains x,y and they are adjacent: set(x,y,z+);
    //      11. graph g does not contains x and y: set(x,y,0);
    //      12. graph g contains x,y but they are not adjacent: set(x,y,0); x != y
    //      13. graph g contains x,y and they are adjacent: set(x,y,0);
    //          1. x = y
    //          2. x != y      
    //      14. none of the above
    
    //tests for ConcreteEdgesGraph.toString():
    
/*    @Test
    public void testAddToEmpty(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.add("a");
        
        assertEquals(graph.toString(), "({a}, {})");
    }
    
    @Test
    public void testAddExistingVertex(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.add("a");
        graph.add("a");
        
        assertEquals(graph.toString(), "({a}, {})");
    }
    
    @Test
    public void testAddEdgeThenAddVertexIncidentToIt(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.set("a","b",1);
        graph.add("a");
        
        assertTrue(graph.toString().equals("({a, b}, {(a, b, 1)})") ||
                graph.toString().equals("({b, a}, {(a, b, 1)})"));
    }
    
    @Test 
    public void testRemoveFromEmpty(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.remove("a");
        
        assertEquals(graph.toString(), "({}, {})");
    }
    
    @Test
    public void testRemoveVertexWithNoEdge(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.add("a");
        graph.remove("a");
        
        assertEquals(graph.toString(), "({}, {})");
    }
    
    @Test
    public void testRemoveVertexWithEdge(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.set("a","b",1);
        graph.remove("a");
        
        assertEquals(graph.toString(), "({b}, {})");
    }
    
    @Test
    public void testAddReflexiveEdgeNonExistingVertex(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.set("a", "a", 1);
        
        assertEquals(graph.toString(), "({a}, {(a, a, 1)})");
    }
    
    @Test
    public void testAddEdgeNonExistingVertex(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.set("a", "b", 1);
        
        assertTrue(graph.toString().equals("({a, b}, {(a, b, 1)})") ||
                graph.toString().equals("({b, a}, {(a, b, 1)})"));
    }
    
    @Test
    public void testAddEdgeOnlyOneExistingEdge(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.add("a");
        graph.set("a", "b", 1);
        
        assertTrue(graph.toString().equals("({a, b}, {(a, b, 1)})") ||
                graph.toString().equals("({b, a}, {(a, b, 1)})"));
    }
    
    @Test
    public void testAddReflexiveEdgeExistingVertex(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.add("a");
        graph.set("a", "a", 1);
        
        assertEquals(graph.toString(), "({a}, {(a, a, 1)})");
    }
    
    @Test
    public void testAddEdgeExistingVetices(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.add("a");
        graph.add("b");
        graph.set("a", "b", 1);
        
        assertTrue(graph.toString().equals("({a, b}, {(a, b, 1)})") ||
                graph.toString().equals("({b, a}, {(a, b, 1)})"));
    }
    
    @Test
    public void testChangeEdge(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.set("a", "b", 1);
        graph.set("a", "b", 2);
        
        assertTrue(graph.toString().equals("({a, b}, {(a, b, 2)})") ||
                graph.toString().equals("({b, a}, {(a, b, 2)})"));
    }
    
    @Test
    public void testRemoveEdgeNoneExistingVertices(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.set("a", "b", 0);
        
        assertEquals(graph.toString(), "({}, {})");
    }
    
    @Test
    public void testRemoveNoneExistingEdgeExistingVertices(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.add("a");
        graph.add("b");
        graph.set("a", "b", 0);
        
        assertEquals(graph.toString(), "({a, b}, {})");
    }
    
    @Test
    public void testRemoveReflexiveEdge(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.set("a", "a", 1);
        graph.set("a", "a", 0);
        
        assertEquals(graph.toString(), "({a}, {})");
    }
    
    @Test
    public void testRemoveProperEdge(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.set("a", "b", 1);
        graph.set("a", "b", 0);
        
        assertEquals(graph.toString(), "({a,b}, {})");
    }
    
    @Test
    public void testTwoEdges(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.set("a", "b", 1);
        graph.set("b", "a", 2);
        
        assertTrue(graph.toString().equals("({a, b}, {(a, b, 1), (b, a, 2)})") ||
                graph.toString().equals("({a, b}, {(b, a, 2), (a, b, 1)})") ||
                graph.toString().equals("({b, a}, {(a, b, 1), (b, a, 2)})") ||
                graph.toString().equals("({b, a}, {(b, a, 2), (a, b, 1)})"));
    }*/
    
    //alternative to above
    @Test
    public void testNoVerticesNoEdges(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        assertEquals(graph.toString(),"({}, {})");
    }
    
    @Test
    public void testOneVertexNoEdges(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.add("a");
        
        assertEquals(graph.toString(),"({a}, {})");
    }
    
    @Test
    public void testOneVertexOneEdge(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.set("a", "a", 1);
        
        assertEquals(graph.toString(),"({a}, {(a, a, 1)})");
    }
    
    @Test
    public void testTwoVerticesNoEdges(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.add("a");
        graph.add("b");
        
        assertTrue(graph.toString().equals("({a, b}, {})") ||
                graph.toString().equals("({b, a}, {})")); 
    }
    
    @Test
    public void testTwoVerticesOneEdge(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
        graph.set("a", "b", 1);
        
        assertTrue(graph.toString().equals("({a, b}, {(a, b, 1)})") ||
                graph.toString().equals("({b, a}, {(a, b, 1)})"));
    }
    
    @Test
    public void testTwoVerticesTwoEdges(){
        Graph<String> graph = new ConcreteEdgesGraph();
        
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
    
    
    public void testDistinctVerticesWeightOne(){
        Edge e = new Edge("a","b",1);
        
        assertEquals(e.head(),"b");
        assertEquals(e.tail(),"a");
        assertEquals(e.weigt(),1);
        assertEquals(e.toString(),"(a, b, 1)");
    }
    
    public void testSameVerticesWeightGreaterThanOne(){
        Edge e = new Edge("a","a",10);
        
        assertEquals(e.head(),"a");
        assertEquals(e.tail(),"a"); 
        assertEquals(e.weigt(),10);
        assertEquals(e.toString(),"(a, a, 10)"); 
    }
    
}
