package file;

import transportObject.TransportObject;
import transportObject.TransportObjectA;
import transportObject.TransportObjectB;
import transportObject.TransportObjectC;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.HashSet;

public class ReadFileLogic {

    private String line;

    private Automate automateNames;
    private HashSet<String> childrenName;

    private Automate automateCodes;
    private HashSet<String> childrenCodes;

    /**
     * Constructor, reads data from the file and creates the automats
     */
    public ReadFileLogic()
    {
        // automat for the name
        HashSet<Automate> childrenSetAutomat = new HashSet<>();
        childrenName = new HashSet<>();
        automateNames = new Automate("", childrenSetAutomat, false);

        // automat for the code
        HashSet<Automate> childrenSetCodesAutomate = new HashSet<>();
        childrenCodes = new HashSet<>();
        automateCodes = new Automate("", childrenSetCodesAutomate, false);

        readFile();
    }

    /**
     * Function to read data from a file
     */
    private void readFile()
    {
        BufferedReader inputFileBuffer;
        try {
            inputFileBuffer = new BufferedReader(new FileReader("inventaire.txt"));
            line = inputFileBuffer.readLine();
            readInventory(inputFileBuffer);
        } catch (IOException error) {
            error.printStackTrace();
            System.out.println("Error Inventory's file could not be opened.");
        }
    }

    /**
     * Read the sections with their information from a Buffer and store them in a set
     * @param inputFileBuffer BufferReader from where the info should be read
     */
    private void readInventory(BufferedReader inputFileBuffer) throws IOException
    {
        while(line != null && !line.isEmpty())
        {
            String name = getStringAtPosition();
            String code = getStringAtPosition();
            String type = getStringAtPosition();
            switch (type)
            {
                case "A" :
                    createAutomate(name, new TransportObjectA(name, code), automateNames, childrenName);
                    createAutomate(code, new TransportObjectA(name, code), automateCodes, childrenCodes);
                    break;
                case "B" :
                    createAutomate(name, new TransportObjectB(name, code), automateNames, childrenName);
                    createAutomate(code, new TransportObjectB(name, code), automateCodes, childrenCodes);
                    break;
                case "C" :
                    createAutomate(name, new TransportObjectC(name, code), automateNames, childrenName);
                    createAutomate(code, new TransportObjectC(name, code), automateCodes, childrenCodes);
                    break;
            }
            line = inputFileBuffer.readLine();
        }
    }

    /**
     * Function to parse a line following this format "A B C"
     * @return formatted string
     */
    private String getStringAtPosition()
    {
        int spaceIndex = line.indexOf(' ');
        String stringRead = spaceIndex != -1 ? line.substring(0, spaceIndex) : line;
        line = line.substring(spaceIndex + 1);
        return stringRead;
    }

    /**
     * Function to create an automate (tree) from the inventory
     * @param stringRead string to treat (name of code)
     * @param object object being create from that string
     * @param automate where to add the object (automateName or automateCode)
     * @param children where to add the read string (childrenName or childrenCode)
     */
    private void createAutomate(String stringRead, TransportObject object, Automate automate, HashSet<String> children)
    {
        Automate found;
        if(automate.getAllChildrenName().contains(stringRead))
        {
            found = automate.getNodeByName(stringRead);
            found.addObjects(object);
            return;
        }

        String lastString = "";
        Automate node = automate;
        Automate child = null;

        for (int i = 0; i < stringRead.length(); ++i)
        {
            lastString += stringRead.charAt(i);
            child = new Automate(lastString, new HashSet<>(), false);
            if (!node.getAllChildrenName().contains(lastString))
            {
                node.addChild(child);
                node = child;
            }
            else {
                 found = node.getNodeByName(lastString);
                if ( found != null)
                    node = found;
            }
            children.add(lastString);
        }
        child.setTerminal(true);
        child.addObjects(object);
    }

    /**
     * Getter for the attribute automateNames
     * @return attribute automateNames containing the different objects read from the file
     */
    public Automate getAutomateNames() { return automateNames; }

    /**
     * Getter for the attribute automateCodes
     * @return attribute automateCodes containing the different objects read from the file
     */
    public Automate getAutomateCodes() { return automateCodes; }

    /**
     * Getter for all the objects in the file
     * @return all the children's objects in the automat of names (same as the automate of codes)
     */
    public HashSet<TransportObject> getObjectsInFile() {
        return new HashSet<TransportObject>(automateNames.getAllChildrenObjects());
    }
}