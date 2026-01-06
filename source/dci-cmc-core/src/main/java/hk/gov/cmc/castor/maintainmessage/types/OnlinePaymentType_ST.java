











package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types;

  
 


import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;






public class OnlinePaymentType_ST implements java.io.Serializable {


      
     
    

    


    public static final int Y_TYPE = 0;

    


    public static final OnlinePaymentType_ST Y = new OnlinePaymentType_ST(Y_TYPE, "Y");

    


    public static final int N_TYPE = 1;

    


    public static final OnlinePaymentType_ST N = new OnlinePaymentType_ST(N_TYPE, "N");

    


    public static final int A_TYPE = 2;

    


    public static final OnlinePaymentType_ST A = new OnlinePaymentType_ST(A_TYPE, "A");

    


    private static java.util.Hashtable _memberTable = init();

    


    private int type = -1;

    


    private java.lang.String stringValue = null;


      
     
    

    private OnlinePaymentType_ST(int type, java.lang.String value) 
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
        members.put("Y", Y);
        members.put("N", N);
        members.put("A", A);
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

    








    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.OnlinePaymentType_ST valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid OnlinePaymentType_ST";
            throw new IllegalArgumentException(err);
        }
        return (OnlinePaymentType_ST) obj;
    } 

}
