package hk.gov.cmc.persistence.connection.hpfw;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;

public class Parameter {
    public static final short String = 0;
    public static final short Integer = 1;
    public static final short Long = 2;
    public static final short BigDecimal = 3;
    public static final short Date = 4;
    public static final short Timestamp = 5;
    public static final short Clob = 6;
    public static final short Blob = 7;

    public static final String NULL_STRING = "<N>";
    public static java.sql.Date NULL_DATE = null;
    public static java.sql.Timestamp NULL_TIMESTAMP = null;
    public static final int NULL_INT = -1;
    public static final long NULL_LONG = -1;
    public static final java.math.BigDecimal NULL_BIGDECIMAL = new java.math.BigDecimal(-1);

    public short type;
    public Object value = null;
    public boolean isPK = false;

    static {
        Locale locale = new Locale("en", "US");
        Calendar c = Calendar.getInstance(locale);
        c.set(1900, 0, 1, 0, 0, 0);
        c.set(Calendar.YEAR, 1900);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        NULL_DATE = new java.sql.Date(0);
        NULL_DATE.setTime(c.getTimeInMillis());

        NULL_TIMESTAMP = new java.sql.Timestamp(0);
        NULL_TIMESTAMP.setTime(c.getTimeInMillis());
        locale = null;
    }

    public Parameter(short type, Object value, boolean isPK) {
        this.type = type;
        this.value = value;
        this.isPK = isPK;
    }

    public Parameter(short type, Object value) {
        this.type = type;
        this.value = value;
    }

    protected void finalize() throws Throwable {
        value = null;
    }

    public boolean isNullValue() {
        if (type == String)
            return ((String) value).equals(NULL_STRING);
        if (type == Date)
            return ((Date) value).getTime() == NULL_DATE.getTime();
        if (type == Timestamp)
            return ((Timestamp) value).getTime() == NULL_TIMESTAMP.getTime();
        if (type == BigDecimal)
            return ((BigDecimal) value).equals(NULL_BIGDECIMAL);
        if (type == Long)
            return ((Long) value).equals(NULL_LONG);
        if (type == Integer)
            return ((Integer) value).equals(NULL_INT);
        return false;
    }

    static public final short oracleTypeToShort(String data_type, int data_precision, int data_scale) {
        if (data_type.indexOf("CHAR") > -1 || data_type.equals("CLOB"))
            return String;
        if (data_type.equals("DATE") || data_type.indexOf("TIMESTAMP") > -1)
            return Timestamp;
        if (data_type.equals("NUMBER")) {
            if (data_scale > 0)
                return BigDecimal;
            if (data_precision >= 9)
                return Long;
            else
                return Integer;
        }
        if (data_type.equals("BLOB"))
            return Blob;
        else
            return String;
    }
}
