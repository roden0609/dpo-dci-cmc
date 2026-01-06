/*
 * Design, Implementation and Support of Common Middleware Components and Reference Applications 
 * 
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0</a>, using an XML
 * 
 * Developed by: Gilbert Liao
 * Reviewed by: Kai So
 * Tester: Shaohui Wang
 */

package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class MessageRequest_CTDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class MessageRequest_CTDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field elementDefinition
     */
    private boolean elementDefinition;

    /**
     * Field nsPrefix
     */
    private java.lang.String nsPrefix;

    /**
     * Field nsURI
     */
    private java.lang.String nsURI;

    /**
     * Field xmlName
     */
    private java.lang.String xmlName;

    /**
     * Field identity
     */
    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public MessageRequest_CTDescriptor() 
    {
        super();
        nsURI = "http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd";
        xmlName = "MessageRequest.CT";
        elementDefinition = false;
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _portalID
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
        
        //-- validation code for: _portalID
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        //-- _EMessage
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
        
        //-- validation code for: _EMessage
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);

        //-- _toDoItem
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
        
        //-- validation code for: _toDoItem
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);


        // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
        // -- _application
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
        
        //-- validation code for: _application
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        // desc.setValidator(fieldValidator); // TODO: update GCIS SCD to release this function in RPC call
        // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END


        //-- _billAccountInfoInd
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
        
        //-- validation code for: _billAccountInfoInd
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);

        //-- _metaDataList
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
        
        //-- validation code for: _metaDataList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest_CTDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode
     * 
     * 
     * 
     * @return AccessMode
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode()
    {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
     * Method getExtends
     * 
     * 
     * 
     * @return ClassDescriptor
     */
    public org.exolab.castor.mapping.ClassDescriptor getExtends()
    {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
     * Method getIdentity
     * 
     * 
     * 
     * @return FieldDescriptor
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity()
    {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
     * Method getJavaClass
     * 
     * 
     * 
     * @return Class
     */
    public java.lang.Class getJavaClass()
    {
        return hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest_CT.class;
    } //-- java.lang.Class getJavaClass() 

    /**
     * Method getNameSpacePrefix
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getNameSpacePrefix()
    {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
     * Method getNameSpaceURI
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getNameSpaceURI()
    {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
     * Method getValidator
     * 
     * 
     * 
     * @return TypeValidator
     */
    public org.exolab.castor.xml.TypeValidator getValidator()
    {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
     * Method getXMLName
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getXMLName()
    {
        return xmlName;
    } //-- java.lang.String getXMLName() 

    /**
     * Method isElementDefinition
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isElementDefinition()
    {
        return elementDefinition;
    } //-- boolean isElementDefinition() 

}
