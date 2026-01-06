
package hk.gov.cmc.castor.maintainmessage;

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

public class MetaData_CTDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    private boolean elementDefinition;

    private java.lang.String nsPrefix;

    private java.lang.String nsURI;

    private java.lang.String xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor identity;

    public MetaData_CTDescriptor()
    {
        super();
        nsURI = "http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd";
        xmlName = "MetaData.CT";
        elementDefinition = false;

        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_dataContentEN", "DataContentEN", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                MetaData_CT target = (MetaData_CT) object;
                return target.getDataContentEN();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MetaData_CT target = (MetaData_CT) object;
                    target.setDataContentEN( (java.lang.String) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_dataContentTC", "DataContentTC", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                MetaData_CT target = (MetaData_CT) object;
                return target.getDataContentTC();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MetaData_CT target = (MetaData_CT) object;
                    target.setDataContentTC( (java.lang.String) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_dataContentSC", "DataContentSC", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                MetaData_CT target = (MetaData_CT) object;
                return target.getDataContentSC();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MetaData_CT target = (MetaData_CT) object;
                    target.setDataContentSC( (java.lang.String) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo.class, "_billAccountInfo", "BillAccountInfo", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                MetaData_CT target = (MetaData_CT) object;
                return target.getBillAccountInfo();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MetaData_CT target = (MetaData_CT) object;
                    target.setBillAccountInfo( (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient.class, "_recipientList", "Recipient", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                MetaData_CT target = (MetaData_CT) object;
                return target.getRecipient();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MetaData_CT target = (MetaData_CT) object;
                    target.addRecipient( (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setRequired(true);
        desc.setMultivalued(true);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
        }
        desc.setValidator(fieldValidator);
    }

    public org.exolab.castor.mapping.AccessMode getAccessMode()
    {
        return null;
    }

    public org.exolab.castor.mapping.ClassDescriptor getExtends()
    {
        return null;
    }

    public org.exolab.castor.mapping.FieldDescriptor getIdentity()
    {
        return identity;
    }

    public java.lang.Class getJavaClass()
    {
        return hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData_CT.class;
    }

    public java.lang.String getNameSpacePrefix()
    {
        return nsPrefix;
    }

    public java.lang.String getNameSpaceURI()
    {
        return nsURI;
    }

    public org.exolab.castor.xml.TypeValidator getValidator()
    {
        return this;
    }

    public java.lang.String getXMLName()
    {
        return xmlName;
    }

    public boolean isElementDefinition()
    {
        return elementDefinition;
    }

}
