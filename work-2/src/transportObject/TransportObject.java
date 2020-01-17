package transportObject;

public abstract class TransportObject
{
    protected int weight_;
    protected String code_;
    protected String name_;
    protected String type_;

    /**
     * Constructor of a transport object
     * @param weight of the objects
     * @param name of the object
     * @param code unique 6 chars hexadecimal code of an object
     * @param type among 3 types, A, B or C
     */
    public TransportObject(int weight, String name, String code, String type)
    {
        weight_ = weight;
        name_ = name;
        code_ = code;
        type_ = type;
    }

    /**
     * Getter for the object's weight
     * @return the object's weight
     */
    public int getWeight() { return weight_; }

    /**
     * Getter for the object's unique code
     * @return the object's unique code
     */
    public String getHashCode() { return code_; }


    /**
     * Getter for the object's type
     * @return the object's type
     */
    public String getType() { return type_; }

    /**
     * Getter for the object's name
     * @return the object's name
     */
    public String getName() { return name_; }

    /**
     * Function to get a string from the object
     * @return the object's string
     */
    public String getString() { return name_ + " " + code_ + " " + type_; }
}
