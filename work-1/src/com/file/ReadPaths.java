package com.file;

import com.sections.Section;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class ReadPaths
{
    private HashSet<LinkedHashMap<Integer, Section>> paths;
    private HashSet<Section> sections;
    private Section root;

    /**
     * Constructor, reading a file containing paths et parsing data from it
     * @param sectionsFromFile sections where to find information for the numbers read
     */
    public ReadPaths(HashSet<Section> sectionsFromFile)
    {
        sections = sectionsFromFile;
        root = findSection(0);
        paths = new HashSet<>();
        BufferedReader inputFileBuffer;
        String line;
        try {
            inputFileBuffer = new BufferedReader(new FileReader("AllPath.txt"));
            line = inputFileBuffer.readLine();
            while(!line.isEmpty())
            {
                LinkedHashMap<Integer, Section> pathFromFile = readPath(line);
                paths.add(pathFromFile);
                line = inputFileBuffer.readLine();
                if(line == null)
                    break;
            }
        } catch (IOException error) {
            error.printStackTrace();
            System.out.println("Error Path's file could not be opened.");
        }
    }

    /**
     * Getter for all the paths read
     * @return container of all the read path
     */
    public HashSet<LinkedHashMap<Integer, Section>> getAllPath()
    {
        return new HashSet<>(paths);
    }

    /**
     * Function to parse a line following this format "[A,B,C,D]" with A,B,C and D numbers of the section in the path
     * @param line string to parse from
     * @return container representing the path read from the line
     */
    private LinkedHashMap<Integer, Section> readPath(String line)
    {
        int i = 0;
        line = line.substring(1);
        int index = line.indexOf(',');
        LinkedHashMap<Integer, Section> path = new LinkedHashMap<>();
        Integer valueAdded = -1;
        while(index != -1)
        {
            String value = line.substring(0,index);
            valueAdded = Integer.parseInt(value);
            path.put(i++, findSection(valueAdded));
            if(index + 2 < line.length())
                line = line.substring(index + 2);
            else
                break;
            index = line.indexOf(',');
            if(index == -1)
                index = line.indexOf(']');
        }
        if(!valueAdded.equals(0))
            path.put(i, root);
        return path;
    }

    /**
     * Finds the a section in the section HashSet
     * @param sectionNumber section number to be found
     * @return The section requested
     */
    private Section findSection(int sectionNumber)
    {
        for(Section element: sections)
            if(element.getSectionNumber_().equals(sectionNumber))
                return element;
        return null;
    }
}
