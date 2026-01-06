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
 * Class PaymentTransactionAction_ST.
 * 
 * @version $Revision$ $Date$
 */
public class PaymentTransactionAction_ST implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Add type
     */
    public static final int ADD_TYPE = 0;

    /**
     * The instance of the Add type
     */
    public static final PaymentTransactionAction_ST ADD = new PaymentTransactionAction_ST(ADD_TYPE, "Add");

    /**
     * The Delete type
     */
    public static final int DELETE_TYPE = 1;

    /**
     * The instance of the Delete type
     */
    public static final PaymentTransactionAction_ST DELETE = new PaymentTransactionAction_ST(DELETE_TYPE, "Delete");

    /**
     * The Void type
     */
    public static final int VOID_TYPE = 2;

    /**
     * The instance of the Void type
     */
    public static final PaymentTransactionAction_ST VOID = new PaymentTransactionAction_ST(VOID_TYPE, "Void");

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

    private PaymentTransactionAction_ST(int type, java.lang.String value) 
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.PaymentTransactionAction_ST(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * PaymentTransactionAction_ST
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
     * Returns the type of this PaymentTransactionAction_ST
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
        members.put("Add", ADD);
        members.put("Delete", DELETE);
        members.put("Void", VOID);
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
     * PaymentTransactionAction_ST
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
     * Returns a new PaymentTransactionAction_ST based on the given
     * String value.
     * 
     * @param string
     * @return PaymentTransactionAction_ST
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.PaymentTransactionAction_ST valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid PaymentTransactionAction_ST";
            throw new IllegalArgumentException(err);
        }
        return (PaymentTransactionAction_ST) obj;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.PaymentTransactionAction_ST valueOf(java.lang.String) 

}
