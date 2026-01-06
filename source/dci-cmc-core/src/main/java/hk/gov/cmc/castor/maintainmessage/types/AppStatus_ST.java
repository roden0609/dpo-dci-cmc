/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.4.1</a>, using an XML
 * Schema.
 * $Id$
 */

package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types;

/**
 * Enumeration AppStatus_ST.
 * 
 * @version $Revision$ $Date$
 */
public enum AppStatus_ST {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant AR
     */
    AR("AR"),
    /**
     * Constant UP
     */
    UP("UP"),
    /**
     * Constant BP
     */
    BP("BP"),
    /**
     * Constant RJ
     */
    RJ("RJ"),
    /**
     * Constant CP
     */
    CP("CP"),
    /**
     * Constant CA
     */
    CA("CA"),
    // CMC-2025-026: Add new Status Codes for Application Status - BEGIN
    /**
     * Constant AP
     */
    AP("AP"),
    /**
     * Constant DB
     */
    DB("DB"),
    /**
     * Constant AL
     */
    AL("AL"),
    /**
     * Constant PD
     */
    PD("PD"),
    /**
     * Constant NA
     */
    NA("NA");
    // CMC-2025-026: Add new Status Codes for Application Status - END

    /**
     * Field value.
     */
    private final java.lang.String value;

    /**
     * Field enumConstants.
     */
    private static final java.util.Map<java.lang.String, AppStatus_ST> enumConstants = new java.util.HashMap<java.lang.String, AppStatus_ST>();


    static {
        for (AppStatus_ST c: AppStatus_ST.values()) {
            AppStatus_ST.enumConstants.put(c.value, c);
        }

    }

    private AppStatus_ST(final java.lang.String value) {
        this.value = value;
    }

    /**
     * Method fromValue.
     * 
     * @param value
     * @return the constant for this value
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.AppStatus_ST fromValue(final java.lang.String value) {
        AppStatus_ST c = AppStatus_ST.enumConstants.get(value);
        if (c != null) {
            return c;
        }
        throw new IllegalArgumentException(value);
    }

    /**
     * 
     * 
     * @param value
     */
    public void setValue(final java.lang.String value) {
    }

    /**
     * Method toString.
     * 
     * @return the value of this constant
     */
    public java.lang.String toString() {
        return this.value;
    }

    /**
     * Method value.
     * 
     * @return the value of this constant
     */
    public java.lang.String value() {
        return this.value;
    }

}
