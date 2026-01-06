






package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.descriptors;

  
 


import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageResponse;






public class MaintainMessageResponseDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    


    private boolean _elementDefinition;

    


    private java.lang.String _nsPrefix;

    


    private java.lang.String _nsURI;

    


    private java.lang.String _xmlName;

    


    private org.exolab.castor.xml.XMLFieldDescriptor _identity;

    public MaintainMessageResponseDescriptor() {
        super();
        _nsURI = "http://ws.mygovhk.gov.hk/schema/MaintainMessageResponse.xsd";
        _xmlName = "MaintainMessageResponse";
        _elementDefinition = false;

        
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        

        

        
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "resultCode", "ResultCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MaintainMessageResponse target = (MaintainMessageResponse) object;
                return target.getResultCode();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MaintainMessageResponse target = (MaintainMessageResponse) object;
                    target.setResultCode( (java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageResponse.xsd");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { 
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "resultMessage", "ResultMessage", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MaintainMessageResponse target = (MaintainMessageResponse) object;
                return target.getResultMessage();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MaintainMessageResponse target = (MaintainMessageResponse) object;
                    target.setResultMessage( (java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageResponse.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { 
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse.class, "messageResponseList", "MessageResponse", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MaintainMessageResponse target = (MaintainMessageResponse) object;
                return target.getMessageResponse();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MaintainMessageResponse target = (MaintainMessageResponse) object;
                    target.addMessageResponse( (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    MaintainMessageResponse target = (MaintainMessageResponse) object;
                    target.removeAllMessageResponse();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse();
            }
        };
        desc.setSchemaType("list");
        desc.setComponentType("hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageResponse.xsd");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { 
        }
        desc.setValidator(fieldValidator);
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
        return hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageResponse.class;
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
