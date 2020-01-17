package com.menu;

public class Order
{
    private int[] list;

    /**
     * Constructor of an order, set's the number of object A to -1 (to know when order is taken)
     */
    public Order() { list = new int [3]; }

    /**
     * Function to get index of an objects type
     * A is 0
     * B is 1
     * C is 2
     * @return the object's index in the list
     */
    private int getIndex(String objectType)
    {
        if(objectType.equals("Object A"))
            return 0;
        if(objectType.equals("Object B"))
            return 1;
        if(objectType.equals("Object C"))
            return 2;
        throw new IllegalArgumentException();
    }

    /**
     * Function to add the number of requested object X to the list
     */
    public void takeOrder(String objectType, Integer amount) { list[getIndex(objectType)] = amount; }

    /**
     * Getter for the object's requested number in the order
     * @return number of object A in the current order
     */
    public int getNumberOfA() { return list[0]; }

    /**
     * Getter for the object's requested number in the order
     * @return number of object B in the current order
     */
    public int getNumberOfB() { return list[1]; }

    /**
     * Getter for the object's requested number in the order
     * @return number of object C in the current order
     */
    public int getNumberOfC() { return list[2]; }

    /**
     * Function to display the already taken order
     */
    void display()
    {
        System.out.println("Number of object A is " + list[0]);
        System.out.println("Number of object B is " + list[1]);
        System.out.println("Number of object C is " + list[2]);
    }
}
