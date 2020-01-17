package file;

import transportObject.TransportObject;
import java.util.HashSet;

public class Automate {
    private String name_;
    private HashSet<TransportObject> objects_;
    private boolean terminal_;
    private HashSet<Automate> childrenSet_;

    public Automate(String name, HashSet<Automate> childrenSet, boolean terminal)
    {
        name_ = name;
        childrenSet_ = childrenSet;
        objects_ = new HashSet<>();
        terminal_ = terminal;
    }

    /**
     * Getter of the automate's name
     * @return automate's name
     */
    public String getName(){ return name_; }

    /**
     * Function to add a child to an automate
     * @param child to be added
     */
    public void addChild(Automate child){ childrenSet_.add(child); }

    /**
     * Getter of the automate's direct objects
     * @return collection of all the Automate's objects
     */
    public HashSet<TransportObject> getObjects(){
        return objects_;
    }

    /**
     * Function to add an object to the current Automate
     * @param object to be added
     */
    public void addObjects(TransportObject object) { objects_.add(object); }

    /**
     * Setter for the terminal attribute
     * @param terminal indicated what to set the attribute at
     */
    public void setTerminal(boolean terminal) {
        terminal_ = terminal;
    }

    /**
     * Getter for an automate's children names
     * @param node where the children should be found
     * @return collection containing the objects names
     */
    private HashSet<String> getAllChildrenName(Automate node)
    {
        HashSet<String> childrenName = new HashSet<>();
        if (node.childrenSet_.isEmpty())
            return childrenName;
        for (Automate child : node.childrenSet_)
        {
            childrenName.add(child.getName());
            if (child.childrenSet_ != null)
                childrenName.addAll(getAllChildrenName(child));
        }
        return childrenName;
    }

    /**
     * Getter for the current automate's children names
     * @return collection containing the names
     */
    public HashSet<String> getAllChildrenName() { return getAllChildrenName(this); }

    /**
     * Getter for an automate's children as objects
     * @param node where the children should be found
     * @return collection containing the objects children
     */
    private HashSet<TransportObject> getAllChildrenObjects(Automate node)
    {
        HashSet<TransportObject> childrenObjects = new HashSet<>();
        if (node.childrenSet_.isEmpty())
            return objects_;
        for (Automate child : node.childrenSet_)
        {
            childrenObjects.addAll(child.getObjects());
            if (child.childrenSet_ != null)
                childrenObjects.addAll(getAllChildrenObjects(child));
        }
        return childrenObjects;
    }

    /**
     * Getter for the current automate's children as objects
     * @return collection containing the objects children
     */
    public HashSet<TransportObject> getAllChildrenObjects() { return getAllChildrenObjects(this); }

    public Automate getNodeByName(Automate node, String name)
    {
        int minimum;
        String childName;
        if (node.getName().equals(name))
            return node;
        for (Automate child : node.childrenSet_)
        {
            childName = child.getName();
            minimum = Math.min(childName.length(), name.length());
            if(childName.substring(0,minimum).equals(name.substring(0, minimum) ) )
                if (!child.childrenSet_.isEmpty() || child.terminal_)
                    return getNodeByName(child, name);
        }
        return null;
    }

    /**
     * Function to find an automate using it's name
     * @param name of the automate to find
     * @return Automate if found, else null
     */
    public Automate getNodeByName(String name) { return getNodeByName(this, name); }
}
