package hk.gov.cmc.dao.report.impl;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class IAS04ReportDAO {

    private static Log logger = LogFactory.getLog(IAS04ReportDAO.class);

    private static final String SQL_COUNT_EXIST_IAS_MESS_STAT_BY_DATE = "SELECT COUNT(1) AS totalCount FROM ias_message_stat WHERE stat_date = STR_TO_DATE(?, '%d/%m/%Y')";

    /**
     * Check record is exist in IasMessageStat table by request date (yyyymmdd)
     * 
     * @param hpfwConn
     * @param requestDateStr
     * @return
     * @throws Exception
     */
    public boolean recordExistInMessStatTable(HPFW_Connection hpfwConn, String requestDateStr) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("recordExistInMessStatTable() begin");

        if (logger.isDebugEnabled())
            logger.debug("requestDateStr[" + requestDateStr + "]");

        ArrayList<Parameter> paraList = new ArrayList<Parameter>();
        ResultSet rs = null;
        boolean result = false;
        try {
            paraList.add(new Parameter(Parameter.String, requestDateStr));
            rs = hpfwConn.getResultSet(SQL_COUNT_EXIST_IAS_MESS_STAT_BY_DATE, paraList);
            if (rs.next()) {
                result = rs.getInt("totalCount") > 0;
            }
            HPFW_Connection.close(rs);

            return result;
        } catch (Exception ex) {
            logger.error("General exception raised in recordExistInMessStatTable", ex);
            throw ex;
        } finally {
            if (rs != null)
                HPFW_Connection.close(rs);
            if (logger.isDebugEnabled())
                logger.debug("recordExistInMessStatTable() end");
        }
    }

    /**
     * Call store procedure to update IasMessageStat table by request date (yyyymmdd)
     * 
     * @param hpfwConn
     * @param requestDateStr
     * @throws Exception
     */
    public void updateMessStatTable(HPFW_Connection hpfwConn, String requestDateStr) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("updateMessStatTable() begin");

        if (logger.isDebugEnabled())
            logger.debug("requestDateStr[" + requestDateStr + "]");

        ResultSet rsMc = null;
        ResultSet rsTc = null;
        ResultSet rsAc = null;
        try {
            int read_ind_record_cnt;
            int sent_ind_record_cnt;
            String read_ind = "";
            String sp_id = "";
            String tmp_id = "";
            String tmp_ver = "";

            String temp_sp_id = "";
            String temp_tmp_id = "";
            String temp_tmp_ver = "";
            int temp_num_created = 0;
            int temp_num_created_read = 0;
            int temp_num_created_unread = 0;
            int temp_num_created_sent = 0;

            String mesg_count_cursor_sql = "SELECT IFNULL(A.read_ind_record_count, 0) as read_ind_record_count, IFNULL(A.read_ind,'R') as read_ind, B.service_provider_id, B.template_id, B.template_version, "
                    +
                    "IFNULL(C.sent_ind_record_count, 0) as sent_ind_record_count, IFNULL(C.ias_noti_status,'S') as ias_noti_status "
                    +
                    "FROM " +
                    "( " +
                    "	SELECT count(im.ias_msg_id) AS read_ind_record_count, A.read_ind, im.template_id, im.template_version FROM "
                    +
                    "	( " +
                    "		SELECT ias_msg_id, read_ind FROM ias_user_message " +
                    "		WHERE create_dt BETWEEN STR_TO_DATE(CONCAT(?, '00:00:00'), '%Y%m%d%H:%i:%s') AND STR_TO_DATE(CONCAT(?, '23:59:59'), '%Y%m%d%H:%i:%s') "
                    +
                    "	) A " +
                    "	INNER JOIN ias_message im ON (A.ias_msg_id = im.ias_msg_id) " +
                    "	GROUP BY A.read_ind, im.template_id, im.template_version " +
                    ") A " +
                    "RIGHT JOIN " +
                    "( " +
                    "	SELECT ct.service_provider_id, ct.template_id, ct.template_version " +
                    "	FROM cmc_template ct " +
                    "	INNER JOIN cmc_template_type ctt ON (ct.template_type = ctt.template_type AND ctt.type_ind = 'M') "
                    +
                    ") B ON (A.template_id = B.template_id AND A.template_version = B.template_version) " +
                    "LEFT join " +
                    "( " +
                    "	SELECT count(im.ias_msg_id) AS sent_ind_record_count, A.ias_noti_status, im.template_id, im.template_version FROM "
                    +
                    "	( " +
                    "		SELECT ias_msg_id, ias_noti_status FROM ias_user_message " +
                    "		WHERE create_dt BETWEEN STR_TO_DATE(CONCAT(?, '00:00:00'), '%Y%m%d%H:%i:%s') AND STR_TO_DATE(CONCAT(?, '23:59:59'), '%Y%m%d%H:%i:%s') AND ias_noti_status = 'S' "
                    +
                    "	) A " +
                    "	INNER JOIN ias_message im ON (A.ias_msg_id = im.ias_msg_id) " +
                    "	GROUP BY A.ias_noti_status, im.template_id, im.template_version " +
                    ") C ON (B.template_id = C.template_id AND B.template_version = C.template_version) " +
                    "ORDER BY B.service_provider_id, B.template_id, B.template_version, A.read_ind";

            String todo_count_cursor_sql = "SELECT IFNULL(A.read_ind_record_count, 0) as read_ind_record_count, IFNULL(A.read_ind,'R') as read_ind, B.service_provider_id, B.template_id, B.template_version, "
                    +
                    "IFNULL(C.sent_ind_record_count, 0) as sent_ind_record_count, IFNULL(C.ias_noti_status,'S') as ias_noti_status "
                    +
                    "FROM " +
                    "( " +
                    "	SELECT count(itdi.ias_to_do_item_id) AS read_ind_record_count, A.read_ind, itdi.template_id, itdi.template_version FROM "
                    +
                    "	( " +
                    "		SELECT ias_to_do_item_id, read_ind FROM ias_user_to_do_item " +
                    "		WHERE create_dt BETWEEN STR_TO_DATE(CONCAT(?, '00:00:00'), '%Y%m%d%H:%i:%s') AND STR_TO_DATE(CONCAT(?, '23:59:59'), '%Y%m%d%H:%i:%s') "
                    +
                    "	) A " +
                    "	INNER JOIN ias_to_do_item itdi ON (A.ias_to_do_item_id = itdi.ias_to_do_item_id) " +
                    "	GROUP BY A.read_ind, itdi.template_id, itdi.template_version " +
                    ") A " +
                    "RIGHT JOIN " +
                    "( " +
                    "	SELECT ct.service_provider_id, ct.template_id, ct.template_version " +
                    "	FROM cmc_template ct " +
                    "	INNER JOIN cmc_template_type ctt ON (ct.template_type = ctt.template_type AND ctt.type_ind = 'I') "
                    +
                    ") B ON (A.template_id = B.template_id AND A.template_version = B.template_version) " +
                    "LEFT join " +
                    "( " +
                    "	SELECT count(itdi.ias_to_do_item_id) AS sent_ind_record_count, A.ias_noti_status, itdi.template_id, itdi.template_version FROM "
                    +
                    "	( " +
                    "		SELECT ias_to_do_item_id, ias_noti_status FROM ias_user_to_do_item " +
                    "		WHERE create_dt BETWEEN STR_TO_DATE(CONCAT(?, '00:00:00'), '%Y%m%d%H:%i:%s') AND STR_TO_DATE(CONCAT(?, '23:59:59'), '%Y%m%d%H:%i:%s') AND ias_noti_status = 'S' "
                    +
                    "	) A " +
                    "	INNER JOIN ias_to_do_item itdi ON (A.ias_to_do_item_id = itdi.ias_to_do_item_id) " +
                    "	GROUP BY A.ias_noti_status, itdi.template_id, itdi.template_version " +
                    ") C ON (B.template_id = C.template_id AND B.template_version = C.template_version) " +
                    "ORDER BY B.service_provider_id, B.template_id, B.template_version, A.read_ind ";

            String application_count_cursor_sql = "SELECT IFNULL(A.record_count, 0) as record_count, B.service_provider_id, B.template_id, B.template_version, "
                    +
                    "IFNULL(C.sent_ind_record_count, 0) as sent_ind_record_count, IFNULL(C.ias_noti_status,'S') as ias_noti_status "
                    +
                    "FROM " +
                    "(  " +
                    "	SELECT count(ia.ias_application_id) AS record_count, ia.template_id, ia.template_version FROM  "
                    +
                    "	(  " +
                    "		SELECT ias_application_id FROM ias_user_application  " +
                    "		WHERE create_dt BETWEEN STR_TO_DATE(CONCAT(?, '00:00:00'), '%Y%m%d%H:%i:%s') AND STR_TO_DATE(CONCAT(?, '23:59:59'), '%Y%m%d%H:%i:%s')  "
                    +
                    "	) A  " +
                    "	INNER JOIN ias_application ia ON (A.ias_application_id = ia.ias_application_id)  " +
                    "	GROUP BY ia.template_id, ia.template_version  " +
                    ") A  " +
                    "RIGHT JOIN " +
                    "( " +
                    "	SELECT ct.service_provider_id, ct.template_id, ct.template_version " +
                    "	FROM cmc_template ct " +
                    "	INNER JOIN cmc_template_type ctt ON (ct.template_type = ctt.template_type AND ctt.type_ind = 'A') "
                    +
                    ") B ON (A.template_id = B.template_id AND A.template_version = B.template_version)  " +
                    "LEFT join " +
                    "( " +
                    "	SELECT count(ia.ias_application_id) AS sent_ind_record_count, A.ias_noti_status, ia.template_id, ia.template_version FROM "
                    +
                    "	( " +
                    "		SELECT ias_application_id, ias_noti_status FROM ias_user_application " +
                    "		WHERE create_dt BETWEEN STR_TO_DATE(CONCAT(?, '00:00:00'), '%Y%m%d%H:%i:%s') AND STR_TO_DATE(CONCAT(?, '23:59:59'), '%Y%m%d%H:%i:%s') AND ias_noti_status = 'S' "
                    +
                    "	) A " +
                    "	INNER JOIN ias_application ia ON (A.ias_application_id = ia.ias_application_id) " +
                    "	GROUP BY A.ias_noti_status, ia.template_id, ia.template_version " +
                    ") C ON (B.template_id = C.template_id AND B.template_version = C.template_version) " +
                    "ORDER BY B.service_provider_id, B.template_id, B.template_version, C.ias_noti_status ";

            // DELETE FROM ias_message_stat WHERE stat_date = requestDateStr;
            ArrayList<Parameter> paraT = new ArrayList<Parameter>();
            paraT.add(new Parameter(Parameter.String, requestDateStr));
            hpfwConn.executeStatement("DELETE FROM ias_message_stat WHERE stat_date = ?", paraT);

            // OPEN mesg_count_cursor;
            ArrayList<Parameter> paraMc = new ArrayList<Parameter>();
            paraMc.add(new Parameter(Parameter.String, requestDateStr));
            paraMc.add(new Parameter(Parameter.String, requestDateStr));
            paraMc.add(new Parameter(Parameter.String, requestDateStr));
            paraMc.add(new Parameter(Parameter.String, requestDateStr));

            rsMc = hpfwConn.getResultSet(mesg_count_cursor_sql, paraMc);

            int rowcount = 0;
            while (rsMc.next()) {
                rowcount++;
                // FETCH mesg_count_cursor INTO record_cnt, read_ind, sp_id, tmp_id, tmp_ver;
                read_ind_record_cnt = rsMc.getInt("read_ind_record_count");
                sent_ind_record_cnt = rsMc.getInt("sent_ind_record_count");
                read_ind = rsMc.getString("read_ind");
                sp_id = rsMc.getString("service_provider_id");
                tmp_id = rsMc.getString("template_id");
                tmp_ver = rsMc.getString("template_version");

                if (rowcount == 1) {
                    /* Put data to temp fields */
                    temp_sp_id = sp_id;
                    temp_tmp_id = tmp_id;
                    temp_tmp_ver = tmp_ver;
                    temp_num_created_read = 0;
                    temp_num_created_unread = 0;
                    temp_num_created_sent = 0;

                    if ("R".equals(read_ind)) {
                        temp_num_created_read = read_ind_record_cnt;
                        temp_num_created_sent = sent_ind_record_cnt;
                    } else if ("U".equals(read_ind)) {
                        temp_num_created_unread = read_ind_record_cnt;
                    }

                } else {
                    if ((sp_id.equals(temp_sp_id)) && (tmp_id.equals(temp_tmp_id)) && (tmp_ver.equals(temp_tmp_ver))) {
                        if ("R".equals(read_ind)) {
                            temp_num_created_read = read_ind_record_cnt;
                            temp_num_created_sent = sent_ind_record_cnt;
                        } else if ("U".equals(read_ind)) {
                            temp_num_created_unread = read_ind_record_cnt;
                        }
                    } else {
                        /* Insert record and clear the temp value */
                        temp_num_created = temp_num_created_read + temp_num_created_unread;

                        createIasMsgStat(hpfwConn, temp_sp_id, temp_tmp_id, temp_tmp_ver,
                                temp_num_created, temp_num_created_read, temp_num_created_unread, temp_num_created_sent,
                                requestDateStr);

                        temp_num_created_read = 0;
                        temp_num_created_unread = 0;

                        /* Put data to temp fields */
                        temp_sp_id = sp_id;
                        temp_tmp_id = tmp_id;
                        temp_tmp_ver = tmp_ver;

                        if ("R".equals(read_ind)) {
                            temp_num_created_read = read_ind_record_cnt;
                            temp_num_created_sent = sent_ind_record_cnt;
                        } else if ("U".equals(read_ind)) {
                            temp_num_created_unread = read_ind_record_cnt;
                        }
                    }
                }

            }

            temp_num_created = temp_num_created_read + temp_num_created_unread;

            createIasMsgStat(hpfwConn, temp_sp_id, temp_tmp_id, temp_tmp_ver,
                    temp_num_created, temp_num_created_read, temp_num_created_unread, temp_num_created_sent,
                    requestDateStr);

            hpfwConn.commit();

            temp_sp_id = null;
            temp_tmp_id = null;
            temp_tmp_ver = null;
            temp_num_created = 0;
            temp_num_created_read = 0;
            temp_num_created_unread = 0;

            // OPEN todo_count_cursor;
            ArrayList<Parameter> paraTc = new ArrayList<Parameter>();
            paraTc.add(new Parameter(Parameter.String, requestDateStr));
            paraTc.add(new Parameter(Parameter.String, requestDateStr));
            paraTc.add(new Parameter(Parameter.String, requestDateStr));
            paraTc.add(new Parameter(Parameter.String, requestDateStr));

            rsTc = hpfwConn.getResultSet(todo_count_cursor_sql, paraTc);

            int rowcountT = 0;
            while (rsTc.next()) {
                rowcountT++;
                // FETCH mesg_count_cursor INTO record_cnt, read_ind, sp_id, tmp_id, tmp_ver;
                read_ind_record_cnt = rsTc.getInt("read_ind_record_count");
                sent_ind_record_cnt = rsTc.getInt("sent_ind_record_count");
                read_ind = rsTc.getString("read_ind");
                sp_id = rsTc.getString("service_provider_id");
                tmp_id = rsTc.getString("template_id");
                tmp_ver = rsTc.getString("template_version");

                if (rowcountT == 1) {
                    /* Put data to temp fields */
                    temp_sp_id = sp_id;
                    temp_tmp_id = tmp_id;
                    temp_tmp_ver = tmp_ver;
                    temp_num_created_read = 0;
                    temp_num_created_unread = 0;
                    temp_num_created_sent = 0;

                    if ("R".equals(read_ind)) {
                        temp_num_created_read = read_ind_record_cnt;
                    } else if ("U".equals(read_ind)) {
                        temp_num_created_unread = read_ind_record_cnt;
                    }
                    temp_num_created_sent = sent_ind_record_cnt;

                } else {
                    if ((sp_id.equals(temp_sp_id)) && (tmp_id.equals(temp_tmp_id)) && (tmp_ver.equals(temp_tmp_ver))) {
                        if ("R".equals(read_ind)) {
                            temp_num_created_read = read_ind_record_cnt;
                        } else if ("U".equals(read_ind)) {
                            temp_num_created_unread = read_ind_record_cnt;
                        }
                        temp_num_created_sent = sent_ind_record_cnt;
                    } else {
                        /* Insert record and clear the temp value */
                        temp_num_created = temp_num_created_read + temp_num_created_unread;

                        createIasMsgStat(hpfwConn, temp_sp_id, temp_tmp_id, temp_tmp_ver,
                                temp_num_created, temp_num_created_read, temp_num_created_unread, temp_num_created_sent,
                                requestDateStr);

                        temp_num_created_read = 0;
                        temp_num_created_unread = 0;

                        /* Put data to temp fields */
                        temp_sp_id = sp_id;
                        temp_tmp_id = tmp_id;
                        temp_tmp_ver = tmp_ver;

                        if ("R".equals(read_ind)) {
                            temp_num_created_read = read_ind_record_cnt;
                        } else if ("U".equals(read_ind)) {
                            temp_num_created_unread = read_ind_record_cnt;
                        }
                        temp_num_created_sent = sent_ind_record_cnt;
                    }
                }

            }

            temp_num_created = temp_num_created_read + temp_num_created_unread;

            createIasMsgStat(hpfwConn, temp_sp_id, temp_tmp_id, temp_tmp_ver,
                    temp_num_created, temp_num_created_read, temp_num_created_unread, temp_num_created_sent,
                    requestDateStr);

            hpfwConn.commit();

            // OPEN todo_count_cursor;
            ArrayList<Parameter> paraAc = new ArrayList<Parameter>();
            paraAc.add(new Parameter(Parameter.String, requestDateStr));
            paraAc.add(new Parameter(Parameter.String, requestDateStr));
            paraAc.add(new Parameter(Parameter.String, requestDateStr));
            paraAc.add(new Parameter(Parameter.String, requestDateStr));

            rsAc = hpfwConn.getResultSet(application_count_cursor_sql, paraAc);

            int record_count = 0;
            while (rsAc.next()) {
                // FETCH mesg_count_cursor INTO record_cnt, sp_id, tmp_id, tmp_ver;
                sent_ind_record_cnt = rsAc.getInt("sent_ind_record_count");
                sp_id = rsAc.getString("service_provider_id");
                tmp_id = rsAc.getString("template_id");
                tmp_ver = rsAc.getString("template_version");
                record_count = rsAc.getInt("record_count");

                createIasMsgStat(hpfwConn, sp_id, tmp_id, tmp_ver,
                        record_count, 0, 0, sent_ind_record_cnt,
                        requestDateStr);
            }

            hpfwConn.commit();

            rsMc.close();
            rsMc = null;
            rsTc.close();
            rsTc = null;
            rsAc.close();
            rsAc = null;
        } catch (Exception ex) {
            logger.error("General exception raised in updateMessStatTable", ex);
            throw ex;
        } finally {
            if (rsMc != null) {
                rsMc.close();
            }
            if (logger.isDebugEnabled())
                logger.debug("updateMessStatTable() end");
        }
    }

    /**
     * Create IAS_MESSAGE_STAT record
     *
     * @param HPFW_Connection con
     * @param String          temp_sp_id
     * @param String          temp_tmp_id
     * @param String          temp_tmp_ver
     * @param int             temp_num_created
     * @param int             temp_num_created_read
     * @param int             temp_num_created_unread
     * @param String          para_exec_date
     */
    private void createIasMsgStat(HPFW_Connection con, String temp_sp_id, String temp_tmp_id, String temp_tmp_ver,
            int temp_num_created, int temp_num_created_read, int temp_num_created_unread, int temp_num_created_sent,
            String para_exec_date) throws Exception {

        String InsertSQL = "INSERT INTO ias_message_stat( " +
                "	service_provider_id, template_id, template_version, " +
                "	no_of_message_created, no_of_message_created_read, no_of_message_created_unread, no_of_message_sent, "
                + "	stat_date, create_dt, last_modify_dt, create_by, last_modify_by) " +
                "VALUES (?, ?, ?, " +
                "	?, ?, ?, ?, " +
                "	(STR_TO_DATE(?, '%Y%m%d')), now(3), now(3), 'SYSTEM', 'SYSTEM')";

        ArrayList<Parameter> paraI = new ArrayList<Parameter>();
        paraI.add(new Parameter(Parameter.String, temp_sp_id));
        paraI.add(new Parameter(Parameter.String, temp_tmp_id));
        paraI.add(new Parameter(Parameter.String, temp_tmp_ver));
        paraI.add(new Parameter(Parameter.Integer, temp_num_created));
        paraI.add(new Parameter(Parameter.Integer, temp_num_created_read));
        paraI.add(new Parameter(Parameter.Integer, temp_num_created_unread));
        paraI.add(new Parameter(Parameter.Integer, temp_num_created_sent));
        paraI.add(new Parameter(Parameter.String, para_exec_date));

        con.executeStatement(InsertSQL, paraI);
    }

}
