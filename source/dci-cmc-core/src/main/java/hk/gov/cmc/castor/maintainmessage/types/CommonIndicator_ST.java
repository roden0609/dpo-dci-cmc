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
 * Class CommonIndicator_ST.
 * 
 * @version $Revision$ $Date$
 */
public class CommonIndicator_ST implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Y type
     */
    public static final int Y_TYPE = 0;

    /**
     * The instance of the Y type
     */
    public static final CommonIndicator_ST Y = new CommonIndicator_ST(Y_TYPE, "Y");

    /**
     * The N type
     */
    public static final int N_TYPE = 1;

    /**
     * The instance of the N type
     */
    public static final CommonIndicator_ST N = new CommonIndicator_ST(N_TYPE, "N");

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

    private CommonIndicator_ST(int type, java.lang.String value) 
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * CommonIndicator_ST
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
     * Returns the type of this CommonIndicator_ST
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
        members.put("Y", Y);
        members.put("N", N);
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
     * Returns the String representation of this CommonIndicator_ST
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
     * Returns a new CommonIndicator_ST based on the given String
     * value.
     * 
     * @param string
     * @return CommonIndicator_ST
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CommonIndicator_ST";
            throw new IllegalArgumentException(err);
        }
        return (CommonIndicator_ST) obj;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST valueOf(java.lang.String) 

}
