/*
 * Design, Implementation and Support of Common Middleware Components and Reference Applications 
 * 
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0</a>, using an XML
 * 
 * Developed by: Gilbert Liao
 * Reviewed by: Kai So
 * Tester: Shaohui Wang
 */

package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Action_ST.
 * 
 * @version $Revision$ $Date$
 */
public class Action_ST implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The New type
     */
    public static final int NEW_TYPE = 0;

    /**
     * The instance of the New type
     */
    public static final Action_ST NEW = new Action_ST(NEW_TYPE, "New");

    /**
     * The Delete type
     */
    public static final int DELETE_TYPE = 1;

    /**
     * The instance of the Delete type
     */
    public static final Action_ST DELETE = new Action_ST(DELETE_TYPE, "Delete");

    /**
     * The Replace type
     */
    public static final int REPLACE_TYPE = 2;

    /**
     * The instance of the Replace type
     */
    public static final Action_ST REPLACE = new Action_ST(REPLACE_TYPE, "Replace");

    /**
     * The MarkComplete type
     */
    public static final int MARKCOMPLETE_TYPE = 3;

    /**
     * The instance of the MarkComplete type
     */
    public static final Action_ST MARKCOMPLETE = new Action_ST(MARKCOMPLETE_TYPE, "MarkComplete");

    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
    public static final int UPDATE_TYPE = 4;
    public static final Action_ST UPDATE = new Action_ST(UPDATE_TYPE, "Update");
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END

    /**
     * Field _memberTable
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private Action_ST(int type, java.lang.String value) 
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * Action_ST
     * 
     * @return Enumeration
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getType
     * 
     * Returns the type of this Action_ST
     * 
     * @return int
     */
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
     * Method init
     * 
     * 
     * 
     * @return Hashtable
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("New", NEW);
        members.put("Delete", DELETE);
        members.put("Replace", REPLACE);
        members.put("MarkComplete", MARKCOMPLETE);
        // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
        members.put("Update", UPDATE);
        // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method readResolve
     * 
     *  will be called during deserialization to replace the
     * deserialized object with the correct constant instance.
     * <br/>
     * 
     * @return Object
     */
    private java.lang.Object readResolve()
    {
        return valueOf(this.stringValue);
    } //-- java.lang.Object readResolve() 

    /**
     * Method toString
     * 
     * Returns the String representation of this Action_ST
     * 
     * @return String
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOf
     * 
     * Returns a new Action_ST based on the given String value.
     * 
     * @param string
     * @return Action_ST
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid Action_ST";
            throw new IllegalArgumentException(err);
        }
        return (Action_ST) obj;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST valueOf(java.lang.String) 

}
