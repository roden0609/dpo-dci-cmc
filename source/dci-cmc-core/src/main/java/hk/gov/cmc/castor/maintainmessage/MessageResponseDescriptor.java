
package hk.gov.cmc.castor.maintainmessage;

import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse;

public class MessageResponseDescriptor extends hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse_CTDescriptor {

    private boolean _elementDefinition;

    private java.lang.String _nsPrefix;

    private java.lang.String _nsURI;

    private java.lang.String _xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor _identity;

    public MessageResponseDescriptor() {
        super();
        setExtendsWithoutFlatten(new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse_CTDescriptor());
        _nsURI = "http://ws.mygovhk.gov.hk/schema/MaintainMessageResponse.xsd";
        _xmlName = "MessageResponse";
        _elementDefinition = true;
    }

    @Override()
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    }

    @Override()
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        if (_identity == null) {
            return super.getIdentity();
        }
        return _identity;
    }

    @Override()
    public java.lang.Class getJavaClass() {
        return hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse.class;
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
