package com.menu;

import com.file.ReadFileLogic;
import com.graph.GraphConsole;
import com.route.RouteAlgorithm;
import com.sections.Section;
import com.transportObject.TransportObjectA;
import com.transportObject.TransportObjectB;
import com.transportObject.TransportObjectC;

import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    private Scanner inputStream = new Scanner(System.in);
    private Order order = new Order();
    private HashSet<Section> sectionsInFile = new HashSet<>();

    /**
     * Constructor, asks user for a menu choice until the user quits.
     */
    private Menu()
    {
        int choice = getUserChoice();
        while(choice != 0)
        {
            switch (choice)
            {
                default:
                    System.out.println("Please enter a valid choice!");
                    break;
                case 1:
                    System.out.println("----------Create graph----------");
                    createGraph();
                    break;
                case 2:
                    System.out.println("----------Display graph----------");
                    displayGraph();
                    break;
                case 3:
                    System.out.println("----------Take order----------");
                    takeOrder();
                    break;
                case 4:
                    System.out.println("----------Display order----------");
                    displayOrder();
                    break;
                case 5:
                    System.out.println("----------Find the shortest way----------");
                    findShortestPath();
                    break;
            }
            choice = getUserChoice();
        }
        System.out.println("End of program");
    }

    /**
     * Displays the available options to the user
     * @return What the user chose, or 0 to quit
     */
    private int printOptions()
    {
        System.out.println("\n");
        System.out.println("Choose between the following options: ");
        System.out.println("1 - Create graph");
        System.out.println("2 - Display graph");
        System.out.println("3 - Take order");
        System.out.println("4 - Display order");
        System.out.println("5 - Find the shortest way");
        System.out.println("To quit enter 'Q'");

        String userInput = inputStream.nextLine();
        int input = -1;
        try {
            input = Character.getNumericValue(userInput.charAt(0));
        } catch(StringIndexOutOfBoundsException error) {
            System.out.println("Enter a valid choice!");
            input = printOptions();
        }
        return userInput.equals("Q") ?  0 : input;
    }

    /**
     * Get the user's menu choice, and treat errors
     * @return What the user chooses
     */
    private int getUserChoice() { return printOptions(); }

    /**
     * Created an order from user's input
     */
    private void takeOrder()
    {
        boolean validChoice = false;
        while(!validChoice)
        {
            try {
                System.out.println("Object A amount : ");
                Scanner amountA = new Scanner(System.in);
                order.takeOrder(TransportObjectA.getName(), amountA.nextInt());
                validChoice = true;
            } catch (InputMismatchException error) {
                System.out.println("Enter a valid number!");
                validChoice = false;
            }
        }

        validChoice = false;
        while(!validChoice)
        {
            try {
                System.out.println("Object B amount : ");
                Scanner amountB = new Scanner(System.in);
                order.takeOrder(TransportObjectB.getName(), amountB.nextInt());
                validChoice = true;
            } catch (InputMismatchException error) {
                System.out.println("Enter a valid number!");
                validChoice = false;
            }
        }
        validChoice = false;
        while(!validChoice)
        {
            try {
                System.out.println("Object C amount : ");
                Scanner amountC = new Scanner(System.in);
                order.takeOrder(TransportObjectC.getName(), amountC.nextInt());
                validChoice = true;
            } catch (InputMismatchException error) {
                System.out.println("Enter a valid number!");
                validChoice = false;
            }
        }
    }

    /**
     * Displays the order (if created before) or an error!
     */
    private void displayOrder()
    {
        if(order.getNumberOfA() == 0 && order.getNumberOfB() == 0 && order.getNumberOfC() == 0)
            System.out.println("Your order is empty!");
        else
            order.display();
    }

    /**
     * Displays the graph (if created before) or an error!
     */
    private void displayGraph()
    {
        if(sectionsInFile.isEmpty())
            System.out.println("Please Create the graph first");
        else
        {
            GraphConsole graphOutput = new GraphConsole(sectionsInFile);
            graphOutput.display();
        }
    }

    /**
     * Creates the graph from the available information in the text file
     */
    private void createGraph()
    {
        ReadFileLogic file = new ReadFileLogic();
        sectionsInFile = file.getSectionsInFile();
        if(sectionsInFile.isEmpty())
            System.out.println("Sorry!! The graph couldn't be created :(");
        else
            System.out.println("File read and graph created successfully!");
    }

    /**
     * Finds the shortest path to complete the user's order
     */
    private void findShortestPath()
    {
        if(sectionsInFile.isEmpty())
            System.out.println("Please Create the graph first");
        else
        {
            RouteAlgorithm route = new RouteAlgorithm(sectionsInFile, order.getNumberOfA(),
                    order.getNumberOfB(), order.getNumberOfC());
            if (order.getNumberOfA() != 0 || order.getNumberOfB() != 0 || order.getNumberOfC() == 0)
                route.displayBestRoute();
        }
    }

    public static void main(String[] args)
    {
        new Menu();
    }
}