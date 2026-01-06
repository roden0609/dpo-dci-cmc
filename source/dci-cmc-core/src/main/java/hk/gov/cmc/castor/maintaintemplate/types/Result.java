






package hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.types;

  
 


import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;






public class Result implements java.io.Serializable {


      
     
    

    


    public static final int VALUE_0_TYPE = 0;

    


    public static final Result VALUE_0 = new Result(VALUE_0_TYPE, "0000");

    


    public static final int VALUE_1_TYPE = 1;

    


    public static final Result VALUE_1 = new Result(VALUE_1_TYPE, "0001");

    


    public static final int VALUE_9999_TYPE = 2;

    


    public static final Result VALUE_9999 = new Result(VALUE_9999_TYPE, "9999");

    


    private static java.util.Hashtable _memberTable = init();

    


    private int type = -1;

    


    private java.lang.String stringValue = null;


      
     
    

    private Result(int type, java.lang.String value) 
     {
        super();
        this.type = type;
        this.stringValue = value;
    } 


      
     
    

    






    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } 

    






    public int getType()
    {
        return this.type;
    } 

    






    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("0000", VALUE_0);
        members.put("0001", VALUE_1);
        members.put("9999", VALUE_9999);
        return members;
    } 

    








    private java.lang.Object readResolve()
    {
        return valueOf(this.stringValue);
    } 

    






    public java.lang.String toString()
    {
        return this.stringValue;
    } 

    







    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.types.Result valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid Result";
            throw new IllegalArgumentException(err);
        }
        return (Result) obj;
    } 

}
