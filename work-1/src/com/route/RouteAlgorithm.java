package com.route;

import com.file.ReadPaths;
import com.menu.Order;
import com.robot.Robot;
import com.robot.RobotX;
import com.robot.RobotY;
import com.robot.RobotZ;
import com.sections.Section;
import com.transportObject.TransportObjectA;
import com.transportObject.TransportObjectB;
import com.transportObject.TransportObjectC;

import java.io.PrintStream;
import java.util.*;

public class RouteAlgorithm
{
    private HashSet<Section> sections;
    private int numberA;
    private int numberB;
    private int numberC;

    /**
     * Constructor assigns attributes to the parameter's values
     * @param sectionsFromFile contains the sections read from the file
     * @param numberOfA number of objects A contained in the order
     * @param numberOfB number of objects B contained in the order
     * @param numberOfC number of objects C contained in the order
     */
    public RouteAlgorithm(HashSet<Section> sectionsFromFile, int numberOfA, int numberOfB, int numberOfC)
    {
        sections = sectionsFromFile;
        numberA = numberOfA;
        numberB = numberOfB;
        numberC = numberOfC;
    }

    /**
     * Finds the best route by calling other functions
     */
    public void displayBestRoute()
    {
        System.out.println();
        try {
            LinkedHashMap<Integer, Section> bestPath = getBestRoute();
            LinkedHashMap<Integer, Order> pickUps = findHowToPickObjects(bestPath);
            System.out.println(printPathAndPickups(bestPath, pickUps));
            System.out.print("Robot utilisé: ");
            int time = findRobotType(pickUps, bestPath);
            System.out.println("Temps: " + time + "s");
        } catch (NoSuchElementException error) { System.out.println("Sorry an error occurred try again!"); }
    }

    /**
     * Finds how to pick the object to minimise the path's time of completion
     * @param path path to optimize
     * @return contains for each section (1 to path.size - 1) a list of items to be picked
     */
    private LinkedHashMap<Integer, Order> findHowToPickObjects(LinkedHashMap<Integer, Section> path)
    {
        int pathNumberOfA = 0, pathNumberOfB = 0, pathNumberOfC = 0, howMuch;
        Section currentSection;
        Order currentOrder;
        LinkedHashMap<Integer, Order> fromEachSection = new LinkedHashMap<>();
        for(int pick = path.size() - 1; pick >= 0; pick--)
        {
            currentSection = path.get(pick);
            currentOrder = new Order();
            if(pathNumberOfA < numberA && !currentSection.hasBeenVisited())
            {
                howMuch = findHowMuchFrom(currentSection.getNumberOfObjectsA(), pathNumberOfA, numberA);
                pathNumberOfA += howMuch;
                currentOrder.takeOrder("Object A", howMuch);
            }
            if(pathNumberOfB < numberB && !currentSection.hasBeenVisited())
            {
                howMuch = findHowMuchFrom(currentSection.getNumberOfObjectsB(), pathNumberOfB, numberB);
                pathNumberOfB += howMuch;
                currentOrder.takeOrder("Object B", howMuch);
            }
            if(pathNumberOfC < numberC && !currentSection.hasBeenVisited())
            {
                howMuch = findHowMuchFrom(currentSection.getNumberOfObjectsC(), pathNumberOfC, numberC);
                pathNumberOfC += howMuch;
                currentOrder.takeOrder("Object C", howMuch);
            }
            fromEachSection.put(pick, currentOrder);
            currentSection.visit();
        }
        return fromEachSection;
    }

    /**
     * Finds how much items should be picked from a section
     * @param contains number of items contained in the section
     * @param current number of items currently in the robot's carry
     * @param limit number of items that the order contains
     * @return number of items to be picked
     */
    private int findHowMuchFrom(int contains, int current, int limit)
    {
        if(contains + current <= limit)
            return contains;
        else
            return limit - current;
    }

    /**
     * Finds the best path
     * @return best path
     * @exception NoSuchElementException thrown if no path fits the requirements
     */
    private LinkedHashMap<Integer, Section> getBestRoute()
    {
        HashSet<LinkedHashMap<Integer, Section>> allPaths = findAllPath();
        HashSet<LinkedHashMap<Integer, Section>> modifiedPath = new HashSet<>();
        for(LinkedHashMap<Integer, Section> aPath : allPaths)
            removePathWithNotEnoughItems(aPath, modifiedPath); // Not working
        LinkedHashMap<Integer, Section> bestPath = fromModifiedPathFindShortest(modifiedPath);
        if(bestPath.isEmpty())
        {
            System.out.println("There's no existing path!");
            throw new NoSuchElementException();
        }
        return bestPath;
    }

    /**
     * Function, to print a path
     * @param bestPath path to be printed
     * @return string path built
     */
    private String printPathAndPickups(LinkedHashMap<Integer, Section> bestPath, LinkedHashMap<Integer, Order> pickUps)
    {
        String separator = "\n    ==> \n";
        StringBuilder path = new StringBuilder();
        for(int i = 0; i < bestPath.size() - 1; i++)
            path.append("Section ").append(bestPath.get(i).getSectionNumber_()).append(printPickups(pickUps.get(i))).append(separator);
        path.append("Section ").append(bestPath.get(bestPath.size()-1).getSectionNumber_()).append(printPickups(pickUps.get(bestPath.size()-1)));
        path.append("\n");
        return path.toString();
    }

