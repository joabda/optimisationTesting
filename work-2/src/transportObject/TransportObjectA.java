package transportObject;

public class TransportObjectA extends TransportObject
{
    public static final int weight = 1;
    public static final String type = "A";

    /**
     * Constructor of a transport object type A
     * @param name of the object
     * @param code unique 6 chars hexadecimal code of an object
     */
    public TransportObjectA(String name, String code) { super(weight, name, code, type); }

    /**
     * Constructor of a transport object type A from a normal Transport Object
     * @param object to construct from
     */
    public TransportObjectA(TransportObject object)
    {
        super(weight, object.getName(), object.getHashCode(), object.getType());
    }
}