/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
        System.out.println("loading corpus file...");
        final GraphPoet nimoy = new GraphPoet(new File(corpusFileName));
        
        outer:
            while(true){
                System.out.println("please enter input text:");
                final String input = cin.readLine();
                System.out.println("\n" + input + "\n>>>\n" + nimoy.poem(input));
                System.out.println("\nwould you like to continue?(y|n)");
                while(true){
                    String answer = cin.readLine();
                    
                    if(answer.equals("y")) break;
                    if(answer.equals("n")) break outer;
                    
                    System.err.println(answer + " is invalid answer.");
                    System.out.println("Please answer y for \"Yes\" or n for \"No\":");
                }
            }
    }
}
