










package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;

  
 


import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;






public class MessageRequest_CTDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      
     
    

    


    private boolean elementDefinition;

    


    private java.lang.String nsPrefix;

    


    private java.lang.String nsURI;

    


    private java.lang.String xmlName;

    


    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      
     
    

    public MessageRequest_CTDescriptor() 
    {
        super();
        nsURI = "http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd";
        xmlName = "MessageRequest.CT";
        elementDefinition = false;
        
        
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        
        
        
        
        
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_portalID", "PortalID", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MessageRequest_CT target = (MessageRequest_CT) object;
                return target.getPortalID();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MessageRequest_CT target = (MessageRequest_CT) object;
                    target.setPortalID( (java.lang.String) value);
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

        
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.EMessage.class, "_EMessage", "EMessage", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MessageRequest_CT target = (MessageRequest_CT) object;
                return target.getEMessage();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MessageRequest_CT target = (MessageRequest_CT) object;
                    target.setEMessage( (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.EMessage) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.EMessage();
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

        
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem.class, "_toDoItem", "ToDoItem", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MessageRequest_CT target = (MessageRequest_CT) object;
                return target.getToDoItem();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MessageRequest_CT target = (MessageRequest_CT) object;
                    target.setToDoItem( (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem();
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


        
        
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application.class, "_application", "Application", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                MessageRequest_CT target = (MessageRequest_CT) object;
                return target.getApplication();
            }
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    MessageRequest_CT target = (MessageRequest_CT) object;
                    target.setApplication((hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { 
        }
        
        


        
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST.class, "_billAccountInfoInd", "BillAccountInfoInd", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MessageRequest_CT target = (MessageRequest_CT) object;
                return target.getBillAccountInfoInd();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MessageRequest_CT target = (MessageRequest_CT) object;
                    target.setBillAccountInfoInd( (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        };
        handler = new org.exolab.castor.xml.handlers.EnumFieldHandler(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST.class, handler);
        desc.setImmutable(true);
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { 
        }
        desc.setValidator(fieldValidator);

        
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData.class, "_metaDataList", "MetaData", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MessageRequest_CT target = (MessageRequest_CT) object;
                return target.getMetaData();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MessageRequest_CT target = (MessageRequest_CT) object;
                    target.addMetaData( (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData();
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
        return hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest_CT.class;
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