    private String printPickups(Order currentOrder)
    {
        StringBuilder pickupsString = new StringBuilder();
        if(currentOrder.getNumberOfA() != 0 || currentOrder.getNumberOfB() != 0 || currentOrder.getNumberOfC() !=0)
        {
            if(currentOrder.getNumberOfA() != 0)
                pickupsString.append(" Object A: ").append(currentOrder.getNumberOfA()).append(" ");
            if(currentOrder.getNumberOfB() != 0)
                pickupsString.append(" Object B: ").append(currentOrder.getNumberOfB()).append(" ");
            if(currentOrder.getNumberOfC() != 0)
                pickupsString.append(" Object C: ").append(currentOrder.getNumberOfC()).append(" ");
        }
        return pickupsString.toString();
    }

    /**
     * Finds the shortest path in a set of path
     * @param modifiedPath set of path containing all the path
     * @return The shortest path in the set
     */
    private LinkedHashMap<Integer, Section> fromModifiedPathFindShortest(HashSet<LinkedHashMap<Integer, Section>> modifiedPath)
    {
        int minLength = (int)Double.POSITIVE_INFINITY, currentPathLength;
        LinkedHashMap<Integer, Section> minPath = new LinkedHashMap<>();
        for(LinkedHashMap<Integer, Section> aPath : modifiedPath)
        {
            currentPathLength = getLength(aPath, minLength);
            if (minLength > currentPathLength)
            {
                minPath = aPath;
                minLength = currentPathLength;
            }
        }
        return minPath;
    }

    /**
     * Function to find the length of a path
     * @param aPath path to be evaluated
     * @param minLength if aPath's length becomes bigger than the minimum then we break
     * @return length of the path, (or part of it, if we break before completion)
     */
    private int getLength(LinkedHashMap<Integer, Section> aPath, int minLength)
    {
        int length = 0;
        for(int i = 0; i < aPath.size() - 1; i++)
        {
            length += aPath.get(i).getDistance(aPath.get(i + 1).getSectionNumber_());
            if(length > minLength)
                break;
        }
        return length;
    }

    /**
     * Function to evaluate a path and stores it in a container, at least the number of requested objects from
     *      each type
     * @param aPath path to be evaluated
     * @param modifiedPaths contains all the path that fit the requirements
     */
    private void removePathWithNotEnoughItems(LinkedHashMap<Integer, Section> aPath,
                                              HashSet<LinkedHashMap<Integer, Section>> modifiedPaths)
    {
        int pathNumberOfA = 0, pathNumberOfB = 0, pathNumberOfC = 0;
        Section currentSection;
        for(int i = 0; i < aPath.size(); i++)
        {
            currentSection = aPath.get(i);
            if(currentSection.hasBeenVisited())
            {
                pathNumberOfA += currentSection.getNumberOfObjectsA();
                pathNumberOfB += currentSection.getNumberOfObjectsB();
                pathNumberOfC += currentSection.getNumberOfObjectsC();
            }
            currentSection.visit();
        }
        resetVisited();
        if(pathNumberOfA >= numberA && pathNumberOfB >= numberB && pathNumberOfC >= numberC)
            modifiedPaths.add(aPath);
    }

    /**
     * Reset all the sections to not visited
     */
    private void resetVisited()
    {
        for(Section element : sections)
            element.unVisit();
    }

    /**
     * Finds the root in the section HashSet
     * @return root section
     */
    private Section findRoot()
    {
        for(Section element: sections)
            if(element.getSectionNumber_().equals(0))
                return element;
        return null;
    }

    /**
     * Function to find all possible path from 0 back to itself
     * @return container containing all the possible paths
     */
    private HashSet<LinkedHashMap<Integer, Section>> findAllPath()
    {
        PrintStream console = System.out; // Store current stream in console
        AllPath path = new AllPath(sections);
        Section root = findRoot();
        assert root != null;
        HashMap<Integer, Integer> distances = root.getDistances();
        if(distances != null)
        {
            Iterator it = distances.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                it.remove(); // avoids a ConcurrentModificationException
                path.printAllPaths((Integer) pair.getKey());
            }
        }
        System.setOut(console); // Restore current stream
        ReadPaths pathReader = new ReadPaths(sections);
        return pathReader.getAllPath();
    }

    /**
     * Finds what robot is best to complete a path in the best time
     * @param pickUps contains for every section what objects to pick (sorted by order of visit)
     * @param bestPath path that the robot have to follow
     * @return time taken by the robot (if the task has been completed) or infinity
     */
    private int findRobotType(LinkedHashMap<Integer, Order> pickUps, LinkedHashMap<Integer, Section> bestPath)
    {
        int totalWeight = numberA * TransportObjectA.weight + numberB * TransportObjectB.weight
                + numberC * TransportObjectC.weight;
        if(totalWeight > RobotX.getMaxWeight() && totalWeight > RobotY.getMaxWeight())
        {
            if(totalWeight > RobotZ.getMaxWeight())
                System.out.println("No robot can carry that much weight: " + totalWeight + "kg.");
            else
                System.out.println("Only Robot C can carry " + totalWeight + "kg.");
        }
        List<Robot> robots = new ArrayList<>();
        robots.add(new RobotX());
        robots.add(new RobotY());
        robots.add(new RobotZ());
        Robot bestRobot = robots.get(0);
        int bestTime = (int)Double.POSITIVE_INFINITY, currentTime;
        Order order = new Order();
        order.takeOrder("Object A", numberA);
        order.takeOrder("Object B", numberB);
        order.takeOrder("Object C", numberC);
        for(Robot aRobot : robots)
        {
            currentTime = aRobot.findPathTime(pickUps, bestPath, order);
            if(currentTime < bestTime)
            {
                bestTime = currentTime;
                bestRobot = aRobot;
            }
        }
        System.out.println(bestRobot.getName());
        return bestTime;
    }
}
