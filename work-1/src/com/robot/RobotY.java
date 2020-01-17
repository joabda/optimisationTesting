package com.robot;

public class RobotY extends Robot
{
    /**
     * Constructor of a robot type Y, sets the maximum weight and the initial value of K
     */
    public RobotY()
    {
        maxWeight = 10;
        k = 1.5;
    }

    /**
     * Getter for the max weight that Y can carry
     * @return max weight that can be carried (10kg here)
     */
    static public int getMaxWeight() { return 10; }

    /**
     * Function to update the speed of robot A with the current load's weight
     */
    @Override
    protected void updateK() { k = 1.5 + 0.6 * currentWeight; }

    /**
     * Getter function of the robot's name
     */
    @Override
    public String getName() { return "Robot Y"; }
}