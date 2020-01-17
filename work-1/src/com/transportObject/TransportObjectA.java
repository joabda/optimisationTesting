package com.transportObject;

public class TransportObjectA extends TransportObject
{
    public static final int weight = 1;
    
    /**
     * Getter for the object's constant weight
     * @return Object's weight
     */
    @Override
    public int getWeight() { return weight; }

    /**
     * Getter for the object's constant name
     * @return Object's name
     */
    static public String getName() { return "Object A"; }
}