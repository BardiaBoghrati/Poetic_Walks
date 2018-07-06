/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    //   TODO
    
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
     * Testing Vertex...
     */
    
    // Testing strategy for Vertex
    //   setTraget(v,u,w):
    //      case w > 0 ( adding or changing an edge ):
    //          v --> u already exists (change edge):
    //              1. v == u (reflexive edge),
    //              2. v !== u
    //          v --> u does not exists (add edge):
    //                  3. there is already an edge v --> x where x.label() = u.label(), x != u 
    //                  4. there is already an edge x --> u where x.label() = v.label(), x != u
    //                  5. none of the above
    //      case w = 0 (remove edge):
    //          v --> u already exists
    //              6. v == u (reflexive edge)
    //              7. v != u
    //          v --> u does not exist:
    //              8. there is already an edge v --> x where x.label() = u.label()
    //              9. there is already an edge x --> u where x.label() = v.label()
    //              10. none of the above
    //    sources(v):
    //      number of sources: 0 (1.1), 1 (1.2), >1 (1.3)
    //      v is in v.sources() (2.1), v is not in v.sources() (2.2);
    //      there is distinct vertices u,x in v.sources() with same label (3.1), else (3.2);
    //    targets(v):
    //      number of targets(): 0 (1.1), 1 (1.2), >1 (1.3)
    //      v is in v.targets() (2.1), v is not in v.targets() (2.2);
    //      there is distinct vertices u,x in v.targets() with same label (3.1), else (3.2);
    //    label(v): only one partition for this
    //    toString(v):
    //       v has : no targets (1.1), single target (1.2), multiple targets (1.3)
    //       v has: no sources (2.1), single sources (2.2), multiple sources (2.3)
    //       the list constituting second element of v's string representation has repeated elements (3.1),
    //       else (3.2);
    //       the list constituting thirds element of v's string representation has repeated elements (4.1),
    //       else (4.2);
    
    //covers:
    //setTargets(): 1
    //sources(): 1.2, 2.1, 3.2
    //targets(): 1.2, 2.1, 3.2
    //toString(): 1.2, 2.2, 3.2, 4.2
    @Test
    public void testChangeReflexiveEdge(){
        Vertex v = new Vertex("a");
        
        v.setTarget(v, 1);
        assertEquals(1, v.setTarget(v, 2));
        
        assertEquals(v.sources().size(), 1);
        assertEquals(v.sources().get(v), new Integer(2));
        assertEquals(v.targets().size(), 1);
        assertEquals(v.targets().get(v), new Integer(2));
        assertEquals(v.label(), "a");
        assertEquals(v.toString(),"(a, [(a, 2)], [(a, 2)])");
    }
    
    //covers:
    //setTargets(): 2
    //sources(): 1.1, 1.2, 2.2, 3.2
    //targets(): 1.1, 1.2, 2.2, 3.2
    //toString(): 1.1, 1.2, 2.1, 2.2, 3.2, 4.2
    @Test
    public void testChangeProperEdge(){
        Vertex v = new Vertex("a");
        Vertex u = new Vertex("b");
        
        assertEquals(0, v.setTarget(u, 1));
        assertEquals(1,v.setTarget(u, 2));
        
        assertEquals(v.sources().size(), 0);
        assertEquals(v.targets().size(), 1);
        assertEquals(v.targets().get(u), new Integer(2));
        assertEquals(v.label(), "a");
        assertEquals(v.toString(), "(a, [(b, 2)], [])" );
        
        assertEquals(u.sources().size(), 1);
        assertEquals(u.sources().get(v), new Integer(2));
        assertEquals(u.targets().size(), 0);
        assertEquals(u.label(), "b");
        assertEquals(u.toString(), "(b, [], [(a, 2)])");      
    }
    
    @Test
    public void testTargetsWithSameLabelDifferentWeight(){
        Vertex v = new Vertex("a");
        Vertex u = new Vertex("b");
        Vertex x = new Vertex("b");
        
        v.setTarget(u, 1);
        v.setTarget(x, 2);
        
        assertEquals(0, v.sources().size());
        assertEquals(2, v.targets().size());
        assertEquals(new Integer(1), v.targets().get(u));
        assertEquals(new Integer(2), v.targets().get(x));
        assertTrue(v.toString().equals("(a, [(b, 1), (b, 2)], [])") ||
                v.toString().equals("(a, [(b, 2), (b, 1)], [])"));
        
    }
    
    @Test
    public void testAddTargetsWithSameLabelSameWeight(){
        Vertex v = new Vertex("a");
        Vertex u = new Vertex("b");
        Vertex x = new Vertex("b");
        
        v.setTarget(u, 1);
        v.setTarget(x, 1);
        
        assertEquals(0, v.sources().size());
        assertEquals(2, v.targets().size());
        assertEquals(new Integer(1), v.targets().get(u));
        assertEquals(new Integer(1), v.targets().get(x));
        assertEquals("(a, [(b, 1), (b, 1)], [])", v.toString());
    }
    
    @Test
    public void testAddSourcesWithSameLabelDifferentWeight(){
        Vertex v = new Vertex("a");
        Vertex u = new Vertex("b");
        Vertex x = new Vertex("b");
        
        u.setTarget(v, 1);
        x.setTarget(v, 2);
        
        assertEquals(0, v.targets().size());
        assertEquals(2, v.sources().size());
        assertEquals(new Integer(1), v.sources().get(u));
        assertEquals(new Integer(2), v.sources().get(x));
        assertTrue(v.toString().equals("(a, [], [(b, 1), (b, 2)])") ||
                v.toString().equals("(a, [], [(b, 2), (b, 1)])"));
    }
    
    @Test
    public void testAddSourcesWithSameLabelSameWeight(){
        Vertex v = new Vertex("a");
        Vertex u = new Vertex("b");
        Vertex x = new Vertex("b");
        
        u.setTarget(v, 1);
        x.setTarget(v, 1);
        
        assertEquals(0, v.targets().size());
        assertEquals(2, v.sources().size());
        assertEquals(new Integer(1), v.sources().get(u));
        assertEquals(new Integer(1), v.sources().get(x));
        assertEquals("(a, [], [(b, 1), (b, 1)])", v.toString());
    }
    
    @Test
    public void testAddReflexiveEdge(){
        Vertex v = new Vertex("a");
        
        assertEquals(0, v.setTarget(v, 1));
        
        assertEquals(1,v.targets().size());
        assertEquals(new Integer(1), v.targets().get(v));
        assertEquals(1,v.sources().size());
        assertEquals(new Integer(1), v.sources().get(v));
        assertEquals("(a, [(a, 1)], [(a, 1)])", v.toString());
    }
    
    @Test
    public void testRemoveReflexiveEdge(){
        Vertex v = new Vertex("a");
        
        v.setTarget(v, 1);
        assertEquals(1, v.setTarget(v, 0));
        
        assertEquals(0, v.targets().size());
        assertEquals(0, v.sources().size());
        assertEquals("(a, [], [])", v.toString());
    }
    
    @Test
    public void testRemoveProperEdge(){
        Vertex v = new Vertex("a");
        Vertex u = new Vertex("b");
        
        v.setTarget(u, 1);
        assertEquals(1, v.setTarget(u, 0));
        
        assertEquals(0, v.targets().size());
        assertEquals(0, v.sources().size());
        assertEquals("(a, [], [])", v.toString());
        
        assertEquals(0, u.targets().size());
        assertEquals(0, u.sources().size());
        assertEquals("(b, [], [])", u.toString());
    }
    
    @Test
    public void testRemoveOneOfTheTargetsWithAGivenLabel(){
        Vertex v = new Vertex("a");
        Vertex u = new Vertex("b");
        Vertex x = new Vertex("b");
        
        v.setTarget(u, 1);
        v.setTarget(x, 1);
        v.setTarget(u, 0);
        
        assertEquals(1, v.targets().size());
        assertEquals(new Integer(1), v.targets().get(x));
        assertEquals(null, v.targets().get(u));
        assertEquals("(a, [(b, 1)], [])", v.toString());   
    }
    
    @Test
    public void testRemoveOneOfTheSourcesWithAGivenLabel(){
        Vertex v = new Vertex("a");
        Vertex u = new Vertex("b");
        Vertex x = new Vertex("b");
        
        u.setTarget(v, 1);
        x.setTarget(v, 1);
        u.setTarget(v, 0);
        
        assertEquals(1, v.sources().size());
        assertEquals(new Integer(1), v.sources().get(x));
        assertEquals(null, v.sources().get(u));
        assertEquals("(a, [], [(b, 1)])", v.toString());      
    }
    
    @Test
    public void testRemoveNonExistingTargetWithATargetWithSameLabel(){
        Vertex v = new Vertex("a");
        Vertex u = new Vertex("b");
        Vertex x = new Vertex("b");
        
        v.setTarget(x, 1);
        v.setTarget(u, 0);
        
        assertEquals(1, v.targets().size());
        assertEquals(new Integer(1), v.targets().get(x));
        assertEquals(null, v.targets().get(u));
        assertEquals("(a, [(b, 1)], [])", v.toString()); 
    }
    
    @Test
    public void testRemoveNonExistingSourceWithASourceWithSameLabel(){
        Vertex v = new Vertex("a");
        Vertex u = new Vertex("b");
        Vertex x = new Vertex("b");
        
        x.setTarget(v, 1);
        u.setTarget(v, 0);
        
        assertEquals(1, v.sources().size());
        assertEquals(new Integer(1), v.sources().get(x));
        assertEquals(null, v.sources().get(u));
        assertEquals("(a, [], [(b, 1)])", v.toString());  
    }
    
    @Test
    public void testRemoveNonExistingReflexiveEdge(){
        Vertex v = new Vertex("a");
        
        assertEquals(0, v.setTarget(v, 0));
        
        assertEquals(0, v.sources().size());
        assertEquals(0, v.targets().size());
        assertEquals("(a, [], [])", v.toString());
    }
    
}
