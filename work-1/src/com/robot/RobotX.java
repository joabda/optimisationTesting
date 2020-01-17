package com.robot;

public class RobotX extends Robot
{
    /**
     * Constructor of a robot type X, sets the maximum weight and the initial value of K
     */
    public RobotX()
    {
        maxWeight = 5;
        k = 1;
    }

    /**
     * Getter for the max weight that X can carry
     * @return max weight that can be carried (5kg here)
     */
    static public int getMaxWeight() { return 5; }

    /**
     * Function to update the speed of robot A with the current load's weight
     */
    @Override
    protected void updateK() { k = 1 + currentWeight; }

    /**
     * Getter function of the robot's name
     */
    @Override
    public String getName() { return "Robot X"; }
}