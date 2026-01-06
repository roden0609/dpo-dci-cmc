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
 * Class Report_CT.
 *
 * @version $Revision$ $Date$
 */
public class Report_CT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _reportID
     */
    private java.lang.String _reportID = "RPT-CMC-06-D";

    /**
     * Field _desc
     */
    private java.lang.String _desc = "Report on List of Undelivered e-Messages / To-do-items / iAM Smart Messages";

    /**
     * Field _mode
     */
    private java.lang.String _mode = "Daily";

    /**
     * Field _generationDate
     */
    private java.lang.String _generationDate;

    /**
     * Field _content
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Content _content;


      //----------------/
     //- Constructors -/
    //----------------/

    public Report_CT()
     {
        super();
        setReportID("RPT-CMC-06-D");
        setDesc("Report on List of Undelivered e-Messages / To-do-items / iAM Smart Messages");
        setMode("Daily");
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Report_CT()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'content'.
     *
     * @return Content
     * @return the value of field 'content'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Content getContent()
    {
        return this._content;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Content getContent()

    /**
     * Returns the value of field 'desc'.
     *
     * @return String
     * @return the value of field 'desc'.
     */
    public java.lang.String getDesc()
    {
        return this._desc;
    } //-- java.lang.String getDesc()

    /**
     * Returns the value of field 'generationDate'.
     *
     * @return String
     * @return the value of field 'generationDate'.
     */
    public java.lang.String getGenerationDate()
    {
        return this._generationDate;
    } //-- java.lang.String getGenerationDate()

    /**
     * Returns the value of field 'mode'.
     *
     * @return String
     * @return the value of field 'mode'.
     */
    public java.lang.String getMode()
    {
        return this._mode;
    } //-- java.lang.String getMode()

    /**
     * Returns the value of field 'reportID'.
     *
     * @return String
     * @return the value of field 'reportID'.
     */
    public java.lang.String getReportID()
    {
        return this._reportID;
    } //-- java.lang.String getReportID()

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
     * Sets the value of field 'content'.
     *
     * @param content the value of field 'content'.
     */
    public void setContent(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Content content)
    {
        this._content = content;
    } //-- void setContent(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Content)

    /**
     * Sets the value of field 'desc'.
     *
     * @param desc the value of field 'desc'.
     */
    public void setDesc(java.lang.String desc)
    {
        this._desc = desc;
    } //-- void setDesc(java.lang.String)

    /**
     * Sets the value of field 'generationDate'.
     *
     * @param generationDate the value of field 'generationDate'.
     */
    public void setGenerationDate(java.lang.String generationDate)
    {
        this._generationDate = generationDate;
    } //-- void setGenerationDate(java.lang.String)

    /**
     * Sets the value of field 'mode'.
     *
     * @param mode the value of field 'mode'.
     */
    public void setMode(java.lang.String mode)
    {
        this._mode = mode;
    } //-- void setMode(java.lang.String)

    /**
     * Sets the value of field 'reportID'.
     *
     * @param reportID the value of field 'reportID'.
     */
    public void setReportID(java.lang.String reportID)
    {
        this._reportID = reportID;
    } //-- void setReportID(java.lang.String)

    /**
     * Method unmarshal
     *
     *
     *
     * @param reader
     * @return Report_CT
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Report_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Report_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Report_CT.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Report_CT unmarshal(java.io.Reader)

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
