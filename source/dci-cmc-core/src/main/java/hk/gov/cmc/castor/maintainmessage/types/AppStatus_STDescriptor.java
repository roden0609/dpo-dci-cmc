
package hk.gov.cmc.castor.maintainmessage.types;

import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.AppStatus_ST;

public class AppStatus_STDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    private boolean _elementDefinition;

    private java.lang.String _nsPrefix;

    private java.lang.String _nsURI;

    private java.lang.String _xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor _identity;

    public AppStatus_STDescriptor() {
        super();
        _nsURI = "http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd";
        _xmlName = "AppStatus_ST";
        _elementDefinition = false;
    }

    @Override()
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    }

    @Override()
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return _identity;
    }

    @Override()
    public java.lang.Class getJavaClass() {
        return hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.AppStatus_ST.class;
    }

    @Override()
    public java.lang.String getNameSpacePrefix() {
        return _nsPrefix;
    }

    @Override()
    public java.lang.String getNameSpaceURI() {
        return _nsURI;
    }

    @Override()
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    }

    @Override()
    public java.lang.String getXMLName() {
        return _xmlName;
    }

    public boolean isElementDefinition() {
        return _elementDefinition;
    }

}
