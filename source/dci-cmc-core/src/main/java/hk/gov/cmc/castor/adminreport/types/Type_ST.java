/**
 * Design, Implementation and Support of Common Middleware Components and Reference Applications
 *
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 1.0</a>, using an XML
 *
 * Developed by: Gilbert Liao
 * Reviewed by: Kai So
 * Tester: Shaohui Wang
 *
 */

package hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Type_ST.
 *
 * @version $Revision$ $Date$
 */
public class Type_ST implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The e-Message type
     */
    public static final int E_MESSAGE_TYPE = 0;

    /**
     * The instance of the e-Message type
     */
    public static final Type_ST E_MESSAGE = new Type_ST(E_MESSAGE_TYPE, "e-Message");

    /**
     * The To-Do-item type
     */
    public static final int TO_DO_ITEM_TYPE = 1;

    /**
     * The instance of the To-Do-item type
     */
    public static final Type_ST TO_DO_ITEM = new Type_ST(TO_DO_ITEM_TYPE, "To-Do-item");

    /**
     * The iAM Smart Message type
     */
    public static final int IAS_MESSAGE_TYPE = 2;

    /**
     * The instance of the iAM Smart Message type
     */
    public static final Type_ST IAS_MESSAGE = new Type_ST(IAS_MESSAGE_TYPE, "iAM Smart Message");

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

    private Type_ST(int type, java.lang.String value)
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Type_ST(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     *
     * Returns an enumeration of all possible instances of Type_ST
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
     * Returns the type of this Type_ST
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
        members.put("e-Message", E_MESSAGE);
        members.put("To-Do-item", TO_DO_ITEM);
        members.put("iAM Smart Message", IAS_MESSAGE);
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
     * Returns the String representation of this Type_ST
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
     * Returns a new Type_ST based on the given String value.
     *
     * @param string
     * @return Type_ST
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Type_ST valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid Type_ST";
            throw new IllegalArgumentException(err);
        }
        return (Type_ST) obj;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Type_ST valueOf(java.lang.String)

}
