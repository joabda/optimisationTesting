package com.robot;
import com.menu.Order;
import com.sections.Section;
import com.transportObject.TransportObject;
import com.transportObject.TransportObjectA;
import com.transportObject.TransportObjectB;
import com.transportObject.TransportObjectC;
import java.util.LinkedHashMap;
import java.util.Vector;

public abstract class Robot {

    int maxWeight = 0;
    int currentWeight = 0;
    double k;
    private Vector<TransportObject> baggage = new Vector<>();

    /**
     * Function to evaluate the time taken by a Robot to go through a path and pick the requested items
     * @param pickUps container indicating for sections 1 to bestPath.size what object to pick in each section
     * @param bestPath path the robot should take, containing the ordered sections to visit
     * @param order user's order (numbers of A, B and C)
     * @return the time take by a path (if the robot can complete it ) else infinity
     */
    public int findPathTime(LinkedHashMap<Integer, Order> pickUps, LinkedHashMap<Integer, Section> bestPath, Order order)
    {
        int time = 0;
        Order currentOrder;
        for(int i = 0; i < bestPath.size(); i++)
        {
            if( i != bestPath.size() - 1)
                time += findETA(bestPath.get(i).getDistance(bestPath.get(i + 1).getSectionNumber_()));
            currentOrder = pickUps.get(i);
            try {
                for (int j = 0; j < currentOrder.getNumberOfA(); j++)
                    addBaggage(new TransportObjectA());
                for (int j = 0; j < currentOrder.getNumberOfB(); j++)
                    addBaggage(new TransportObjectB());
                for (int j = 0; j < currentOrder.getNumberOfC(); j++)
                    addBaggage(new TransportObjectC());
            } catch (UnsupportedOperationException error) { return (int)Double.POSITIVE_INFINITY; }
        }
        if(baggage.size() < (order.getNumberOfA() + order.getNumberOfB() + order.getNumberOfC()))
            time = (int)Double.POSITIVE_INFINITY;
        return time;
    }

    /**
     * Find the estimated time the robot will take to travel a certain distance
     * @param distance distance for which the time will be evaluated
     * @return The time that will be taken
     */
    private double findETA(double distance) { return distance * k; }

    /**
     * Adds an object to a robot's transportation list
     * @param toAdd object to be added to the list
     */
    private void addBaggage(TransportObject toAdd)
    {
        if(toAdd != null)
        {
            if (canAddObject(toAdd.getWeight()))
            {
                baggage.add(toAdd);
                currentWeight += toAdd.getWeight();
                updateK();
            }
            else
                throw new UnsupportedOperationException();
        }
    }

    /**
     * Checks if an object can be added to a robot
     * @param weightToAdd weight of the object to be added
     * @return if the object can be added without exceeding the limit
     */
    private boolean canAddObject(int weightToAdd) { return (currentWeight + weightToAdd) <= maxWeight; }

    /**
     * Abstract function (should be defined for each child class), to calculate the speed depending
     *      on the current load's weight
     */
    protected abstract void updateK();

    /**
     * Abstract function (should be defined for each child class), to get the robot's name
     */
    public abstract String getName();

}