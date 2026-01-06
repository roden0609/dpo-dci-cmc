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

package hk.gov.ogcio.mars_cmc.cmc.castor.adminreport;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Content_CT.
 * 
 * @version $Revision$ $Date$
 */
public class Content_CT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _serviceProviderList
     */
    private java.util.Vector _serviceProviderList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Content_CT() 
     {
        super();
        _serviceProviderList = new Vector();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Content_CT()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addServiceProvider
     * 
     * 
     * 
     * @param vServiceProvider
     */
    public void addServiceProvider(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider vServiceProvider)
        throws java.lang.IndexOutOfBoundsException
    {
        _serviceProviderList.addElement(vServiceProvider);
    } //-- void addServiceProvider(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider) 

    /**
     * Method addServiceProvider
     * 
     * 
     * 
     * @param index
     * @param vServiceProvider
     */
    public void addServiceProvider(int index, hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider vServiceProvider)
        throws java.lang.IndexOutOfBoundsException
    {
        _serviceProviderList.insertElementAt(vServiceProvider, index);
    } //-- void addServiceProvider(int, hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider) 

    /**
     * Method enumerateServiceProvider
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateServiceProvider()
    {
        return _serviceProviderList.elements();
    } //-- java.util.Enumeration enumerateServiceProvider() 

    /**
     * Method getServiceProvider
     * 
     * 
     * 
     * @param index
     * @return ServiceProvider
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider getServiceProvider(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _serviceProviderList.size())) {
            throw new IndexOutOfBoundsException("getServiceProvider: Index value '"+index+"' not in range [0.."+(_serviceProviderList.size() - 1) + "]");
        }
        
        return (hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider) _serviceProviderList.elementAt(index);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider getServiceProvider(int) 

    /**
     * Method getServiceProvider
     * 
     * 
     * 
     * @return ServiceProvider
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider[] getServiceProvider()
    {
        int size = _serviceProviderList.size();
        hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider[] mArray = new hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider) _serviceProviderList.elementAt(index);
        }
        return mArray;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider[] getServiceProvider() 

    /**
     * Method getServiceProviderCount
     * 
     * 
     * 
     * @return int
     */
    public int getServiceProviderCount()
    {
        return _serviceProviderList.size();
    } //-- int getServiceProviderCount() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method removeAllServiceProvider
     * 
     */
    public void removeAllServiceProvider()
    {
        _serviceProviderList.removeAllElements();
    } //-- void removeAllServiceProvider() 

    /**
     * Method removeServiceProvider
     * 
     * 
     * 
     * @param index
     * @return ServiceProvider
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider removeServiceProvider(int index)
    {
        java.lang.Object obj = _serviceProviderList.elementAt(index);
        _serviceProviderList.removeElementAt(index);
        return (hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider) obj;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider removeServiceProvider(int) 

    /**
     * Method setServiceProvider
     * 
     * 
     * 
     * @param index
     * @param vServiceProvider
     */
    public void setServiceProvider(int index, hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider vServiceProvider)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _serviceProviderList.size())) {
            throw new IndexOutOfBoundsException("setServiceProvider: Index value '"+index+"' not in range [0.." + (_serviceProviderList.size() - 1) + "]");
        }
        _serviceProviderList.setElementAt(vServiceProvider, index);
    } //-- void setServiceProvider(int, hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider) 

    /**
     * Method setServiceProvider
     * 
     * 
     * 
     * @param serviceProviderArray
     */
    public void setServiceProvider(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider[] serviceProviderArray)
    {
        //-- copy array
        _serviceProviderList.removeAllElements();
        for (int i = 0; i < serviceProviderArray.length; i++) {
            _serviceProviderList.addElement(serviceProviderArray[i]);
        }
    } //-- void setServiceProvider(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Content_CT
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Content_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Content_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Content_CT.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Content_CT unmarshal(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
