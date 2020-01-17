package menu;
import file.ReadFileLogic;
import search.Criteria;
import search.Search;
import transportObject.ObjectManager;
import transportObject.TransportObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class Menu
{
    private JFrame mainWindow;
    private JList<String> suggested;
    private DefaultListModel<String> suggestedModel;
    private JList<String> available;
    private DefaultListModel<String> availableModel;
    private JList<String> cart;
    private DefaultListModel<String> cartModel;
    private JLabel weight;
    private int weightInt;
    private JLabel number;
    private int suggest = 0;

    private ObjectManager manager = new ObjectManager();
    private ObjectManager orderManager = new ObjectManager();
    private ObjectManager suggestedItems = new ObjectManager();
    private Criteria currentCriteria = new Criteria();
    Search search;


    private Menu()
    {
        mainWindow = new JFrame();
        mainWindow.setTitle("LOG2810 - TP2");
        addButtons();
        addSuggestedItems();
        addInputFields();
        addCriteria();
        addAvailableItems();
        addCart();
        mainWindow.setSize(1100,500);
        mainWindow.setLayout(null);
        mainWindow.setVisible(true);
        mainWindow.setResizable(false);
        centerWindow();
    }

    /**
     * Function to read a file creating the automate
     */
    private void readFile()
    {
        ReadFileLogic file = new ReadFileLogic();
        HashSet<TransportObject> objectsInFile = file.getObjectsInFile();
        search = new Search(manager, file.getAutomateNames(), file.getAutomateCodes());
        if(objectsInFile.isEmpty())
            JOptionPane.showMessageDialog(mainWindow, "Sorry!! There was a problem with the initiation of the program :(");
        else
        {
            for(TransportObject element: objectsInFile)
                manager.add(element);
            availableModel.addAll( manager.getElementsString() );
        }
    }

    /**
     * Function to switch elements between different containers/window
     */
    private TransportObject switchElements(JList<String> listFrom, DefaultListModel<String> modelFrom, DefaultListModel<String> modelTo,
                                ObjectManager from, ObjectManager to, boolean removeFromAvailable, boolean addToCart)
    {
        TransportObject result = null;
        if(listFrom.getSelectedValue() != null)
        {
            String selected = listFrom.getSelectedValue();
            Criteria searchSelected = new Criteria(selected);
            result = from.findByCode(searchSelected.getCode());
            if (result != null)
            {
                if(orderManager.findByCode(result.getHashCode()) != null && listFrom.equals(available))
                    return null;
                if(!listFrom.equals(orderManager)){
                    if (addToCart){
                        suggest--;
                        weightInt += result.getWeight();
                    }
                    else{
                        weightInt -= result.getWeight();
                    }
                }
                weight.setText(weightInt + " kg");
                if(weightInt > 25)
                    JOptionPane.showMessageDialog(mainWindow, "Warning: Critical weight!!\n Current Weight" +
                            " is " + weight.getText() + " > allowed weight of 25 kg.");
                from.remove(result);
                modelFrom.removeElement(result.getString());
                to.add(result);
                modelTo.add(modelTo.size(), result.getString());
                if(removeFromAvailable)
                {
                    manager.remove(result);
                    availableModel.removeElement(result.getString());
                }
            }
            else
                JOptionPane.showMessageDialog(mainWindow, "Item already added to the cart, please refresh suggestions");
        }
        else
            JOptionPane.showMessageDialog(mainWindow, "Error please select an element!!");
        return result;
    }

    /**
     * Function to empty the cart
     */
    private void emptyCart(DefaultListModel<String> modelFrom, DefaultListModel<String> modelTo,
                           ObjectManager from, ObjectManager to){
        for (var result : from.getElements()) {
            if(result != null)
            {
                from.remove(result);
                modelFrom.removeElement(result.getString());
                to.add(result);
                modelTo.add(modelTo.size(), result.getString());
                suggest--;
            }
        }
    }

    /**
     * Function to add the buttons and handle the event
     */
    private void addButtons()
    {
        int BUTTONWIDTH = 220;
        int BUTTONHEIGHT = 40;
        int POSITIONX = 10;

        JButton initiate = new JButton("Initiate");
        initiate.setBounds(POSITIONX,10, BUTTONWIDTH, BUTTONHEIGHT);
        initiate.addActionListener(e -> {
            if(manager.isEmpty())
                readFile();
            else
                JOptionPane.showMessageDialog(mainWindow, "File already read.");
        });

        JButton addToOrder = new JButton("Add To Cart");
        addToOrder.setBounds(POSITIONX,60, BUTTONWIDTH, BUTTONHEIGHT);
        addToOrder.addActionListener(e -> {
            TransportObject result = switchElements(available, availableModel, cartModel, manager, orderManager, false, true);
            if (result != null){
                suggestedItems.remove(result);
                suggestedModel.removeElement(result.toString());
            }

        });

        JButton addFromSuggestion = new JButton("Add From Suggestion To Cart");
        addFromSuggestion.setBounds(POSITIONX,110, BUTTONWIDTH, BUTTONHEIGHT);
        addFromSuggestion.addActionListener(e -> {
            switchElements(suggested, suggestedModel, cartModel, suggestedItems, orderManager, true, true);
            number.setText(suggest + " ");
        });

        JButton removeFromOrder = new JButton("Remove From Cart");
        removeFromOrder.setBounds(POSITIONX,160, BUTTONWIDTH, BUTTONHEIGHT);
        removeFromOrder.addActionListener(e -> switchElements(cart, cartModel, availableModel, orderManager, manager, false, false));

        JButton emptyOrder = new JButton("Empty Cart");
        emptyOrder.setBounds(POSITIONX,210, BUTTONWIDTH, BUTTONHEIGHT);
        emptyOrder.addActionListener(e -> {
            emptyCart(cartModel, availableModel, orderManager, manager);
            cartModel.removeAllElements();
            orderManager = new ObjectManager();
            weightInt = 0;
            weight.setText("0 kg");
        });

        JButton order = new JButton("Order");
        order.setBounds(POSITIONX,260, BUTTONWIDTH, BUTTONHEIGHT);
        order.addActionListener(e -> {
            if(weightInt == 0){
                JOptionPane.showMessageDialog(mainWindow, "Sorry your cart is empty :(\n Too bad if you don't want any of these items ¯\\_(ツ)_/¯");
            }
            else if(weightInt > 25)
                JOptionPane.showMessageDialog(mainWindow, "Sorry the cart's weight should be less than 25 kg to order\n" +
                        "Remove some items and try again later :)");
            else
            {
                for(TransportObject element: orderManager.getElements())
                {
                    manager.remove(element);
                    availableModel.removeElement(element.getString());
                }
                cartModel.removeAllElements();
                orderManager = new ObjectManager();
                weightInt = 0;
                weight.setText("0 kg");
            }
        });

        JButton quit = new JButton("Quit");
        quit.setBounds(POSITIONX,310, BUTTONWIDTH, BUTTONHEIGHT);
        quit.addActionListener(e -> System.exit(0));

        mainWindow.add(initiate);
        mainWindow.add(addToOrder);
        mainWindow.add(addFromSuggestion);
        mainWindow.add(removeFromOrder);
        mainWindow.add(emptyOrder);
        mainWindow.add(order);
        mainWindow.add(quit);
    }
    /**
     * Function to add the suggested item fields
     */
    private void addSuggestedItems()
    {
        JLabel suggestedLabel = new JLabel("Suggested Items using inputted criteria");
        suggestedLabel.setBounds(250 ,22, 275, 40);
        mainWindow.add(suggestedLabel);

        JLabel suggestedNumber = new JLabel("Number of suggested elements : ");
        suggestedNumber.setBounds(300 ,250, 275, 20);
        mainWindow.add(suggestedNumber);

        number = new JLabel("0");
        number.setBounds(490 ,250, 270, 20);
        mainWindow.add(number);

        JScrollPane scrollPane = new JScrollPane();
        suggestedModel = new DefaultListModel<>();
        suggested = new JList<>(suggestedModel);

        scrollPane.setViewportView(suggested);
        scrollPane.setBounds(300,52, 200, 200);
        mainWindow.add(scrollPane);
    }

    /**
     * Function to add the inputs fields for the criteria
     */
    private void addInputFields()
    {
        int POSITIONX = 300;
        int LABELWIDTH = 75;
        int LABELHEIGHT = 40;

        JLabel type = new JLabel("Type");
        type.setBounds(POSITIONX,290, LABELWIDTH, LABELHEIGHT);
        mainWindow.add(type);

        JLabel name = new JLabel("Name");
        name.setBounds(POSITIONX,340, LABELWIDTH, LABELHEIGHT);
        mainWindow.add(name);


        JLabel code = new JLabel("Code");
        code.setBounds(POSITIONX,390, LABELWIDTH, LABELHEIGHT);
        mainWindow.add(code);
    }

    /**
     * Function to find an item using the critea entered, search for it and add it to the suggestion box
     * @param search search algorithm to find an item
     */
    private void addToSuggested(Search search)
    {
        suggestedModel.removeAllElements();
        suggestedItems = new ObjectManager();
        suggest = 0;
        if(search.exists(currentCriteria))
        {
            for(TransportObject element: search.getResults()) {
                suggest++;
                if (suggest <= 10)
                    suggestedItems.add(element);
                else
                    suggest--;
            }
            suggestedModel.addAll(suggestedItems.getElementsString());
        }
    }

    /**
     * Function to add the criteria fields, and signals so when the user inputs something
     *          it will be transmitted to the right function
     */
    private void addCriteria()
    {
        int POSITIONX = 350;
        int LABELWIDTH = 150;
        int LABELHEIGHT = 20;

        JLabel criteriaLabel = new JLabel("Criteria To Get Suggestions");
        criteriaLabel.setBounds(POSITIONX - 50,272, LABELWIDTH+100, LABELHEIGHT);
        mainWindow.add(criteriaLabel);

        JComboBox<String> type = new JComboBox<>();
        type.addItem(" ");
        type.addItem("A");
        type.addItem("B");
        type.addItem("C");
        type.setBounds(POSITIONX,302, LABELWIDTH, LABELHEIGHT);
        mainWindow.add(type);
        type.addActionListener (e -> {
            String input = type.getSelectedItem().toString();
            currentCriteria.setType(input);
            suggestedItems = new ObjectManager();
            suggestedModel.removeAllElements();
            addToSuggested(search);
            number.setText(suggest + " ");
        });
        JTextArea name = new JTextArea();
        name.setBounds(POSITIONX,352, LABELWIDTH, LABELHEIGHT);
        mainWindow.add(name);
        name.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) { }

            @Override
            public void keyPressed(KeyEvent keyEvent) { }

            @Override
            public void keyReleased(KeyEvent keyEvent)
            {
                currentCriteria.setName(name.getText());
                addToSuggested(search);
                number.setText(suggest + " ");
            }
        });

        JTextArea code = new JTextArea();
        code.setBounds(POSITIONX,402, LABELWIDTH, LABELHEIGHT);
        mainWindow.add(code);
        code.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) { }

            @Override
            public void keyPressed(KeyEvent keyEvent) { }

            @Override
            public void keyReleased(KeyEvent keyEvent)
            {
                currentCriteria.setCode(code.getText());
                addToSuggested(search);
                number.setText(suggest + " ");
            }
        });
        JButton refresh = new JButton("Refresh");
        refresh.setBounds(POSITIONX,430, 150, 20);
        refresh.addActionListener(e -> {
            currentCriteria.setCode(code.getText());
            currentCriteria.setName(name.getText());
            currentCriteria.setType(type.getSelectedItem().toString());
            addToSuggested(search);
            number.setText(suggest + " ");
        });
        mainWindow.add(refresh);

    }

    /**
     * Function returns to add the available items option
     */
    private void addAvailableItems()
    {
        JLabel availableLabel = new JLabel("Available items to add");
        availableLabel.setBounds(620 ,22, 175, 40);
        mainWindow.add(availableLabel);

        availableModel = new DefaultListModel<>();
        available = new JList<>(availableModel);

        JScrollPane scrollPane = new JScrollPane();

        scrollPane.setViewportView(available);
        scrollPane.setBounds(600,52, 200, 400);
        mainWindow.add(scrollPane);
    }

    /**
     * Function to add the cart options
     */
    private void addCart()
    {
        JLabel cartLabel = new JLabel("Items Currently in cart");
        cartLabel.setBounds(870 ,22, 175, 40);
        mainWindow.add(cartLabel);

        JScrollPane scrollPane = new JScrollPane();
        cartModel = new DefaultListModel<>();
        cart = new JList<>(cartModel);

        scrollPane.setViewportView(cart);
        scrollPane.setBounds(850,52, 200, 300);
        mainWindow.add(scrollPane);

        JLabel weightLabel = new JLabel("Cart's Weight");
        weightLabel.setBounds(850 ,372, 100, 20);
        mainWindow.add(weightLabel);

        weight = new JLabel("0 kg");
        weight.setBounds(950, 372, 100, 20);
        mainWindow.add(weight);
    }

    /**
     * Function to center the UI
     */
    private void centerWindow()
    {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - mainWindow.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - mainWindow.getHeight()) / 2);
        mainWindow.setLocation(x, y);
    }

    public static void main(String[] args)
    {
        new Menu();
    }
}
