










package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;

  
 


import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;






public class MaintainMessageRequestDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      
     
    

    


    private boolean elementDefinition;

    


    private java.lang.String nsPrefix;

    


    private java.lang.String nsURI;

    


    private java.lang.String xmlName;

    


    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      
     
    

    public MaintainMessageRequestDescriptor() 
     {
        super();
        nsURI = "http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd";
        xmlName = "MaintainMessageRequest";
        elementDefinition = false;
        
        
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        
        
        
        
        
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest.class, "_messageRequestList", "MessageRequest", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MaintainMessageRequest target = (MaintainMessageRequest) object;
                return target.getMessageRequest();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MaintainMessageRequest target = (MaintainMessageRequest) object;
                    target.addMessageRequest( (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest();
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
        return hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageRequest.class;
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
