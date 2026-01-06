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
 * Class OnlinePaymentType_ST.
 * 
 * @version $Revision$ $Date$
 */
public class OnlinePaymentType_ST implements java.io.Serializable {


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
    public static final OnlinePaymentType_ST Y = new OnlinePaymentType_ST(Y_TYPE, "Y");

    /**
     * The N type
     */
    public static final int N_TYPE = 1;

    /**
     * The instance of the N type
     */
    public static final OnlinePaymentType_ST N = new OnlinePaymentType_ST(N_TYPE, "N");

    /**
     * The A type
     */
    public static final int A_TYPE = 2;

    /**
     * The instance of the A type
     */
    public static final OnlinePaymentType_ST A = new OnlinePaymentType_ST(A_TYPE, "A");

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

    private OnlinePaymentType_ST(int type, java.lang.String value) 
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.OnlinePaymentType_ST(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * OnlinePaymentType_ST
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
     * Returns the type of this OnlinePaymentType_ST
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
        members.put("A", A);
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
     * Returns the String representation of this
     * OnlinePaymentType_ST
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
     * Returns a new OnlinePaymentType_ST based on the given String
     * value.
     * 
     * @param string
     * @return OnlinePaymentType_ST
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.OnlinePaymentType_ST valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid OnlinePaymentType_ST";
            throw new IllegalArgumentException(err);
        }
        return (OnlinePaymentType_ST) obj;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.OnlinePaymentType_ST valueOf(java.lang.String) 

}
