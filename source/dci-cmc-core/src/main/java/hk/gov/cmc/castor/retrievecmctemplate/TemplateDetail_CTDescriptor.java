
package hk.gov.cmc.castor.retrievecmctemplate;

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

public class TemplateDetail_CTDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    private boolean elementDefinition;

    private java.lang.String nsPrefix;

    private java.lang.String nsURI;

    private java.lang.String xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor identity;

    public TemplateDetail_CTDescriptor()
    {
        super();
        nsURI = "http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd";
        xmlName = "TemplateDetail.CT";
        elementDefinition = false;

        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_subjectEn", "SubjectEn", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getSubjectEn();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setSubjectEn( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
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

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_subjectTc", "SubjectTc", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getSubjectTc();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setSubjectTc( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
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

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_subjectSc", "SubjectSc", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getSubjectSc();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setSubjectSc( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
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

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_contentEn", "ContentEn", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getContentEn();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setContentEn( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
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

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_contentTc", "ContentTc", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getContentTc();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setContentTc( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
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

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_contentSc", "ContentSc", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getContentSc();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setContentSc( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
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

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_emailSubjectEn", "EmailSubjectEn", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getEmailSubjectEn();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setEmailSubjectEn( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_emailSubjectTc", "EmailSubjectTc", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getEmailSubjectTc();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setEmailSubjectTc( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_emailSubjectSc", "EmailSubjectSc", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getEmailSubjectSc();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setEmailSubjectSc( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_emailContentEn", "EmailContentEn", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getEmailContentEn();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setEmailContentEn( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_emailContentTc", "EmailContentTc", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getEmailContentTc();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setEmailContentTc( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_emailContentSc", "EmailContentSc", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getEmailContentSc();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setEmailContentSc( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_mobileSubjectEn", "MobileSubjectEn", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getMobileSubjectEn();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setMobileSubjectEn( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_mobileSubjectTc", "MobileSubjectTc", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getMobileSubjectTc();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setMobileSubjectTc( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_mobileSubjectSc", "MobileSubjectSc", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getMobileSubjectSc();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setMobileSubjectSc( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_mobileContentEn", "MobileContentEn", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getMobileContentEn();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setMobileContentEn( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_mobileContentTc", "MobileContentTc", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getMobileContentTc();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setMobileContentTc( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_mobileContentSc", "MobileContentSc", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object )
            throws IllegalStateException
            {
                TemplateDetail_CT target = (TemplateDetail_CT) object;
                return target.getMobileContentSc();
            }
            public void setValue( java.lang.Object object, java.lang.Object value)
            throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TemplateDetail_CT target = (TemplateDetail_CT) object;
                    target.setMobileContentSc( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplate.xsd");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        fieldValidator = new org.exolab.castor.xml.FieldValidator();
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
        return hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.TemplateDetail_CT.class;
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
