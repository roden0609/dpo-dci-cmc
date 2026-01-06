package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;

public class ApplicationDescriptor extends Application_CTDescriptor {

    private boolean elementDefinition;
    private java.lang.String nsPrefix;
    private java.lang.String nsURI;
    private java.lang.String xmlName;
    private org.exolab.castor.xml.XMLFieldDescriptor identity;

    public ApplicationDescriptor() {
        super();
        setExtendsWithoutFlatten(new Application_CTDescriptor());
        nsURI = "http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd";
        xmlName = "Application";
        elementDefinition = true;
    }

    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    }

    public org.exolab.castor.mapping.ClassDescriptor getExtends() {
        return super.getExtends();
    }

    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        if (identity == null)
            return super.getIdentity();
        return identity;
    }

    public java.lang.Class getJavaClass() {
        return hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application.class;
    }

    public java.lang.String getNameSpacePrefix() {
        return nsPrefix;
    }

    public java.lang.String getNameSpaceURI() {
        return nsURI;
    }

    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    }

    public java.lang.String getXMLName() {
        return xmlName;
    }

    public boolean isElementDefinition() {
        return elementDefinition;
    }

}
