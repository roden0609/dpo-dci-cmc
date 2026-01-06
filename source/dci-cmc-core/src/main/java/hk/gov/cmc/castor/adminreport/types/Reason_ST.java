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
 * Class Reason_ST.
 *
 * @version $Revision$ $Date$
 */
public class Reason_ST implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Delinked type
     */
    public static final int DELINKED_TYPE = 0;

    /**
     * The instance of the Delinked type
     */
    public static final Reason_ST DELINKED = new Reason_ST(DELINKED_TYPE, "Delinked");

    /**
     * The Account suspended type
     */
    public static final int ACCOUNT_SUSPENDED_TYPE = 1;

    /**
     * The instance of the Account suspended type
     */
    public static final Reason_ST ACCOUNT_SUSPENDED = new Reason_ST(ACCOUNT_SUSPENDED_TYPE, "Account suspended");

    /**
     * The general error type
     */
    public static final int GENERAL_ERROR_TYPE = 2;

    /**
     * The instance of the general error type
     */
    public static final Reason_ST GENERAL_ERROR = new Reason_ST(GENERAL_ERROR_TYPE, "General Error");

    /**
     * The Account de-registered type
     */
    public static final int ACCOUNT_DEREGISTERED_TYPE = 3;

    /**
     * The instance of the Account de-registered type
     */
    public static final Reason_ST ACCOUNT_DEREGISTERED = new Reason_ST(ACCOUNT_DEREGISTERED_TYPE, "User is de-registered");

    /**
     * The Account not active type
     */
    public static final int ACCOUNT_NOT_ACTIVE_TYPE = 4;

    /**
     * The instance of the Account not active type
     */
    public static final Reason_ST ACCOUNT_NOT_ACTIVE = new Reason_ST(ACCOUNT_NOT_ACTIVE_TYPE, "Account is not active");

    /**
     * The Account Opt-out to receive message type
     */
    public static final int ACCOUNT_OPT_OUT_TYPE = 5;

    /**
     * The instance of the Account Opt-out to receive message type
     */
    public static final Reason_ST ACCOUNT_OPT_OUT = new Reason_ST(ACCOUNT_OPT_OUT_TYPE, "User Opt-out to receive message");

    /**
     * The unknown error type
     */
    public static final int UNKNOWN_ERROR_TYPE = 2;

    /**
     * The instance of the unknown error type
     */
    public static final Reason_ST UNKNOWN_ERROR = new Reason_ST(UNKNOWN_ERROR_TYPE, "Unknown Error");

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

    private Reason_ST(int type, java.lang.String value)
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Reason_ST(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     *
     * Returns an enumeration of all possible instances of
     * Reason_ST
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
     * Returns the type of this Reason_ST
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
        members.put("Delinked", DELINKED);
        members.put("Account suspended", ACCOUNT_SUSPENDED);
        members.put("General Error", GENERAL_ERROR);
        members.put("User is de-registered", ACCOUNT_DEREGISTERED);
        members.put("Account is not active", ACCOUNT_NOT_ACTIVE);
        members.put("User Opt-out to receive message", ACCOUNT_OPT_OUT);
        members.put("Unknown Error", UNKNOWN_ERROR);
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
     * Returns the String representation of this Reason_ST
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
     * Returns a new Reason_ST based on the given String value.
     *
     * @param string
     * @return Reason_ST
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Reason_ST valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid Reason_ST";
            throw new IllegalArgumentException(err);
        }
        return (Reason_ST) obj;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Reason_ST valueOf(java.lang.String)

}
