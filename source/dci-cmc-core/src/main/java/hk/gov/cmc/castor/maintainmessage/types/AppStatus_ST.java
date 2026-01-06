






package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types;






public enum AppStatus_ST {


      
     
    

    


    AR("AR"),
    


    UP("UP"),
    


    BP("BP"),
    


    RJ("RJ"),
    


    CP("CP"),
    


    CA("CA"),
    
    


    AP("AP"),
    


    DB("DB"),
    


    AL("AL"),
    


    PD("PD"),
    


    NA("NA");
    

    


    private final java.lang.String value;

    


    private static final java.util.Map<java.lang.String, AppStatus_ST> enumConstants = new java.util.HashMap<java.lang.String, AppStatus_ST>();


    static {
        for (AppStatus_ST c: AppStatus_ST.values()) {
            AppStatus_ST.enumConstants.put(c.value, c);
        }

    }

    private AppStatus_ST(final java.lang.String value) {
        this.value = value;
    }

    





    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.AppStatus_ST fromValue(final java.lang.String value) {
        AppStatus_ST c = AppStatus_ST.enumConstants.get(value);
        if (c != null) {
            return c;
        }
        throw new IllegalArgumentException(value);
    }

    




    public void setValue(final java.lang.String value) {
    }

    




    public java.lang.String toString() {
        return this.value;
    }

    




    public java.lang.String value() {
        return this.value;
    }

}
