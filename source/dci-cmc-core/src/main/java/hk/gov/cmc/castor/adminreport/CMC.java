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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class CMC.
 * 
 * @version $Revision$ $Date$
 */
public class CMC implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _report
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Report _report;


      //----------------/
     //- Constructors -/
    //----------------/

    public CMC() 
     {
        super();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.CMC()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'report'.
     * 
     * @return Report
     * @return the value of field 'report'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Report getReport()
    {
        return this._report;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Report getReport() 

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
     * Sets the value of field 'report'.
     * 
     * @param report the value of field 'report'.
     */
    public void setReport(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Report report)
    {
        this._report = report;
    } //-- void setReport(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Report) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return CMC
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.CMC unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.CMC) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.CMC.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.CMC unmarshal(java.io.Reader) 

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
