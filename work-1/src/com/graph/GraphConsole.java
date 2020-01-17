package com.graph;

import com.sections.Section;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class GraphConsole {

    private HashSet<Section> sectionsInFile;

    /**
     * Constructor of a graphic.
     * @param sectionsFromFile all the sections read from the file with their information
     */
    public GraphConsole(HashSet<Section> sectionsFromFile)
    {
        sectionsInFile = sectionsFromFile;
    }

    /**
     * Function to display the graph in console
     */
    public void display()
    {
        for(Section current : sectionsInFile)
            displaySection(current);
    }

    /**
     * Function to display a section in the console
     */
    private static void displaySection(Section current)
    {
        System.out.print("(Noeud " + current.getSectionNumber_() + ", ");
        System.out.println(current.getNumberOfObjectsA() + ", " + current.getNumberOfObjectsB() + ", " + current.getNumberOfObjectsC());
        System.out.println("Voisins:");
        displayNeighbors(current.getDistances());
        System.out.println();
    }

    /**
     * Function to display a section's neighbors in the console
     */
    private static void displayNeighbors(HashMap<Integer, Integer> neighbors)
    {
        HashMap<Integer, Integer> neighborsCopy = neighbors;
        Iterator it = neighborsCopy.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println("     Noeud " + pair.getKey() + ", Distance " + pair.getValue());
            it.remove(); // To Avoid a current modification exception
        }
    }

}
