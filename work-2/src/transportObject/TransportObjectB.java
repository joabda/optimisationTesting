package transportObject;

public class TransportObjectB extends TransportObject
{
    public static final int weight = 3;
    public static final String type = "B";

    /**
     * Constructor of a transport object type B
     * @param name of the object
     * @param code unique 6 chars hexadecimal code of an object
     */
    public TransportObjectB(String name, String code)
    {
        super(weight, name, code, type);
    }

    /**
     * Constructor of a transport object type B from a normal Transport Object
     * @param object to construct from
     */
    public TransportObjectB(TransportObject object)
    {
        super(weight, object.getName(), object.getHashCode(), object.getType());
    }
}