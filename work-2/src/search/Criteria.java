package search;

public class Criteria
{
    private String type_;
    private String code_;
    private String name_;

    /**
     * Constructor of a criteria, empty criteria
     */
    public Criteria()
    {
        name_ = "";
        code_ = "";
        type_ = "";
    }

    /**
     * Constructor of a criteria, entered by user for the search
     * @param input from where the infos should be extracted
     */
    public Criteria(String input)
    {
        int positionSpace = input.indexOf(" ");
        name_ = input.substring(0, positionSpace);
        int positionSpace2 = input.substring(positionSpace + 1).indexOf(" ") + positionSpace + 1;
        code_ = input.substring(positionSpace + 1, positionSpace2);
        type_ = input.substring(positionSpace2 + 1);
    }

    /**
     * Getter for the criteria's type
     * @return type of the current criteria
     */
    public String getType() { return type_; }

    /**
     * Setter for the criteria's type
     * @param  type of the current criteria
     */
    public void setType(String type) { type_ = type; }

    /**
     * Getter for the criteria's code
     * @return code of the current criteria
     */
    public String getCode() { return code_; }

    /**
     * Setter for the criteria's code
     * @param  code of the current criteria
     */
    public void setCode(String code) { code_ = code; }

    /**
     * Getter for the criteria's name
     * @return name of the current criteria
     */
    public String getName() { return name_; }

    /**
     * Setter for the criteria's name
     * @param name of the current criteria
     */
    public void setName(String name) { name_ = name; }

    /**
     * Function to know if the criteria's type is set
     * @return true if set, false else
     */
    public boolean hasType() { return type_.equals("A") || type_.equals("B") || type_.equals("C"); }

    /**
     * Function to know if the criteria's name is set
     * @return true if set, false else
     */
    public boolean hasName() { return !name_.equals(""); }

    /**
     * Function to know if the criteria's code is set
     * @return true if set, false else
     */
    public boolean hasCode() { return !code_.equals(""); }

}
