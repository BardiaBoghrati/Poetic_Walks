/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;


import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
/* Testing strategy
      add():
          graph already contains the vertex being added:
              previous addition was through add() (1.1),
              previous addition was through set() (1.2);
          graph does not contain the vertex being added (2);
      set():
          add an edge (graph does not contain the edge):
              graph contains sources but not target (1.1),
              graph not contain source but contains target (1.2),
              graph contains neither vertices (1.3),
              graph contains both the vertices (1.4);
              
              add reflexive edge (1.5),
              else (1.6);
          change weight of edge (graph contains the specified edge):
              change the weight of an existing edge to a different value (2.1),
              set the weight to same value (2.2);
              
              change reflexive edge (2.3),
              else (2.4);
          remove an edge:
              the edge exist (3.1),
              the edge does not exist:
                  but both vertices exist (3.2.1),
                  least one vertex in missing (3.2.2);
              
              remove reflexive edge (3.3),
              else (3.4);
      remove():
          graph contains the vertex:
          
              number of out edges: 0 (1.1), 1 (1.2) , > 1 (1.3);
              
              number of in edges: 0 (1.4), 1 (1.5), >1 (1.6);
              
              vertex added through add() operation (1.7),
              vertex added through set() operation (1.8);
              
          graph does not contain the vertex (2);
          
      vertices():
          number of vertices in graph: 0 (1), 1 (2) , >1 (3); 
                  
          a vertex added twice to the graph (4),
          else (5);
      sources():
          graph contains target:
              number of sources: 0 (1.1), 1 (1.2), >1 (1.3);
          graph does not contain target (2)
          
          target vertex has an reflexive edge (3),
          else (4);
      target():
          graph contains source:
              number of targets: 0 (1.1), 1 (1.2), >1 (1.3);
          graph does not contain source (2);
          
          source vertex has an reflexive edge (3),
          else (4);
*/
    
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }

    /*
     * covers:
     * add(): 2
     * set():
     * remove():
     * vertices(): 2, 5
     * sources(): 1.1 , 4
     * targets(): 1.1, 4
     */
    @Test
    public void testAddToEmptyGraph(){
        Graph<String> graph = emptyInstance();
        
        assertTrue(graph.add("a"));
        assertTrue(graph.vertices().size() == 1);
        assertTrue(graph.vertices().contains("a"));
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("a").isEmpty());
    }
    /*
     * covers:
     * add(): 1.1
     * set():
     * remove(): 1.1, 1.4, 1.7
     * vertices(): 1, 2, 4
     * sources(): 1.1 , 2, 4
     * targets(): 1.1, 2, 4
     */
    @Test
    public void testDuplicated_Add_Remove(){

        Graph<String> graph = emptyInstance();
        
        graph.add("a");
        
        assertFalse(graph.add("a"));
        assertEquals(graph.vertices().size(),1);
        assertTrue(graph.vertices().contains("a"));
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("a").isEmpty());
        
        assertTrue(graph.remove("a"));
        assertTrue(graph.vertices().isEmpty());
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("a").isEmpty());
        
        assertFalse(graph.remove("a"));
        assertTrue(graph.vertices().isEmpty());
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("a").isEmpty());
    }
    /*
     * covers:
     * add(): 1.2
     * set(): 1.4
     * remove():
     * vertices(): 3, 4
     * sources(): 1.2
     * targets(): 1.2
     */
    @Test
    public void testAddEdgeThenAddVertexIncidentToIt(){
        Graph<String> graph = emptyInstance();
        
        graph.set("a", "b", 1);
        
        assertFalse(graph.add("a"));
        assertEquals(graph.vertices().size(), 2);
        assertTrue(graph.vertices().contains("a") &&
                graph.vertices().contains("b"));
    }
    /*
     * covers:
     * add(): 1.1
     * set(): 1.1
     * remove():
     * vertices(): 3
     * sources(): 1.2, 4
     * targets(): 1.2, 4
     */
    @Test
    public void testAddEdgeSourceExistsTragetDoesNot(){
        Graph<String> graph = emptyInstance();
        
        graph.add("a");
        
        assertEquals(graph.set("a", "b", 1),0);
        assertEquals(graph.vertices().size(),2);
        assertTrue(graph.vertices().contains("a") &&
                graph.vertices().contains("b"));
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("b").isEmpty());
        assertEquals(graph.targets("a").size(),1);
        assertEquals(graph.targets("a").get("b"),new Integer(1));
        assertEquals(graph.sources("b").size(),1);
        assertEquals(graph.sources("b").get("a"),new Integer(1));  
    }
    /*
     * covers:
     * add(): 1.1
     * set(): 1.2
     * remove():
     * vertices(): 3, 4
     * sources(): 1.2, 4
     * targets(): 1.2, 4
     */
    @Test
    public void testAddEdgeSourceNotExistTargetDoes(){
        Graph<String> graph = emptyInstance();
        
        graph.add("b");
        
        assertEquals(graph.set("a", "b", 1),0);
        assertEquals(graph.vertices().size(),2);
        assertTrue(graph.vertices().contains("a") &&
                graph.vertices().contains("b"));
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("b").isEmpty());
        assertEquals(graph.targets("a").size(),1);
        assertEquals(graph.targets("a").get("b"),new Integer(1));
        assertEquals(graph.sources("b").size(),1);
        assertEquals(graph.sources("b").get("a"),new Integer(1));  
    }
    /*
     * covers:
     * add(): 1.1
     * set(): 1.4
     * remove():
     * vertices(): 3, 4
     * sources(): 1.2, 4
     * targets(): 1.1, 4
     */
    @Test
    public void testAddEdgeBetweenExisitingVertices(){
        Graph<String> graph = emptyInstance();
        
        graph.add("a");
        graph.add("b");
        
        assertEquals(graph.set("a", "b", 1),0);
        assertEquals(graph.vertices().size(),2);
        assertTrue(graph.vertices().contains("a") &&
                graph.vertices().contains("b"));
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("b").isEmpty());
        assertEquals(graph.targets("a").size(),1);
        assertEquals(graph.targets("a").get("b"),new Integer(1));
        assertEquals(graph.sources("b").size(),1);
        assertEquals(graph.sources("b").get("a"),new Integer(1));
    }
    
    @Test
    public void testAddEdgeBetweenNonExistingVertices(){
        Graph<String> graph = emptyInstance();
        
        assertEquals(graph.set("a", "b", 1),0);
        assertEquals(graph.vertices().size(),2);
        assertTrue(graph.vertices().contains("a") &&
                graph.vertices().contains("b"));
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("b").isEmpty());
        assertEquals(graph.targets("a").size(),1);
        assertEquals(graph.targets("a").get("b"),new Integer(1));
        assertEquals(graph.sources("b").size(),1);
        assertEquals(graph.sources("b").get("a"),new Integer(1));
    }
    
    @Test
    public void testChangeEdgeWeight(){
        Graph<String> graph = emptyInstance();
        
        graph.set("a", "b", 2);
        
        assertEquals(graph.set("a", "b", 1),2);
        assertEquals(graph.vertices().size(),2);
        assertTrue(graph.vertices().contains("a") &&
                graph.vertices().contains("b"));
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("b").isEmpty());
        assertEquals(graph.targets("a").size(),1);
        assertEquals(graph.targets("a").get("b"),new Integer(1));
        assertEquals(graph.sources("b").size(),1);
        assertEquals(graph.sources("b").get("a"),new Integer(1));  
    }
    
    @Test
    public void testRemoveEdgeBetweenNonExistingVertices(){
        Graph<String> graph = emptyInstance();
        
        assertEquals(graph.set("a", "b",0), 0);
        assertTrue(graph.vertices().isEmpty());
        assertTrue(graph.sources("a").isEmpty() &&
                graph.targets("a").isEmpty());
        assertTrue(graph.sources("b").isEmpty() &&
                graph.targets("b").isEmpty());
        
    }
    
    @Test
    public void testRemoveNoExistingEdgeBetweenExisitingVertices(){
        Graph<String> graph = emptyInstance();
        
        graph.add("a");
        graph.add("b");
        
        assertEquals(graph.set("a", "b", 0), 0);
        assertEquals(graph.vertices().size(), 2);
        assertTrue(graph.vertices().contains("a") &&
                graph.vertices().contains("b"));
        assertTrue(graph.sources("a").isEmpty() &&
                graph.targets("a").isEmpty());
        assertTrue(graph.sources("b").isEmpty() &&
                graph.targets("b").isEmpty());
        
    }
    
    @Test
    public void testRemoveEdgeDistinctVertices(){
        Graph<String> graph = emptyInstance();
        
        graph.set("a", "b", 1);
        
        assertEquals(graph.set("a", "b",0), 1);
        assertEquals(graph.vertices().size(), 2);
        assertTrue(graph.vertices().contains("a") &&
                graph.vertices().contains("b"));
        assertTrue(graph.sources("a").isEmpty() &&
                graph.targets("a").isEmpty());
        assertTrue(graph.sources("b").isEmpty() &&
                graph.targets("b").isEmpty());
    }
    
    @Test
    public void testRemoveEdgeOnlyTailVertexExists(){
        Graph<String> graph = emptyInstance();
        
        graph.add("a");
        
        assertEquals(graph.set("a", "b",0), 0);
        assertEquals(graph.vertices().size(), 1);
        assertTrue(graph.vertices().contains("a"));
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("a").isEmpty());
    }
    
    @Test
    public void testRemoveEdgeOnlyHeadVertexExists(){
        Graph<String> graph = emptyInstance();
        
        graph.add("a");
        
        assertEquals(graph.set("b", "a",0), 0);
        assertEquals(graph.vertices().size(), 1);
        assertTrue(graph.vertices().contains("a"));
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("a").isEmpty());
    }
    
    @Test
    public void testAddChangeRemoveReflexiveEdge(){
        Graph<String> graph = emptyInstance();
        
        //add edge a-->a with weight 1
        assertEquals(graph.set("a", "a", 1),0);
        assertEquals(graph.vertices().size(),1);
        assertTrue(graph.vertices().contains("a"));
        assertEquals(graph.sources("a").size(),1);
        assertEquals(graph.targets("a").size(),1);
        assertEquals(graph.sources("a").get("a"), new Integer(1));
        assertEquals(graph.targets("a").get("a"), new Integer(1));
        
        //change edge a-->a to weight 2
        assertEquals(graph.set("a", "a", 2),1);
        assertEquals(graph.vertices().size(),1);
        assertTrue(graph.vertices().contains("a"));
        assertEquals(graph.sources("a").size(),1);
        assertEquals(graph.targets("a").size(),1);
        assertEquals(graph.sources("a").get("a"), new Integer(2));
        assertEquals(graph.targets("a").get("a"), new Integer(2));
        
        //remove edge a-->a
        assertEquals(graph.set("a", "a", 0),2);
        assertEquals(graph.vertices().size(),1);
        assertTrue(graph.vertices().contains("a"));
        assertEquals(graph.sources("a").size(),0);
        assertEquals(graph.targets("a").size(),0);    
    }
    
    @Test
    public void testRemoveVertexWithMultipleInAndOutEdges(){
        Graph<String> graph = emptyInstance();
        
        graph.set("a", "b", 1);
        graph.set("b", "a", 1);
        graph.set("a", "c", 2);
        graph.set("c", "a", 2);
        
        assertEquals(graph.sources("a").size(),2);
        assertEquals(graph.sources("a").get("b"), new Integer(1));
        assertEquals(graph.sources("a").get("c"), new Integer(2));
        assertEquals(graph.targets("a").size(),2);
        assertEquals(graph.targets("a").get("b"), new Integer(1));
        assertEquals(graph.targets("a").get("c"), new Integer(2));
        
        
        assertTrue(graph.remove("a"));
        assertEquals(graph.vertices().size(), 2);
        assertTrue(graph.vertices().contains("b") &&
                graph.vertices().contains("c"));
        assertTrue(graph.sources("b").isEmpty() &&
                graph.sources("c").isEmpty());
        assertTrue(graph.targets("b").isEmpty() &&
                graph.targets("c").isEmpty());  
    }
    
    @Test
    public void testRemoveVertexWithSingleInAndOutEdges(){
        Graph<String> graph = emptyInstance();
        
        graph.set("a", "b", 1);
        graph.set("c", "a", 2);
        
        assertTrue(graph.remove("a"));
        assertEquals(graph.vertices().size(), 2);
        assertTrue(graph.vertices().contains("b") &&
                graph.vertices().contains("c"));
        assertTrue(graph.sources("b").isEmpty());
        assertTrue(graph.targets("c").isEmpty());
    }
    
    @Test
    public void testRemoveIsolatedVertex(){
        Graph<String> graph = emptyInstance();
        
        graph.add("a");
        graph.set("b", "c", 1);
        
        assertTrue(graph.remove("a"));
        assertEquals(graph.vertices().size(), 2);
        assertTrue(graph.vertices().contains("b") &&
                graph.vertices().contains("c"));
        assertTrue(graph.sources("b").isEmpty());
        assertTrue(graph.targets("c").isEmpty());
        assertEquals(graph.targets("b").size(), 1);
        assertEquals(graph.targets("b").get("c"), new Integer(1));
        assertEquals(graph.sources("c").size(), 1);
        assertEquals(graph.sources("c").get("b"), new Integer(1));
    }
    
    @Test
    public void testNonMutatingObservers(){
        Graph<String> graph = emptyInstance();
        
        graph.set("a", "b", 1);
        graph.vertices();
        
        //test vertices() does not mutate state
        assertTrue(graph.vertices().contains("a") &&
                graph.vertices().contains("b"));
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("b").isEmpty());
        assertEquals(graph.targets("a").size(),1);
        assertEquals(graph.targets("a").get("b"),new Integer(1));
        assertEquals(graph.sources("b").size(),1);
        assertEquals(graph.sources("b").get("a"),new Integer(1)); 
        
        //test sources() does not mutate state
        graph.sources("b");
        
        assertTrue(graph.vertices().contains("a") &&
                graph.vertices().contains("b"));
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("b").isEmpty());
        assertEquals(graph.targets("a").size(),1);
        assertEquals(graph.targets("a").get("b"),new Integer(1));
        assertEquals(graph.sources("b").size(),1);
        assertEquals(graph.sources("b").get("a"),new Integer(1));
        
        //test targets() does not mutate state
        graph.targets("a");
        
        assertTrue(graph.vertices().contains("a") &&
                graph.vertices().contains("b"));
        assertTrue(graph.sources("a").isEmpty());
        assertTrue(graph.targets("b").isEmpty());
        assertEquals(graph.targets("a").size(),1);
        assertEquals(graph.targets("a").get("b"),new Integer(1));
        assertEquals(graph.sources("b").size(),1);
        assertEquals(graph.sources("b").get("a"),new Integer(1));
    } 
}
