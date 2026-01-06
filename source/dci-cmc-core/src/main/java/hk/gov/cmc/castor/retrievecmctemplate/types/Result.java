/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0</a>, using an XML
 * Schema.
 * $Id$
 */

package hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Result.
 * 
 * @version $Revision$ $Date$
 */
public class Result implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The 0000 type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the 0000 type
     */
    public static final Result VALUE_0 = new Result(VALUE_0_TYPE, "0000");

    /**
     * The 0001 type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the 0001 type
     */
    public static final Result VALUE_1 = new Result(VALUE_1_TYPE, "0001");

    /**
     * The 9999 type
     */
    public static final int VALUE_9999_TYPE = 2;

    /**
     * The instance of the 9999 type
     */
    public static final Result VALUE_9999 = new Result(VALUE_9999_TYPE, "9999");

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

    private Result(int type, java.lang.String value) 
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.types.Result(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of Result
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
     * Returns the type of this Result
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
        members.put("0000", VALUE_0);
        members.put("0001", VALUE_1);
        members.put("9999", VALUE_9999);
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
     * Returns the String representation of this Result
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
     * Returns a new Result based on the given String value.
     * 
     * @param string
     * @return Result
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.types.Result valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid Result";
            throw new IllegalArgumentException(err);
        }
        return (Result) obj;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.types.Result valueOf(java.lang.String) 

}
