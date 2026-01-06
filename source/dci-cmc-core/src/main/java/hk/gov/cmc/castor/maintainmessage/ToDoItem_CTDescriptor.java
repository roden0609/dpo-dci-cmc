










package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;

  
 


import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;






public class ToDoItem_CTDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      
     
    

    


    private boolean elementDefinition;

    


    private java.lang.String nsPrefix;

    


    private java.lang.String nsURI;

    


    private java.lang.String xmlName;

    


    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      
     
    

    public ToDoItem_CTDescriptor() 
     {
        super();
        nsURI = "http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd";
        xmlName = "ToDoItem.CT";
        elementDefinition = false;
        
        
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        
        
        
        
        
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_templateID", "TemplateID", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ToDoItem_CT target = (ToDoItem_CT) object;
                return target.getTemplateID();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ToDoItem_CT target = (ToDoItem_CT) object;
                    target.setTemplateID( (java.lang.String) value);
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
        
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_templateVersion", "TemplateVersion", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ToDoItem_CT target = (ToDoItem_CT) object;
                return target.getTemplateVersion();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ToDoItem_CT target = (ToDoItem_CT) object;
                    target.setTemplateVersion( (java.lang.String) value);
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
        return hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem_CT.class;
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
