











package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types;

  
 


import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;






public class PaymentTransactionAction_ST implements java.io.Serializable {


      
     
    

    


    public static final int ADD_TYPE = 0;

    


    public static final PaymentTransactionAction_ST ADD = new PaymentTransactionAction_ST(ADD_TYPE, "Add");

    


    public static final int DELETE_TYPE = 1;

    


    public static final PaymentTransactionAction_ST DELETE = new PaymentTransactionAction_ST(DELETE_TYPE, "Delete");

    


    public static final int VOID_TYPE = 2;

    


    public static final PaymentTransactionAction_ST VOID = new PaymentTransactionAction_ST(VOID_TYPE, "Void");

    


    private static java.util.Hashtable _memberTable = init();

    


    private int type = -1;

    


    private java.lang.String stringValue = null;


      
     
    

    private PaymentTransactionAction_ST(int type, java.lang.String value) 
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
        members.put("Add", ADD);
        members.put("Delete", DELETE);
        members.put("Void", VOID);
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

    








    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.PaymentTransactionAction_ST valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid PaymentTransactionAction_ST";
            throw new IllegalArgumentException(err);
        }
        return (PaymentTransactionAction_ST) obj;
    } 

}
