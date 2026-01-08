package hk.gov.cmc.mapper.utils;

import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public final class XmlDateMapper {

    private static final DatatypeFactory FACTORY = createFactory();

    private XmlDateMapper() {
    }

    private static DatatypeFactory createFactory() {
        try {
            return DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static XMLGregorianCalendar toXml(Date value) {
        if (value == null) {
            return null;
        }
        return FACTORY.newXMLGregorianCalendar(
                value.toInstant().toString());
    }

    public static Date fromXml(XMLGregorianCalendar value) {
        if (value == null) {
            return null;
        }
        return Date.from(value.toGregorianCalendar().toInstant());
    }
}
