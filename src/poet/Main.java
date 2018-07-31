/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Example program using GraphPoet.
 * 
 * <p>PS2 instructions: you are free to change this example class.
 */
public class Main {
    
    /**
     * Generate example poetry.
     * 
     * @param args unused
     * @throws IOException if a poet corpus file cannot be found or read
     */
    public static void main(String[] args) throws IOException {
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("please enter pathname for corpus text file:");
        final String corpusFileName = cin.readLine();
        final GraphPoet nimoy = new GraphPoet(new File(corpusFileName));
        
        System.out.println("please enter pathname for input text file:");
        final String inputFileName = cin.readLine();
        List<String> inputs = Files.readAllLines(new File(inputFileName).toPath());
        
        List<String> outputs = new ArrayList<>();
        for(String input : inputs){
            outputs.add(nimoy.poem(input));
        }
        System.out.println("\n" + String.join("\n", inputs) + "\n>>>\n" + String.join(" ", outputs));
    }
    
}
