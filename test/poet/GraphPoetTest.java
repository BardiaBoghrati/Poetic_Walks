/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Testing strategy
    // TODO
           
    
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    

    @Test(expected=IOException.class)
    public void testGraphPoetFileDoesnotExist() throws IOException{
        final File textFile = new File("test/poet/doesNotExists.txt");
        new GraphPoet(textFile);
    };
    
    @Test
    public void testGraphPoetNoTextInCorpus() throws IOException{
        //Corpus: ""
        //Input: "a b"
        final File textFile = new File("test/poet/empty.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("({}, {})", poet.toString());
        assertEquals("a b", poet.poem("a b"));
    }
    
    @Test
    public void testTwoWordInputFirstMissingFromAffinityGraph() throws IOException{
        //Corpus: "\na a"
        //Input: "b a"
        final File textFile = new File("test/poet/TwoWordInputFirstMissingFromAffinityGraph.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("({a}, {(a, a, 1)})", poet.toString());
        assertEquals("b a", poet.poem("b a"));

    }
    
    @Test
    public void testTwoWordInputSecondMissingFromAffinityGraph() throws IOException{
        //Corpus: "a\n\na"
        //Input: "a b"
        final File textFile = new File("test/poet/TwoWordInputSecondMissingFromAffinityGraph.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("({a}, {(a, a, 1)})", poet.toString());
        assertEquals("a b", poet.poem("a b"));
    }
    
    @Test
    public void testTwoWordInputBothMissingFromAffinityGraph() throws IOException{
        //Corpus: "  "
        //Input: "a b"
        final File textFile = new File("test/poet/TwoWordInputBothMissingFromAffinityGraph.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("({}, {})", poet.toString());
        assertEquals("a b", poet.poem("a b"));
    };
    
    @Test
    public void testTwoWordInputBothInAffinityGraphButNoPath() throws IOException{
        //Corpus: "b , a\n"
        //Input: "a b"
        final File textFile = new File("test/poet/TwoWordInputBothInAffinityGraphButNoPath.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("a b", poet.poem("a b"));
        
    }
    
    @Test
    public void testTwoWordInputOnlyPathOflengthOneBetweenThem() throws IOException{
        //Corpus: " a b\n"
        //Input: "a b  "
        final File textFile = new File("test/poet/TwoWordInputOnlyPathOflengthOneBetweenThem.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertTrue("({a, b}, {(a, b, 1)})".equals(poet.toString()) ||
                "({b, a}, {(a, b, 1)})".equals(poet.toString()));
        assertEquals("a b", poet.poem("a b  "));
    }
    
    @Test
    public void testTwoWordInputOnlyPathOfLengthThreeBetweenThem() throws IOException{
        //Corpus: "a x y b "
        //Input: "a b"
        final File textFile = new File("test/poet/TwoWordInputOnlyPathOfLengthThreeBetweenThem.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("a b", poet.poem("a b"));
    }
    
    @Test
    public void testTwoWordInputOnlyOneLengthTwoPath() throws IOException{
        //Corpus: "a  D b"
        //Input: "a  b"
        final File textFile = new File("test/poet/TwoWordInputOnlyOneLengthTwoPath.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("a d b", poet.poem("a  b"));
    }
    
    @Test
    public void testTwoWordInputTiesForMaximunWeightPath() throws IOException{
        //Corpus: "a a b  b"
        //Input: " a b"
        final File textFile = new File("test/poet/TwoWordInputTiesForMaximunWeightPath.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertTrue("({a, b}, {(a, a, 1), (a, b, 1), (b, b, 1)})".equals(poet.toString()) ||
                "({a, b}, {(a, a, 1), (b, b, 1), (a, b, 1)})".equals(poet.toString()) ||
                "({a, b}, {(a, b, 1), (a, a, 1), (b, b, 1)})".equals(poet.toString()) ||
                "({a, b}, {(a, b, 1), (b, b, 1), (a, a, 1)})".equals(poet.toString()) ||
                "({a, b}, {(b, b, 1), (a, b, 1), (a, a, 1)})".equals(poet.toString()) ||
                "({a, b}, {(b, b, 1), (a, a, 1), (a, b, 1)})".equals(poet.toString()) ||
                "({b, a}, {(a, a, 1), (a, b, 1), (b, b, 1)})".equals(poet.toString()) ||
                "({b, a}, {(a, a, 1), (b, b, 1), (a, b, 1)})".equals(poet.toString()) ||
                "({b, a}, {(a, b, 1), (a, a, 1), (b, b, 1)})".equals(poet.toString()) ||
                "({b, a}, {(a, b, 1), (b, b, 1), (a, a, 1)})".equals(poet.toString()) ||
                "({b, a}, {(b, b, 1), (a, b, 1), (a, a, 1)})".equals(poet.toString()) ||
                "({b, a}, {(b, b, 1), (a, a, 1), (a, b, 1)})".equals(poet.toString()));
        assertTrue("a a b".equals(poet.poem(" a b")) ||
                "a b b".equals(poet.poem(" a b")));
    }
    
    @Test
    public void testTwoWordInputNoTiesBridgeWordEqualsFirstWord() throws IOException{
        //Corpus: "a A  a B b"
        //Input: "\na b"
        final File textFile = new File("test/poet/TwoWordInputNoTiesBridgeWordEqualsFirstWord.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertTrue("({a, b}, {(a, a, 2), (a, b, 1), (b, b, 1)})".equals(poet.toString()) ||
                "({a, b}, {(a, a, 2), (b, b, 1), (a, b, 1)})".equals(poet.toString()) ||
                "({a, b}, {(a, b, 1), (a, a, 2), (b, b, 1)})".equals(poet.toString()) ||
                "({a, b}, {(a, b, 1), (b, b, 1), (a, a, 2)})".equals(poet.toString()) ||
                "({a, b}, {(b, b, 1), (a, b, 1), (a, a, 2)})".equals(poet.toString()) ||
                "({a, b}, {(b, b, 1), (a, a, 2), (a, b, 1)})".equals(poet.toString()) ||
                "({b, a}, {(a, a, 2), (a, b, 1), (b, b, 1)})".equals(poet.toString()) ||
                "({b, a}, {(a, a, 2), (b, b, 1), (a, b, 1)})".equals(poet.toString()) ||
                "({b, a}, {(a, b, 1), (a, a, 2), (b, b, 1)})".equals(poet.toString()) ||
                "({b, a}, {(a, b, 1), (b, b, 1), (a, a, 2)})".equals(poet.toString()) ||
                "({b, a}, {(b, b, 1), (a, b, 1), (a, a, 2)})".equals(poet.toString()) ||
                "({b, a}, {(b, b, 1), (a, a, 2), (a, b, 1)})".equals(poet.toString()));
        assertEquals("a a b", poet.poem("\na b"));
    }
    
    @Test
    public void testTwoWordInputNoTiesBridgeWordEqualsSecondWord() throws IOException{
        //Corpus: "a a b b b"
        //Input: "a\n\nB"
        final File textFile = new File("test/poet/TwoWordInputNoTiesBridgeWordEqualsSecondWord.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertTrue("({a, b}, {(a, a, 1), (a, b, 1), (b, b, 2)})".equals(poet.toString()) ||
                "({a, b}, {(a, a, 1), (b, b, 2), (a, b, 1)})".equals(poet.toString()) ||
                "({a, b}, {(a, b, 1), (a, a, 1), (b, b, 2)})".equals(poet.toString()) ||
                "({a, b}, {(a, b, 1), (b, b, 2), (a, a, 1)})".equals(poet.toString()) ||
                "({a, b}, {(b, b, 2), (a, b, 1), (a, a, 1)})".equals(poet.toString()) ||
                "({a, b}, {(b, b, 2), (a, a, 1), (a, b, 1)})".equals(poet.toString()) ||
                "({b, a}, {(a, a, 1), (a, b, 1), (b, b, 2)})".equals(poet.toString()) ||
                "({b, a}, {(a, a, 1), (b, b, 2), (a, b, 1)})".equals(poet.toString()) ||
                "({b, a}, {(a, b, 1), (a, a, 1), (b, b, 2)})".equals(poet.toString()) ||
                "({b, a}, {(a, b, 1), (b, b, 2), (a, a, 1)})".equals(poet.toString()) ||
                "({b, a}, {(b, b, 2), (a, b, 1), (a, a, 1)})".equals(poet.toString()) ||
                "({b, a}, {(b, b, 2), (a, a, 1), (a, b, 1)})".equals(poet.toString()));
        assertEquals("a b B", poet.poem("a\n\nB"));
        
        
    }
    
    @Test
    public void testRepeatedWordInputOnlyOneLengthTwoPath() throws IOException{
        //Corpus: "a, , A, ,"
        //Input: "a, a,\n"
        final File textFile = new File("test/poet/RepeatedWordInputOnlyOneLengthTwoPath.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertTrue("({a,, ,}, {(a,, ,, 2), (,, a,, 1)})".equals(poet.toString()) ||
                "({a,, ,}, {(,, a,, 1), (a,, ,, 2)})".equals(poet.toString()) ||
                "({,, a,}, {(a,, ,, 2), (,, a,, 1)})".equals(poet.toString()) ||
                "({,, a,}, {(,, a,, 1), (a,, ,, 2)})".equals(poet.toString()));
        assertEquals("a, , a,", poet.poem("a, a,\n"));          
    }
    
    @Test
    public void testRepeatedWordInputWithNoTiesForMaximumWeightPath() throws IOException{
        //Corpus: "a a\n a b\na"
        //Input: "A A"
        final File textFile = new File("test/poet/RepeatedWordInputWithNoTiesForMaximumWeightPath.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("A a A", poet.poem("A A"));
        
    }
    
    @Test
    public void testRepeatedWordInputWithTiesForMaximumPath() throws IOException{
        //Corpus: ",a ,a b ,a"
        //Input: ",A ,a"
        final File textFile = new File("test/poet/RepeatedWordInputWithTiesForMaximumPath.txt");
        GraphPoet poet = new GraphPoet(textFile);

        assertTrue(",A ,a ,a".equals(poet.poem(",A ,a")) ||
                ",A b ,a".equals(poet.poem(",A ,a")));
        
    }
    
    @Test
    public void testReaptedWordInputSingleWordAffinityGraphNoEdges() throws IOException{
        //Corpus: "a"
        //Input: "a a"
        final File textFile = new File("test/poet/ReaptedWordInputSingleWordAffinityGraphNoEdges.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("({a}, {})", poet.toString());
        assertEquals("a a", poet.poem("a a"));
    }
    
    @Test
    public void testSingleWordInput() throws IOException{
        //Corpus: "a a"
        //Input: "a"
        final File textFile = new File("test/poet/SingleWordInput.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("a", poet.poem("a"));
    }
    
    @Test
    public void testEmptyInput() throws IOException{
        //Corpus: "a a"
        //Input: ""
        final File textFile = new File("test/poet/EmptyInput.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("", poet.poem(""));
    }
    
    @Test
    public void testWhiteSpaceInput() throws IOException{
        //Corpus: "a"
        //Input: "  "
        final File textFile = new File("test/poet/WhiteSpaceInput.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("", poet.poem("  "));
    }
    
    @Test
    public void testThreeWordInputBothConsecutivePairsHaveBridgeWord() throws IOException{
        //Corpus: "a b a"
        //Input: "a A a"
        final File textFile = new File("test/poet/ThreeWordInputBothConsecutivePairsHaveBridgeWord.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("a b A b a", poet.poem("a A a"));
    }
    
    @Test
    public void testThreeWordInputNoBridgeWords() throws IOException{
        //Corpus: "a a"
        //Input: "a  b a"
        final File textFile = new File("test/poet/ThreeWordInputNoBridgeWords.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("a b a", poet.poem("a  b a"));
    }
    
    @Test
    public void testThreeWordInputFirstConsecutivePairHasBridgeWordSecondDoesnot() throws IOException{
        //Corpus: "a a b"
        //Input: "a b  a"
        final File textFile = new File("test/poet/ThreeWordInputFirstConsecutivePairHasBridgeWordSecondDoesnot.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("a a b a", poet.poem("a b  a"));
    }
    
    @Test
    public void testThreeWordInputSecondConsecutivePairHasBridgeWordFirstDoesnot() throws IOException{
        //Corpus: "b b a"
        //Input: "a  b  a"
        final File textFile = new File("test/poet/ThreeWordInputSecondConsecutivePairHasBridgeWordFirstDoesnot.txt");
        GraphPoet poet = new GraphPoet(textFile);
        
        assertEquals("a b b a", poet.poem("a  b  a"));
        
    }
}
