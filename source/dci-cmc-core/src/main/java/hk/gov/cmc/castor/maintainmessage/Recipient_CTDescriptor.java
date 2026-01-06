
package hk.gov.cmc.castor.maintainmessage;

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

public class Recipient_CTDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    private boolean elementDefinition;

    private java.lang.String nsPrefix;

    private java.lang.String nsURI;

    private java.lang.String xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor identity;

    public Recipient_CTDescriptor()
    {
        super();
        nsURI = "http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd";
        xmlName = "Recipient.CT";
        elementDefinition = false;

        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_tranID", "TranID", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                Recipient_CT target = (Recipient_CT) object;
                return target.getTranID();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Recipient_CT target = (Recipient_CT) object;
                    target.setTranID( (java.lang.String) value);
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

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_idpID", "IdpID", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                Recipient_CT target = (Recipient_CT) object;
                return target.getIdpID();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Recipient_CT target = (Recipient_CT) object;
                    target.setIdpID( (java.lang.String) value);
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

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_recipientID", "RecipientID", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                Recipient_CT target = (Recipient_CT) object;
                return target.getRecipientID();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Recipient_CT target = (Recipient_CT) object;
                    target.setRecipientID( (java.lang.String) value);
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

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.util.Date.class, "_itemDate", "ItemDate", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                Recipient_CT target = (Recipient_CT) object;
                return target.getItemDate();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Recipient_CT target = (Recipient_CT) object;
                    target.setItemDate( (java.util.Date) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new java.util.Date();
            }
        };
        handler = new org.exolab.castor.xml.handlers.DateFieldHandler(handler);
        desc.setImmutable(true);
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST.class, "_action", "Action", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                Recipient_CT target = (Recipient_CT) object;
                return target.getAction();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Recipient_CT target = (Recipient_CT) object;
                    target.setAction( (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        };
        handler = new org.exolab.castor.xml.handlers.EnumFieldHandler(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST.class, handler);
        desc.setImmutable(true);
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_correlatedTranID", "CorrelatedTranID", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                Recipient_CT target = (Recipient_CT) object;
                return target.getCorrelatedTranID();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Recipient_CT target = (Recipient_CT) object;
                    target.setCorrelatedTranID( (java.lang.String) value);
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

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_recipientIDType", "RecipientIDType", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Recipient_CT target = (Recipient_CT) object;
                return target.getRecipientIDType();
            }
            public void setValue(java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Recipient_CT target = (Recipient_CT) object;
                    target.setRecipientIDType((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_appRefNum", "AppRefNum", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Recipient_CT target = (Recipient_CT) object;
                return target.getAppRefNum();
            }
            public void setValue(java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Recipient_CT target = (Recipient_CT) object;
                    target.setAppRefNum((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_appStatus", "AppStatus", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Recipient_CT target = (Recipient_CT) object;
                return target.getAppStatus();
            }
            public void setValue(java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Recipient_CT target = (Recipient_CT) object;
                    target.setAppStatus((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.util.Date.class, "_appStatusUpdateDate", "AppStatusUpdateDate", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                Recipient_CT target = (Recipient_CT) object;
                return target.getAppStatusUpdateDate();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Recipient_CT target = (Recipient_CT) object;
                    target.setAppStatusUpdateDate( (java.util.Date) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new java.util.Date();
            }
        };
        handler = new org.exolab.castor.xml.handlers.DateFieldHandler(handler);
        desc.setImmutable(true);
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_contactEmail", "ContactEmail", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Recipient_CT target = (Recipient_CT) object;
                return target.getContactEmail();
            }
            public void setValue(java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Recipient_CT target = (Recipient_CT) object;
                    target.setContactEmail((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_contactNum", "ContactNum", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Recipient_CT target = (Recipient_CT) object;
                return target.getContactNum();
            }
            public void setValue(java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Recipient_CT target = (Recipient_CT) object;
                    target.setContactNum((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_miscInfo", "MiscInfo", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Recipient_CT target = (Recipient_CT) object;
                return target.getMiscInfo();
            }
            public void setValue(java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Recipient_CT target = (Recipient_CT) object;
                    target.setMiscInfo((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }

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
        return hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient_CT.class;
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
