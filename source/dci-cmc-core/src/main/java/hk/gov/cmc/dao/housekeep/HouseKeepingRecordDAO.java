package hk.gov.cmc.dao.housekeep;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.model.maintainmessage.application.IasUserApplication;
import hk.gov.cmc.model.maintainmessage.emessage.IasUserMsg;
import hk.gov.cmc.model.maintainmessage.todoitem.IasUserToDoItem;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;

public class HouseKeepingRecordDAO {

    private static final Log logger = LogFactory.getLog(HouseKeepingRecordDAO.class);
    private static final String NEWLINE = "\n";

    public int updateIasUserMessageByBatch(HPFW_Connection hpfwConn,
            int iasMsgHousekeepRetentionMonths, int batchSize) throws Exception {

        String updateSqlStr = "update IAS_USER_MESSAGE set HOUSEKEEP_IND='Y' " +
                " where CREATE_DT < DATE(now()) + INTERVAL ? MONTH " +
                " and (HOUSEKEEP_IND !='Y' or HOUSEKEEP_IND is null) " +
                " LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(updateSqlStr);
            ps.setInt(1, -iasMsgHousekeepRetentionMonths);
            ps.setInt(2, batchSize);
            int row = ps.executeUpdate();

            return row;

        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }

    }

    public int updateIasUserToDoItemByBatch(HPFW_Connection hpfwConn,
            int iasMsgHousekeepRetentionMonths, int batchSize) throws Exception {

        String updateSqlStr = "update IAS_USER_TO_DO_ITEM set HOUSEKEEP_IND='Y' " +
                " where CREATE_DT < DATE(now()) + INTERVAL ? MONTH " +
                " and (HOUSEKEEP_IND !='Y' or HOUSEKEEP_IND is null) " +
                " LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(updateSqlStr);
            ps.setInt(1, -iasMsgHousekeepRetentionMonths);
            ps.setInt(2, batchSize);
            int row = ps.executeUpdate();

            return row;

        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }

    }

    public int updateIasUserApplicationByBatch(HPFW_Connection hpfwConn,
            int iasMsgHousekeepRetentionMonths, int batchSize) throws Exception {

        String updateSqlStr = "update IAS_USER_APPLICATION set HOUSEKEEP_IND='Y' " +
                " where CREATE_DT < DATE(now()) + INTERVAL ? MONTH " +
                " and (HOUSEKEEP_IND !='Y' or HOUSEKEEP_IND is null) " +
                " LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(updateSqlStr);
            ps.setInt(1, -iasMsgHousekeepRetentionMonths);
            ps.setInt(2, batchSize);
            int row = ps.executeUpdate();

            return row;

        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }

    }

    public List<IasUserMsg> getIasMsgIdFromIasUserMessage(HPFW_Connection hpfwConn, int startIndex,
            int endIndex,
            String inputDate) throws Exception {
        List<IasUserMsg> keyList = new ArrayList<IasUserMsg>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(GET_IAS_MSG_ID_FROM_IAS_USER_MESSAGE);
            ps.setString(1, inputDate);
            ps.setInt(2, startIndex - 1);
            ps.setInt(3, endIndex - (startIndex - 1));
            rs = ps.executeQuery();
            while (rs.next()) {
                IasUserMsg iasUserMsg = new IasUserMsg();
                iasUserMsg.setIasMsgId(rs.getString("ias_msg_id"));
                keyList.add(iasUserMsg);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
        return keyList;
    }

    public List<IasUserMsg> getDelIasMsgIDFromIasUserMessage(HPFW_Connection hpfwConn, int startIndex,
            int endIndex, int iasMsgHousekeepRetentionMonths) throws Exception {
        List<IasUserMsg> keyList = new ArrayList<IasUserMsg>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(GET_DEL_IAS_MSG_ID_FROM_IAS_USER_MESSAGE);
            ps.setInt(1, -iasMsgHousekeepRetentionMonths);
            ps.setInt(2, startIndex - 1);
            ps.setInt(3, endIndex - (startIndex - 1));
            rs = ps.executeQuery();
            while (rs.next()) {
                IasUserMsg iasUserMsg = new IasUserMsg();
                iasUserMsg.setIasMsgId(rs.getString("ias_msg_id"));
                keyList.add(iasUserMsg);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
        return keyList;
    }

    public List<IasUserToDoItem> getDelIasToDoItemIDFromIasUserToDoItem(HPFW_Connection hpfwConn, int startIndex,
            int endIndex, int iasMsgHousekeepRetentionMonths) throws Exception {
        List<IasUserToDoItem> keyList = new ArrayList<IasUserToDoItem>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(GET_DEL_IAS_TO_DO_ITEM_ID_FROM_IAS_USER_TO_DO_ITEM);
            ps.setInt(1, -iasMsgHousekeepRetentionMonths);
            ps.setInt(2, startIndex - 1);
            ps.setInt(3, endIndex - (startIndex - 1));
            rs = ps.executeQuery();
            while (rs.next()) {
                IasUserToDoItem iasUserToDoItem = new IasUserToDoItem();
                iasUserToDoItem.setIasToDoItemId(rs.getString("ias_to_do_item_id"));
                keyList.add(iasUserToDoItem);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
        return keyList;
    }

    public List<IasUserApplication> getDelIasApplicationIDFromIasUserApplication(HPFW_Connection hpfwConn,
            int startIndex,
            int endIndex, int iasMsgHousekeepRetentionMonths) throws Exception {
        List<IasUserApplication> keyList = new ArrayList<IasUserApplication>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(GET_DEL_IAS_APPLICATION_ID_FROM_IAS_USER_APPLICATION);
            ps.setInt(1, -iasMsgHousekeepRetentionMonths);
            ps.setInt(2, startIndex - 1);
            ps.setInt(3, endIndex - (startIndex - 1));
            rs = ps.executeQuery();
            while (rs.next()) {
                IasUserApplication iasUserApplication = new IasUserApplication();
                iasUserApplication.setIasApplicationId(rs.getString("ias_application_id"));
                keyList.add(iasUserApplication);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
        return keyList;
    }

    public List<Object> archiveIasMessageCsvLobAndBroadcastLob(HPFW_Connection hpfwConn, String msgId, String path,
            int thresoldCsv, int batchSize, String inputDate, int currCsvIncCnt, int currCsvFileIdx,
            FileWriter csvWriter, String archiveDateStr, String csvMsgCreateDt, int thresoldLob, int currLobIncCnt,
            int currLobFileIdx, FileWriter lobWriter, String lobMsgCreateDt, int currBLobIncCnt, int currBLobFileIdx,
            FileWriter bLobWriter, String bLobMsgCreateDt) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        String csvFileName = "";
        String csvPath = path;
        String lobFileName = "";
        String lobPath = path;
        String bLobfileName = "";
        String bLobPath = path;
        boolean isBroadcastLobArchived = false;

        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf4 = new SimpleDateFormat("HH:mm:ss");

        SimpleDateFormat sdf5 = new SimpleDateFormat("yyyyMM");
        String fileFirstDate = "";
        List<Object> returnResult = new ArrayList<Object>();

        try {

            boolean recordExist = true;
            int startIndex = 1;
            int endIndex = batchSize;

            Connection conn = hpfwConn.getConnectionPtr();
            while (recordExist) {
                recordExist = false;
                ps = conn.prepareStatement(GET_FIELDS_FOR_ARCHIVE_IAS_MESSAGE);
                ps.setString(1, inputDate);
                ps.setString(2, msgId);
                ps.setInt(3, startIndex - 1);
                ps.setInt(4, endIndex - (startIndex - 1));
                rs = ps.executeQuery();

                while (rs.next()) {
                    recordExist = true;

                    String client_id = rs.getString("client_id");
                    String open_id = rs.getString("open_id");
                    String noti_id = checkNull(rs.getString("noti_id"));
                    String service_provider_id = rs.getString("service_provider_id");
                    Date create_dt = rs.getTimestamp("create_dt");
                    String ias_msg_id = rs.getString("ias_msg_id");

                    String user_message_id = checkNull(rs.getString("user_message_id"));
                    String tran_id = rs.getString("tran_id");
                    String read_ind = rs.getString("read_ind");
                    String ias_noti_status = rs.getString("ias_noti_status");
                    String tx_id = checkNull(rs.getString("tx_id"));
                    String ias_delivery_status = checkNull(rs.getString("ias_delivery_status"));

                    String subject_en = checkNull(rs.getString("subject_en"));
                    String subject_tc = checkNull(rs.getString("subject_tc"));
                    String subject_sc = checkNull(rs.getString("subject_sc"));
                    String portal_id = checkNull(rs.getString("portal_id"));
                    String ref_id = checkNull(rs.getString("ref_id"));

                    String broadcast_ind = checkNull(rs.getString("broadcast_ind"));

                    String content_en = checkNull(rs.getString("content_en"));
                    String content_tc = checkNull(rs.getString("content_tc"));
                    String content_sc = checkNull(rs.getString("content_sc"));
                    String ias_es_app_suffix_en = checkNull(rs.getString("ias_es_app_suffix_en"));
                    String ias_es_app_suffix_tc = checkNull(rs.getString("ias_es_app_suffix_tc"));
                    String ias_es_app_suffix_sc = checkNull(rs.getString("ias_es_app_suffix_sc"));

                    if (StringUtils.isEmpty(fileFirstDate)) {
                        fileFirstDate = sdf5.format(rs.getTimestamp("create_dt"));

                        {
                            csvPath = csvPath + "/" + fileFirstDate + "/" + archiveDateStr;
                            File filePath = new File(csvPath);
                            if (!filePath.exists()) {
                                filePath.mkdirs();
                            }
                            if (!csvMsgCreateDt.equals(fileFirstDate)) {

                                if (csvWriter != null) {
                                    csvWriter.close();
                                }
                                currCsvIncCnt = 0;
                                currCsvFileIdx = 1;
                                csvFileName = csvPath + "/" + "IAS" + "_" + fileFirstDate + "."
                                        + String.format("%03d", currCsvFileIdx);
                                csvWriter = new FileWriter(csvFileName);
                                csvMsgCreateDt = fileFirstDate;
                            }
                            if (csvWriter == null) {
                                csvFileName = csvPath + "/" + "IAS" + "_" + fileFirstDate + "."
                                        + String.format("%03d", currCsvFileIdx);
                                csvWriter = new FileWriter(csvFileName);
                            }
                        }

                        if ("N".equalsIgnoreCase(broadcast_ind)) {
                            lobPath = lobPath + "/" + fileFirstDate + "/" + archiveDateStr;
                            File filePath = new File(lobPath);
                            if (!filePath.exists()) {
                                filePath.mkdirs();
                            }
                            if (!lobMsgCreateDt.equals(fileFirstDate)) {

                                if (lobWriter != null) {
                                    lobWriter.close();
                                }
                                currLobIncCnt = 0;
                                currLobFileIdx = 1;
                                lobFileName = lobPath + "/" + "IAS" + "_" + fileFirstDate + "_lob" + "."
                                        + String.format("%03d", currLobFileIdx);
                                lobWriter = new FileWriter(lobFileName);
                                lobMsgCreateDt = fileFirstDate;
                            }
                            if (lobWriter == null) {
                                lobFileName = lobPath + "/" + "IAS" + "_" + fileFirstDate + "_lob" + "."
                                        + String.format("%03d", currLobFileIdx);
                                lobWriter = new FileWriter(lobFileName);
                            }
                        } else if ("Y".equalsIgnoreCase(broadcast_ind) && !isBroadcastLobArchived) {
                            bLobPath = bLobPath + "/" + fileFirstDate + "/" + "broadcast" + "/" + archiveDateStr;
                            File filePath = new File(bLobPath);
                            if (!filePath.exists()) {
                                filePath.mkdirs();
                            }
                            if (!bLobMsgCreateDt.equals(fileFirstDate)) {

                                if (bLobWriter != null) {
                                    bLobWriter.close();
                                }
                                currBLobIncCnt = 0;
                                currBLobFileIdx = 1;
                                bLobfileName = bLobPath + "/" + "IAS" + "_" + fileFirstDate + "_lob" + "."
                                        + String.format("%03d", currBLobFileIdx);
                                bLobWriter = new FileWriter(bLobfileName);
                                bLobMsgCreateDt = fileFirstDate;
                            }
                            if (bLobWriter == null) {
                                bLobfileName = bLobPath + "/" + "IAS" + "_" + fileFirstDate + "_lob" + "."
                                        + String.format("%03d", currBLobFileIdx);
                                bLobWriter = new FileWriter(bLobfileName);
                            }
                        }
                    }

                    {
                        if (currCsvIncCnt > 0 && (currCsvIncCnt % thresoldCsv == 0)) {
                            if (csvWriter != null) {
                                csvWriter.close();
                            }
                            currCsvFileIdx++;
                            csvFileName = csvPath + "/" + "IAS" + "_" + fileFirstDate + "."
                                    + String.format("%03d", currCsvFileIdx);
                            csvWriter = new FileWriter(csvFileName);
                        }

                        csvWriter.append(client_id + ",");
                        csvWriter.append(open_id + ",");
                        csvWriter.append(noti_id + ",");
                        csvWriter.append(service_provider_id + ",");
                        csvWriter.append(sdf3.format(create_dt) + ",");
                        csvWriter.append(sdf4.format(create_dt) + ",");
                        csvWriter.append(ias_msg_id + ",");
                        csvWriter.append(ref_id + ",");
                        csvWriter.append(user_message_id + ",");
                        csvWriter.append(StringEscapeUtils.escapeCsv(subject_en) + ",");
                        csvWriter.append(broadcast_ind + ",");
                        csvWriter.append(read_ind + ",");
                        csvWriter.append(ias_noti_status + ",");
                        csvWriter.append(tx_id + ",");

                        csvWriter.append(ias_delivery_status + ",");

                        csvWriter.append(NEWLINE);
                        currCsvIncCnt++;

                    }

                    if ("N".equalsIgnoreCase(broadcast_ind)) {
                        if (currLobIncCnt > 0 && currLobIncCnt % thresoldLob == 0) {
                            if (lobWriter != null) {
                                lobWriter.close();
                            }
                            currLobFileIdx++;
                            lobFileName = lobPath + "/" + "IAS" + "_" + fileFirstDate + "_lob" + "."
                                    + String.format("%03d", currLobFileIdx);
                            lobWriter = new FileWriter(lobFileName);
                        }

                        lobWriter.append("Client ID:" + client_id + NEWLINE);
                        lobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        lobWriter.append("Open ID:" + open_id + NEWLINE);
                        lobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        lobWriter.append("Noti ID:" + noti_id + NEWLINE);
                        lobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        lobWriter.append("Service Provider ID:" + service_provider_id + NEWLINE);
                        lobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        lobWriter.append("Creation Date:" + sdf3.format(create_dt) + NEWLINE);
                        lobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        lobWriter.append("Creation Time:" + sdf4.format(create_dt) + NEWLINE);
                        lobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        lobWriter.append("Ias Msg ID:" + ias_msg_id + NEWLINE);
                        lobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        lobWriter.append("Subject (English):" + subject_en + NEWLINE);
                        lobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        lobWriter.append("Content (English):" + content_en + NEWLINE);
                        lobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        lobWriter.append("Subject (Trad. Chinese):" + subject_tc + NEWLINE);
                        lobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        lobWriter.append("Content (Trad. Chinese):" + content_tc + NEWLINE);
                        lobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        lobWriter.append("Subject (Simp. Chinese):" + subject_sc + NEWLINE);
                        lobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        lobWriter.append("Content (Simp. Chinese):" + content_sc + NEWLINE);
                        lobWriter.append("<<END_OF_RECORD>>" + NEWLINE);

                        lobWriter.append("App Link suffix (English):" + ias_es_app_suffix_en + NEWLINE);
                        lobWriter.append("<<END_OF_RECORD>>" + NEWLINE);

                        lobWriter.append("App Link suffix (Trad. Chinese):" + ias_es_app_suffix_tc + NEWLINE);
                        lobWriter.append("<<END_OF_RECORD>>" + NEWLINE);

                        lobWriter.append("App Link suffix (Simp. Chinese):" + ias_es_app_suffix_sc + NEWLINE);
                        lobWriter.append("<<END_OF_RECORD>>" + NEWLINE);

                        lobWriter.append(NEWLINE);
                        currLobIncCnt++;

                    } else if ("Y".equalsIgnoreCase(broadcast_ind) && !isBroadcastLobArchived) {
                        if (currBLobIncCnt > 0 && currBLobIncCnt % thresoldLob == 0) {
                            if (bLobWriter != null) {
                                bLobWriter.close();
                            }
                            currBLobFileIdx++;
                            bLobfileName = bLobPath + "/" + "IAS" + "_" + fileFirstDate + "_lob" + "."
                                    + String.format("%03d", currBLobFileIdx);
                            bLobWriter = new FileWriter(bLobfileName);
                        }

                        bLobWriter.append("Client ID:" + client_id + NEWLINE);
                        bLobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        bLobWriter.append("Open ID:" + open_id + NEWLINE);
                        bLobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        bLobWriter.append("Noti ID:" + noti_id + NEWLINE);
                        bLobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        bLobWriter.append("Service Provider ID:" + service_provider_id + NEWLINE);
                        bLobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        bLobWriter.append("Creation Date:" + sdf3.format(create_dt) + NEWLINE);
                        bLobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        bLobWriter.append("Creation Time:" + sdf4.format(create_dt) + NEWLINE);
                        bLobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        bLobWriter.append("Ias Msg ID:" + ias_msg_id + NEWLINE);
                        bLobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        bLobWriter.append("Subject (English):" + subject_en + NEWLINE);
                        bLobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        bLobWriter.append("Content (English):" + content_en + NEWLINE);
                        bLobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        bLobWriter.append("Subject (Trad. Chinese):" + subject_tc + NEWLINE);
                        bLobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        bLobWriter.append("Content (Trad. Chinese):" + content_tc + NEWLINE);
                        bLobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        bLobWriter.append("Subject (Simp. Chinese):" + subject_sc + NEWLINE);
                        bLobWriter.append("<<END_OF_FIELD>>" + NEWLINE);

                        bLobWriter.append("Content (Simp. Chinese):" + content_sc + NEWLINE);
                        bLobWriter.append("<<END_OF_RECORD>>" + NEWLINE);

                        lobWriter.append("App Link suffix (English):" + ias_es_app_suffix_en + NEWLINE);
                        lobWriter.append("<<END_OF_RECORD>>" + NEWLINE);

                        lobWriter.append("App Link suffix (Trad. Chinese):" + ias_es_app_suffix_tc + NEWLINE);
                        lobWriter.append("<<END_OF_RECORD>>" + NEWLINE);

                        lobWriter.append("App Link suffix (Simp. Chinese):" + ias_es_app_suffix_sc + NEWLINE);
                        lobWriter.append("<<END_OF_RECORD>>" + NEWLINE);

                        bLobWriter.append(NEWLINE);
                        currBLobIncCnt++;
                        isBroadcastLobArchived = true;

                    }
                }
                startIndex = startIndex + batchSize;
                endIndex = endIndex + batchSize;
                close(ps, rs);
            }
            returnResult.add(currCsvIncCnt);
            returnResult.add(currCsvFileIdx);
            returnResult.add(csvWriter);
            returnResult.add(csvMsgCreateDt);

            returnResult.add(currLobIncCnt);
            returnResult.add(currLobFileIdx);
            returnResult.add(lobWriter);
            returnResult.add(lobMsgCreateDt);

            returnResult.add(currBLobIncCnt);
            returnResult.add(currBLobFileIdx);
            returnResult.add(bLobWriter);
            returnResult.add(bLobMsgCreateDt);

        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
        return returnResult;
    }

    public int updateIasUserMessageHouseKeepInd(HPFW_Connection hpfwConn, String iasMsgId, String inputDate,
            int batchSize) throws Exception {
        String updateSqlStr = "update IAS_USER_MESSAGE set HOUSEKEEP_IND='X' " +
                " where CREATE_DT < STR_TO_DATE(?,'%Y-%m-%d') " +
                " and IAS_MSG_ID=? " +
                " and HOUSEKEEP_IND ='Y'  " +
                " LIMIT ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(updateSqlStr);
            ps.setString(1, inputDate);
            ps.setString(2, iasMsgId);
            ps.setInt(3, batchSize);
            int row = ps.executeUpdate();
            return row;

        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public void insertIasMessageArchiveIndex(HPFW_Connection hpfwConn,
            String iasMsgId,
            int batchSize,
            String inputDate) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        PreparedStatement selectPs = null;
        ResultSet selectRs = null;

        PreparedStatement updateArchIndxPs = null;
        PreparedStatement insertArchIndxPs = null;

        try {
            boolean recordExist = true;
            int startIndex = 1;
            int endIndex = batchSize;

            Connection conn = hpfwConn.getConnectionPtr();

            updateArchIndxPs = conn.prepareStatement(UPDATE_IAS_ARCHIVE_INDEX);
            insertArchIndxPs = conn.prepareStatement(INSERT_IAS_ARCHIVE_INDEX);

            while (recordExist) {
                recordExist = false;
                ps = conn.prepareStatement(GET_FIELDS_FOR_ARCHIVE_IAS_MESSAGE);
                ps.setString(1, inputDate);
                ps.setString(2, iasMsgId);
                ps.setInt(3, startIndex - 1);
                ps.setInt(4, endIndex - (startIndex - 1));
                rs = ps.executeQuery();

                while (rs.next()) {
                    recordExist = true;
                    String noti_id = rs.getString("noti_id");
                    String sp = rs.getString("service_provider_id");
                    Date create_dt = rs.getTimestamp("create_dt");

                    if (noti_id != null) {

                        boolean updateMode = false;
                        selectPs = conn.prepareStatement(SELECT_IAS_ARCHIVE_INDEX);
                        selectPs.setString(1, noti_id);
                        selectPs.setString(2, sp);
                        selectRs = selectPs.executeQuery();

                        if (selectRs.next()) {
                            updateMode = true;
                        }

                        if (updateMode) {
                            updateArchIndxPs.setTimestamp(1, new Timestamp(create_dt.getTime()));
                            updateArchIndxPs.setString(2, noti_id);
                            updateArchIndxPs.setString(3, sp);
                            updateArchIndxPs.addBatch();
                        } else {

                            insertArchIndxPs.setString(1, noti_id);
                            insertArchIndxPs.setString(2, sp);
                            insertArchIndxPs.setTimestamp(3, new Timestamp(create_dt.getTime()));
                            insertArchIndxPs.setTimestamp(4, new Timestamp(create_dt.getTime()));
                            insertArchIndxPs.addBatch();
                        }
                        close(selectPs, selectRs);
                    }
                }

                int[] insertCnt = insertArchIndxPs.executeBatch();
                int[] updateCnt = updateArchIndxPs.executeBatch();

                startIndex = startIndex + batchSize;
                endIndex = endIndex + batchSize;
                close(ps, rs);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
            close(selectPs, selectRs);
            close(insertArchIndxPs, null);
            close(updateArchIndxPs, null);
        }
    }

    public int deleteIasMessageByIasMsgId(HPFW_Connection hpfwConn, String iasMsgId) throws Exception {
        String deleteSqlStr = "Delete from IAS_MESSAGE where ias_msg_id=? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setString(1, iasMsgId);
            int row = ps.executeUpdate();
            return row;
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteIasToDoItemByIasToDoItemId(HPFW_Connection hpfwConn, String iasToDoItemId) throws Exception {
        String deleteSqlStr = "Delete from IAS_TO_DO_ITEM where ias_to_do_item_id=? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setString(1, iasToDoItemId);
            int row = ps.executeUpdate();
            return row;
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteIasApplicationByIasApplicationId(HPFW_Connection hpfwConn, String iasApplicationId)
            throws Exception {
        String deleteSqlStr = "Delete from IAS_APPLICATION where ias_application_id=? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setString(1, iasApplicationId);
            int row = ps.executeUpdate();
            return row;
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteIasUserMessageByIasMsgId(HPFW_Connection hpfwConn, String iasMsgId, int batchSize)
            throws Exception {
        String deleteSqlStr = "Delete from IAS_USER_MESSAGE where " +
                " ias_msg_id=? LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setString(1, iasMsgId);
            ps.setInt(2, batchSize);
            int row = ps.executeUpdate();
            return row;
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteIasUserToDoItemByIasToDoItemId(HPFW_Connection hpfwConn, String iasToDoItemId, int batchSize)
            throws Exception {
        String deleteSqlStr = "Delete from IAS_USER_TO_DO_ITEM where " +
                " ias_to_do_item_id=? LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setString(1, iasToDoItemId);
            ps.setInt(2, batchSize);
            int row = ps.executeUpdate();
            return row;
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteIasUserApplicationByIasApplicationId(HPFW_Connection hpfwConn, String iasApplicationId,
            int batchSize) throws Exception {
        String deleteSqlStr = "Delete from IAS_USER_APPLICATION where " +
                " ias_application_id=? LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setString(1, iasApplicationId);
            ps.setInt(2, batchSize);
            int row = ps.executeUpdate();
            return row;
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteCmcAsynMessage(HPFW_Connection hpfwConn, int delNonUserTblMonthLimit, String inputDate,
            int batchSize) throws Exception {
        String deleteSqlStr = "Delete from CMC_ASYN_MESSAGE where " +
                " CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) and status='S' LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setString(1, inputDate);
            ps.setInt(2, -delNonUserTblMonthLimit);
            ps.setInt(3, batchSize);
            int row = ps.executeUpdate();
            return row;

        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteCmcAsynMessageH(HPFW_Connection hpfwConn, int delNonUserTblMonthLimit, String inputDate,
            int batchSize) throws Exception {
        String deleteSqlStr = "Delete from CMC_ASYN_MESSAGE_H where " +
                " msg_id in (select msg_id from CMC_ASYN_MESSAGE where CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) and status='S') "
                +
                " LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setString(1, inputDate);
            ps.setInt(2, -delNonUserTblMonthLimit);
            ps.setInt(3, batchSize);
            int row = ps.executeUpdate();
            return row;
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteCmcAsynRspCtrl(HPFW_Connection hpfwConn, int delNonUserTblMonthLimit, String inputDate,
            int batchSize) throws Exception {
        String deleteSqlStr = "Delete from CMC_ASYN_RSP_CTRL where " +
                " CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) and (status='F' or status='A') LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setString(1, inputDate);
            ps.setInt(2, -delNonUserTblMonthLimit);
            ps.setInt(3, batchSize);
            int row = ps.executeUpdate();
            return row;

        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteCmcAsynRspCtrlH(HPFW_Connection hpfwConn, int delNonUserTblMonthLimit, String inputDate,
            int batchSize) throws Exception {
        String deleteSqlStr = "Delete from CMC_ASYN_RSP_CTRL_H where " +
                " corr_id in (select corr_id from CMC_ASYN_RSP_CTRL where CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) and (status='F' or status='A')) "
                +
                " LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setString(1, inputDate);
            ps.setInt(2, -delNonUserTblMonthLimit);
            ps.setInt(3, batchSize);
            int row = ps.executeUpdate();
            return row;
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteCmcEgisNotiStatus(HPFW_Connection hpfwConn, int delNonUserTblMonthLimit, String inputDate,
            int batchSize) throws Exception {
        String deleteSqlStr = "Delete from CMC_EGIS_NOTI_STATUS where " +
                " CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) and (status='C' or status='F') LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setString(1, inputDate);
            ps.setInt(2, -delNonUserTblMonthLimit);
            ps.setInt(3, batchSize);
            int row = ps.executeUpdate();
            return row;

        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteCmcEgisNotiStatusH(HPFW_Connection hpfwConn, int delNonUserTblMonthLimit, String inputDate,
            int batchSize) throws Exception {
        String deleteSqlStr = "Delete from CMC_EGIS_NOTI_STATUS_H where " +
                " noti_job_id in (" +
                "	select noti_job_id from CMC_EGIS_NOTI_STATUS where CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) "
                +
                "	and (status='C' or status='F')" +
                ") " +
                " LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setString(1, inputDate);
            ps.setInt(2, -delNonUserTblMonthLimit);
            ps.setInt(3, batchSize);
            int row = ps.executeUpdate();
            return row;
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteCmcArchiveIndex(HPFW_Connection hpfwConn, int delArchIdxTblMonthLimit, String inputDate,
            int batchSize) throws Exception {
        String deleteSqlStr = "Delete from CMC_ARCHIVE_INDEX where " +
                " LAST_ARCHIVE_REC_CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setString(1, inputDate);
            ps.setInt(2, -delArchIdxTblMonthLimit);
            ps.setInt(3, batchSize);
            int row = ps.executeUpdate();
            return row;

        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteIasMsgStatusQueue(HPFW_Connection hpfwConn, int iasMsgHousekeepRetentionMonths, int batchSize)
            throws Exception {
        String deleteSqlStr = "Delete from IAS_MSG_STATUS_QUEUE where " +
                " CREATE_DT < DATE(now()) + INTERVAL ? MONTH and JOB_STATUS='C' LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setInt(1, -iasMsgHousekeepRetentionMonths);
            ps.setInt(2, batchSize);
            int row = ps.executeUpdate();
            return row;

        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteIasAssoQueue(HPFW_Connection hpfwConn, int iasMsgHousekeepRetentionMonths, int batchSize)
            throws Exception {
        String deleteSqlStr = "Delete from IAS_ASSO_QUEUE where " +
                " CREATE_DT < DATE(now()) + INTERVAL ? MONTH and JOB_STATUS='C' LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setInt(1, -iasMsgHousekeepRetentionMonths);
            ps.setInt(2, batchSize);
            int row = ps.executeUpdate();
            return row;

        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public int deleteIasUserMessageHByCreateDate(HPFW_Connection hpfwConn, int iasMsgHousekeepRetentionMonths,
            int batchSize) throws Exception {
        String deleteSqlStr = "Delete from IAS_USER_MESSAGE_H where " +
                " CREATE_DT < DATE(now()) + INTERVAL ? MONTH LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setInt(1, -iasMsgHousekeepRetentionMonths);
            ps.setInt(2, batchSize);
            int row = ps.executeUpdate();
            return row;
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }

    }

    public int deleteIasUserToDoItemHByCreateDate(HPFW_Connection hpfwConn, int iasMsgHousekeepRetentionMonths,
            int batchSize) throws Exception {
        String deleteSqlStr = "Delete from IAS_USER_TO_DO_ITEM_H where " +
                " CREATE_DT < DATE(now()) + INTERVAL ? MONTH LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setInt(1, -iasMsgHousekeepRetentionMonths);
            ps.setInt(2, batchSize);
            int row = ps.executeUpdate();
            return row;
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }

    }

    public int deleteIasUserApplicationHByCreateDate(HPFW_Connection hpfwConn, int iasMsgHousekeepRetentionMonths,
            int batchSize) throws Exception {
        String deleteSqlStr = "Delete from IAS_USER_APPLICATION_H where " +
                " CREATE_DT < DATE(now()) + INTERVAL ? MONTH LIMIT ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(deleteSqlStr);
            ps.setInt(1, -iasMsgHousekeepRetentionMonths);
            ps.setInt(2, batchSize);
            int row = ps.executeUpdate();
            return row;
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }

    }

    private void close(Statement stmt, ResultSet rs) {
        if (rs != null)
            try {
                rs.close();
            } catch (Exception ignored) {
            }
        if (stmt != null)
            try {
                stmt.close();
            } catch (Exception ignored) {
            }
    }

    private String checkNull(String data) {

        if (data == null) {
            return "";
        } else {
            return data;
        }

    }

    private static final String GET_IAS_MSG_ID_FROM_IAS_USER_MESSAGE = "Select " +
            "distinct ium.ias_msg_id,ium.CREATE_DT " +
            " from " +
            "ias_user_message ium " +
            " where " +
            "ium.CREATE_DT < STR_TO_DATE(?,'%Y-%m-%d')" +
            " and " +
            "ium.housekeep_ind = 'Y'" +
            " order by " +
            "ium.CREATE_DT,ium.ias_msg_id" +
            " LIMIT ?, ? ";

    private static final String GET_DEL_IAS_TO_DO_ITEM_ID_FROM_IAS_USER_TO_DO_ITEM = "Select " +
            "distinct iutdi.ias_to_do_item_id" +
            " from " +
            "ias_user_to_do_item iutdi" +
            " where " +
            "iutdi.CREATE_DT < DATE(now()) + INTERVAL ? MONTH " +
            " and " +
            "iutdi.housekeep_ind = 'Y'" +
            " LIMIT ?, ? ";

    private static final String GET_IAS_APPLICATION_ID_FROM_IAS_USER_APPLICATION = "Select " +
            "distinct iua.ias_application_id,iua.CREATE_DT " +
            " from " +
            "ias_user_application iua " +
            " where " +
            "iua.CREATE_DT < STR_TO_DATE(?,'%Y-%m-%d')" +
            " and " +
            "iua.housekeep_ind = 'Y'" +
            " order by " +
            "iua.CREATE_DT,iua.ias_application_id" +
            " LIMIT ?, ? ";

    private static final String GET_DEL_IAS_APPLICATION_ID_FROM_IAS_USER_APPLICATION = "Select " +
            "distinct iua.ias_application_id" +
            " from " +
            "ias_user_application iua" +
            " where " +
            "iua.CREATE_DT < DATE(now()) + INTERVAL ? MONTH " +
            " and " +
            "iua.housekeep_ind = 'Y'" +
            " LIMIT ?, ? ";

    private static final String GET_DEL_MSG_ID_FROM_CMC_USER_MESSAGE = "Select " +
            "distinct cum.message_id" +
            " from " +
            "cmc_user_message cum" +
            " where " +
            "cum.CREATE_DT < STR_TO_DATE(?,'%Y-%m-%d') " +
            " and " +
            "cum.housekeep_ind = 'X'" +
            " LIMIT ?, ? ";

    private static final String GET_DEL_IAS_MSG_ID_FROM_IAS_USER_MESSAGE = "Select " +
            "distinct ium.ias_msg_id" +
            " from " +
            "ias_user_message ium" +
            " where " +
            "ium.CREATE_DT < DATE(now()) + INTERVAL ? MONTH " +
            " and " +
            "ium.housekeep_ind = 'Y'" +
            " LIMIT ?, ? ";

    private static final String GET_MY_ID_KEY_FROM_CMC_USER_TO_DO_ITEM = " Select " +
            "distinct cutdi.to_do_item_id,cutdi.CREATE_DT" +
            " from " +
            "cmc_user_to_do_item cutdi" +
            " where " +
            "cutdi.CREATE_DT < STR_TO_DATE(?,'%Y-%m-%d')" +
            " and " +
            "cutdi.housekeep_ind = 'Y'" +
            " order by " +
            "cutdi.CREATE_DT,cutdi.to_do_item_id" +
            " LIMIT ?, ? ";

    private static final String GET_DEL_TODO_ID_FROM_CMC_USER_TO_DO_ITEM = " Select " +
            "distinct cutdi.to_do_item_id" +
            " from " +
            "cmc_user_to_do_item cutdi" +
            " where " +
            "cutdi.CREATE_DT < STR_TO_DATE (?,'%Y-%m-%d')" +
            " and " +
            "cutdi.housekeep_ind = 'X'" +
            " LIMIT ?, ? ";
    private static final String GET_MY_ID_KEY_FROM_CMC_USER_PAYMENT_TRAN = "Select " +
            "distinct cupt.TRAN_ID,cupt.CREATE_DT" +
            " from " +
            "cmc_user_payment_tran cupt" +
            " where " +
            "cupt.TRAN_DATE < STR_TO_DATE (?,'%Y-%m-%d') " +
            " and " +
            "cupt.housekeep_ind='Y'" +
            " and " +
            "cupt.IS_MYGOVHK='Y'" +
            " and " +
            "cupt.PAYMENT_STATUS='APPR'" +
            " order by " +
            "cupt.CREATE_DT,cupt.TRAN_ID" +
            " LIMIT ?, ? ";

    private static final String GET_DEL_TRAN_ID_FROM_CMC_USER_PAYMENT_TRAN = "Select " +
            "distinct cupt.TRAN_ID" +
            " from " +
            "cmc_user_payment_tran cupt" +
            " where " +
            "cupt.TRAN_DATE < STR_TO_DATE (?,'%Y-%m-%d') " +
            " and " +
            "(cupt.housekeep_ind='X' or cupt.housekeep_ind='Y')" +
            " LIMIT ?, ? ";

    private static final String GET_MY_ID_KEY_FROM_CMC_USER_EMAIL = "Select " +
            "distinct cu.my_id_key as my_id_key,cue.email_id" +
            " from " +
            "cmc_user_email cue ,cmc_user cu" +
            " where " +
            "cue.user_id = cu.user_id" +
            " and " +
            "cue.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cue.housekeep_ind = 'Y'" +
            " order by " +
            "cu.my_id_key" +
            " LIMIT ?, ? ";

    private static final String GET_EMAIL_ID_FROM_CMC_USER_EMAIL = "Select " +
            "distinct cue.email_id,cue.CREATE_DT" +
            " from " +
            "cmc_user_email cue" +
            " where " +
            "cue.CREATE_DT < STR_TO_DATE (?,'%Y-%m-%d')" +
            " and " +
            "cue.housekeep_ind = 'Y'" +
            " order by " +
            "cue.CREATE_DT,cue.email_id" +
            " LIMIT ?, ? ";

    private static final String GET_DEL_EMAIL_ID_FROM_CMC_USER_EMAIL = "Select " +
            "distinct cue.email_id" +
            " from " +
            "cmc_user_email cue" +
            " where " +
            "cue.CREATE_DT < STR_TO_DATE (?,'%Y-%m-%d') " +
            " and " +
            "(cue.housekeep_ind = 'X' or cue.housekeep_ind = 'Y')" +
            " LIMIT ?, ? ";

    private static final String GET_EMAIL_ID_FROM_CMC_USER_EMAIL_BK = "Select " +
            "distinct cue.email_id,cue.CREATE_DT" +
            " from " +
            "cmc_user_email_bk cue" +
            " where " +
            "cue.CREATE_DT < STR_TO_DATE (?,'%Y-%m-%d')" +
            " and " +
            "cue.housekeep_ind = 'Y'" +
            " order by " +
            "cue.CREATE_DT,cue.email_id" +
            " LIMIT ?, ? ";

    private static final String GET_DEL_EMAIL_ID_FROM_CMC_USER_EMAIL_BK = "Select " +
            "distinct cue.email_id" +
            " from " +
            "cmc_user_email_bk cue" +
            " where " +
            "cue.CREATE_DT < STR_TO_DATE (?,'%Y-%m-%d') " +
            " and " +
            "(cue.housekeep_ind = 'X' or cue.housekeep_ind = 'Y')" +
            " LIMIT ?, ? ";

    private static final String GET_MSG_ID_FROM_CMC_USER_MOBILE_MESSAGE = "Select " +
            "distinct cumm.mobile_msg_id,cumm.CREATE_DT" +
            " from " +
            "cmc_user_mobile_msg cumm" +
            " where " +
            "cumm.CREATE_DT < STR_TO_DATE (?,'%Y-%m-%d')" +
            " and " +
            "cumm.housekeep_ind = 'Y'" +
            " order by " +
            "cumm.CREATE_DT,cumm.mobile_msg_id" +
            " LIMIT ?, ? ";

    private static final String GET_DEL_MMSG_ID_FROM_CMC_USER_MOBILE_MESSAGE = "Select " +
            "distinct cumm.mobile_msg_id" +
            " from " +
            "cmc_user_mobile_msg cumm" +
            " where " +
            "cumm.CREATE_DT < STR_TO_DATE (?,'%Y-%m-%d')" +
            " and " +
            "cumm.housekeep_ind = 'X'" +
            " LIMIT ?, ? ";

    private static final String GET_MY_ID_KEY_FROM_CMC_USER_MOBILE_MESSAGE_BK = "Select " +
            "distinct cumm.mobile_msg_id,cumm.CREATE_DT" +
            " from " +
            "cmc_user_mobile_msg_bk cumm" +
            " where " +
            "cumm.CREATE_DT < STR_TO_DATE (?,'%Y-%m-%d')" +
            " and " +
            "cumm.housekeep_ind = 'Y'" +
            " order by " +
            "cumm.CREATE_DT,cumm.mobile_msg_id" +
            " LIMIT ?, ? ";

    private static final String GET_DEL_MMSG_ID_FROM_CMC_USER_MOBILE_MESSAGE_BK = "Select " +
            "distinct cumm.mobile_msg_id" +
            " from " +
            "cmc_user_mobile_msg_bk cumm" +
            " where " +
            "cumm.CREATE_DT < STR_TO_DATE (?,'%Y-%m-%d') " +
            " and " +
            "cumm.housekeep_ind = 'X'" +
            " LIMIT ?, ? ";

    private static final String GET_MESSAGE_INFO_FOR_DEL = "Select " +
            "distinct cum.user_id,cum.message_id" +
            " from " +
            "cmc_user_message cum" +
            " where " +
            "cum.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cum.housekeep_ind='Y'" +
            " order by " +
            "cum.user_id,cum.message_id " +
            " LIMIT ?, ? ";

    private static final String GET_EMAIL_INFO_FOR_DEL = "Select " +
            "distinct cue.user_id as user_id,cue.email_id" +
            " from " +
            "cmc_user_email cue ,cmc_user cu" +
            " where " +
            "cue.user_id = cu.user_id" +
            " and " +
            "cue.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cue.housekeep_ind ='Y'" +
            " order by " +
            "cue.user_id,cue.email_id" +
            " LIMIT ?, ? ";

    private static final String GET_EMAIL_DAYEND_INFO_FOR_DEL = "Select " +
            "distinct cue.email_id as email_id" +
            " from " +
            "CMC_USER_EMAIL cue, CMC_EMAIL ce" +
            " where " +
            "cue.EMAIL_ID = ce.EMAIL_ID" +
            " and " +
            "ce.ref_id like 'D%'" +
            " and " +
            "cue.CREATE_DT < STR_TO_DATE (?,'%Y-%m-%d')" +
            " LIMIT ?, ? ";

    private static final String GET_EMAIL_INFO_BK_FOR_DEL = "Select " +
            "distinct cue.user_id as user_id,cue.email_id" +
            " from " +
            "cmc_user_email_bk cue ,cmc_user cu" +
            " where " +
            "cue.user_id = cu.user_id" +
            " and " +
            "cue.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cue.housekeep_ind = 'Y'" +
            " order by " +
            "cue.user_id, cue.email_id" +
            " LIMIT ?, ? ";

    private static final String GET_EMAIL_DAYEND_BK_INFO_FOR_DEL = "Select " +
            "distinct cue.email_id as email_id" +
            " from " +
            "CMC_USER_EMAIL_BK cue, CMC_EMAIL_BK ce" +
            " where " +
            "cue.EMAIL_ID = ce.EMAIL_ID" +
            " and " +
            "ce.ref_id like 'D%'" +
            " and " +
            "cue.CREATE_DT < STR_TO_DATE (?,'%Y-%m-%d')" +
            " LIMIT ?, ? ";

    private static final String GET_MOBILE_MESSAGE_INFO_FOR_DEL = "Select " +
            "cumm.user_id as user_id,cumm.mobile_msg_id as mobile_msg_id" +
            " from " +
            "CMC_USER_MOBILE_MSG cumm, cmc_user cu" +
            " where " +
            "cumm.USER_ID = cu.USER_ID" +
            " and " +
            "cumm.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cumm.housekeep_ind='Y'" +
            " order by " +
            "cumm.user_id, cumm.mobile_msg_id" +
            " LIMIT ?, ? ";

    private static final String GET_MOBILE_MESSAGE_BK_INFO_FOR_DEL = "Select " +
            "cumm.user_id as user_id,cumm.mobile_msg_id as mobile_msg_id" +
            " from " +
            "CMC_USER_MOBILE_MSG_BK cumm, cmc_user cu" +
            " where " +
            "cumm.USER_ID = cu.USER_ID" +
            " and " +
            "cumm.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cumm.housekeep_ind='Y'" +
            " order by " +
            "cumm.user_id, cumm.mobile_msg_id" +
            " LIMIT ?, ? ";

    private static final String GET_REMINDER_INFO_FOR_DEL = "Select " +
            "cr.reminder_id as reminder_id" +
            " from " +
            "CMC_USER_MESSAGE cum, CMC_REMINDER cr" +
            " where " +
            "cum.user_id = cr.user_id";

    private static final String GET_MESSAGE_FOLDER_INFO_FOR_DEL = "Select " +
            "folder_id as folder_id" +
            " from " +
            "CMC_MESSAGE_FOLDER" +
            " where " +
            "housekeep_ind='Y'" +
            " and " +
            "LAST_MODIFY_DT < STR_TO_DATE (?,'%Y-%m-%d') " +
            " order by " +
            "folder_id" +
            " LIMIT ?, ? ";

    private static final String GET_CMC_REMINDER_INFO_FOR_DEL = "Select " +
            "distinct cr.reminder_id as reminder_id" +
            " from " +
            "CMC_REMINDER cr, CMC_USER_TO_DO_ITEM cutdi" +
            " where " +
            "cutdi.TO_DO_ITEM_ID = cr.TO_DO_ITEM_ID" +
            " and " +
            "cutdi.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cutdi.housekeep_ind='Y'" +
            " order by " +
            "cr.reminder_id" +
            " LIMIT ?, ? ";

    private static final String GET_TO_DO_ITEM_INFO_FOR_DEL = "Select " +
            "distinct cutdi.user_id as user_id , cutdi.to_do_item_id as to_do_item_id" +
            " from " +
            "CMC_USER_TO_DO_ITEM cutdi" +
            " where " +
            "cutdi.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cutdi.housekeep_ind='Y'" +
            " order by " +
            "cutdi.user_id, cutdi.to_do_item_id" +
            " LIMIT ?, ? ";

    private static final String GET_ACC_BALANCE_INFO_FOR_DEL = "Select " +
            "distinct user_id as user_id, account_balance_id as account_balance_id" +
            " from " +
            "CMC_USER_ACC_BALANCE" +
            " where " +
            "ISSUE_DATE < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "housekeep_ind='Y'" +
            " order by " +
            "user_id, account_balance_id" +
            " LIMIT ?, ? ";

    private static final String GET_PAYMENT_TRAN_INFO_FOR_DEL = "Select " +
            "tran_id as tran_id" +
            " from " +
            "CMC_USER_PAYMENT_TRAN" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "housekeep_ind='Y'" +
            " order by " +
            "tran_id" +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_EMAIL_CSV = "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, ce.create_dt as create_dt, ce.email_id as email_id,"
            +
            "ce.subject_en as subject_en, ce.subject_tc as subject_tc, ce.subject_sc as subject_sc, cue.lang_pref as lang_pref,"
            +
            "cue.status as status, ce.email_type as email_type, ce.ref_id as ref_id, cue.CREATE_DT as file_dt," +
            "CASE WHEN exists(Select * from CMC_USER_EMAIL cue2 where cue.email_id=cue2.email_id having count(*)=1) THEN 'N' ELSE 'Y' END AS broadcast_ind"
            +
            " from " +
            "CMC_USER cu, CMC_USER_EMAIL cue, CMC_TEMPLATE ct, CMC_EMAIL ce, CMC_MESSAGE cm, CMC_SERVICE_PROVIDER sp" +
            " where " +
            "cu.USER_ID = cue.USER_ID" +
            " and " +
            "cue.EMAIL_ID = ce.EMAIL_ID" +
            " and " +
            "ce.REF_ID = cm.MESSAGE_ID " +
            " and " +
            "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            " and " +
            "cue.housekeep_ind='Y'" +
            " and " +
            "cue.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "ce.ref_id not like 'D%'" +
            " and " +
            "cue.EMAIL_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,ce.create_dt,cm.MESSAGE_ID " +
            " LIMIT ?, ? ";

    private static final String GET_FIELDS_FOR_ARCHIVE_EMAIL = "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, ce.create_dt as create_dt, ce.email_id as email_id,"
            +
            "ce.subject_en as subject_en, ce.subject_tc as subject_tc, ce.subject_sc as subject_sc, cue.lang_pref as lang_pref,"
            +
            "cue.status as status, ce.email_type as email_type, ce.ref_id as ref_id, cue.CREATE_DT as file_dt," +
            "cue.broadcast_ind  AS broadcast_ind," +
            "ce.content_en as content_en, ce.content_tc as content_tc,ce.content_sc as content_sc" +
            " from " +
            "CMC_USER cu, CMC_USER_EMAIL cue, CMC_TEMPLATE ct, CMC_EMAIL ce, CMC_MESSAGE cm, CMC_SERVICE_PROVIDER sp" +
            " where " +
            "cu.USER_ID = cue.USER_ID" +
            " and " +
            "cue.EMAIL_ID = ce.EMAIL_ID" +
            " and " +
            "ce.REF_ID = cm.MESSAGE_ID " +
            " and " +
            "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            " and " +
            "cue.housekeep_ind='Y'" +
            " and " +
            "cue.CREATE_DT < STR_TO_DATE(?,'%Y-%m-%d')" +
            " and " +
            "ce.ref_id not like 'D%'" +
            " and " +
            "cue.EMAIL_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,ce.create_dt,cm.MESSAGE_ID " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_EMAIL_BK_CSV = "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, ce.create_dt as create_dt, ce.email_id as email_id,"
            +
            "ce.subject_en as subject_en, ce.subject_tc as subject_tc, ce.subject_sc as subject_sc, cue.lang_pref as lang_pref,"
            +
            "cue.status as status, ce.email_type as email_type, ce.ref_id as ref_id, cue.CREATE_DT as file_dt," +
            "CASE WHEN exists(Select * from CMC_USER_EMAIL_BK cue2 where cue.email_id=cue2.email_id having count(*)=1) THEN 'N' ELSE 'Y' END AS broadcast_ind"
            +
            " from " +
            "CMC_USER cu, CMC_USER_EMAIL_BK cue, CMC_TEMPLATE ct, CMC_EMAIL_BK ce, CMC_MESSAGE cm, CMC_SERVICE_PROVIDER sp"
            +
            " where " +
            "cu.USER_ID = cue.USER_ID" +
            " and " +
            "cue.EMAIL_ID = ce.EMAIL_ID" +
            " and " +
            "cm.MESSAGE_ID = ce.REF_ID" +
            " and " +
            "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            " and " +
            "cue.housekeep_ind='Y'" +
            " and " +
            "cue.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "ce.ref_id not like 'D%'" +
            " and " +
            "cue.EMAIL_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,ce.create_dt,cm.MESSAGE_ID " +
            " LIMIT ?, ? ";

    private static final String GET_FIELDS_FOR_ARCHIVE_EMAIL_BK = "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, ce.create_dt as create_dt, ce.email_id as email_id,"
            +
            "ce.subject_en as subject_en, ce.subject_tc as subject_tc, ce.subject_sc as subject_sc, cue.lang_pref as lang_pref,"
            +
            "cue.status as status, ce.email_type as email_type, ce.ref_id as ref_id, cue.CREATE_DT as file_dt," +
            "CASE WHEN exists(Select * from CMC_USER_EMAIL_BK cue2 where cue.email_id=cue2.email_id having count(*)=1) THEN 'N' ELSE 'Y' END AS broadcast_ind,"
            +
            "ce.content_en as content_en, ce.content_tc as content_tc, ce.content_sc as content_sc" +
            " from " +
            "CMC_USER cu, CMC_USER_EMAIL_BK cue, CMC_TEMPLATE ct, CMC_EMAIL_BK ce, CMC_MESSAGE cm, CMC_SERVICE_PROVIDER sp"
            +
            " where " +
            "cu.USER_ID = cue.USER_ID" +
            " and " +
            "cue.EMAIL_ID = ce.EMAIL_ID" +
            " and " +
            "cm.MESSAGE_ID = ce.REF_ID" +
            " and " +
            "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            " and " +
            "cue.housekeep_ind='Y'" +
            " and " +
            "cue.CREATE_DT < STR_TO_DATE(?,'%Y-%m-%d')" +
            " and " +
            "ce.ref_id not like 'D%'" +
            " and " +
            "cue.EMAIL_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,ce.create_dt,cm.MESSAGE_ID " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_EMAIL_LOB = "select rt.* from (" +
            "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, ce.create_dt as create_dt, ce.email_id as email_id,"
            +
            "ce.subject_en as subject_en, ce.content_en as content_en," +
            "ce.subject_tc as subject_tc, ce.content_tc as content_tc," +
            "ce.subject_sc as subject_sc, ce.content_sc as content_sc," +
            "cue.CREATE_DT as file_dt" +
            " from " +
            "CMC_USER cu, CMC_USER_EMAIL cue, CMC_TEMPLATE ct, CMC_EMAIL ce, CMC_MESSAGE cm,CMC_SERVICE_PROVIDER sp" +
            " where " +
            "cu.USER_ID = cue.USER_ID" +
            " and " +
            "cue.EMAIL_ID = ce.EMAIL_ID" +
            " and " +
            "ce.REF_ID = cm.MESSAGE_ID " +
            " and " +
            "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            " and " +
            "cue.housekeep_ind='Y'" +
            " and " +
            "cue.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "ce.ref_id not like 'D%'" +
            " and " +
            "cue.EMAIL_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,ce.create_dt,cm.MESSAGE_ID " +
            ") rt inner join (" +
            "select email_id,count(email_id) cnt from CMC_USER_EMAIL where email_id=? group by email_id " +
            ") ct on rt.email_id=ct.email_id and ct.cnt=1 " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_EMAIL_BK_LOB = "select rt.* from (" +
            "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, ce.create_dt as create_dt, ce.email_id as email_id,"
            +
            "ce.subject_en as subject_en, ce.content_en as content_en," +
            "ce.subject_tc as subject_tc, ce.content_tc as content_tc," +
            "ce.subject_sc as subject_sc, ce.content_sc as content_sc," +
            "cue.CREATE_DT as file_dt" +
            " from " +
            "CMC_USER cu, CMC_USER_EMAIL_BK cue, CMC_TEMPLATE ct, CMC_EMAIL_BK ce, CMC_MESSAGE cm, CMC_SERVICE_PROVIDER sp"
            +
            " where " +
            "cu.USER_ID = cue.USER_ID" +
            " and " +
            "cue.EMAIL_ID = ce.EMAIL_ID" +
            " and " +
            "ce.REF_ID = cm.MESSAGE_ID " +
            " and " +
            "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            " and " +
            "cue.housekeep_ind='Y'" +
            " and " +
            "cue.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "ce.ref_id not like 'D%'" +
            " and " +
            "cue.EMAIL_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,ce.create_dt,cm.MESSAGE_ID " +
            ")rt inner join (" +
            "select email_id,count(email_id) cnt from CMC_USER_EMAIL_BK where email_id=? group by email_id " +
            ") ct on rt.email_id=ct.email_id and ct.cnt=1 " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_MY_MESSAGE_CSV = "Select "
            + "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, cum.CREATE_DT as create_dt, cm.message_id as message_id,"
            + "cm.subject_en as subject_en, cm.portal_id as portal_id , cum.read_ind as read_ind, cmf.folder_name as folder_name,"
            + "cm.email_ind as email_ind, cm.mobile_msg_ind as mobile_msg_ind, cum.email_status as email_status, cum.mobile_status as mobile_status,"
            + "CASE WHEN exists(Select * from CMC_USER_MESSAGE cum2 where cum.message_id=cum2.message_id having count(*)=1) THEN 'N' ELSE 'Y' END AS broadcast_ind"
            + " from "
            + "CMC_USER_MESSAGE cum, CMC_MESSAGE cm, CMC_USER cu, CMC_TEMPLATE ct, CMC_SERVICE_PROVIDER sp, CMC_MESSAGE_FOLDER cmf"
            + " where "
            + "cum.MESSAGE_ID = cm.MESSAGE_ID"
            + " and "
            + "cum.USER_ID = cu.USER_ID"
            + " and "
            + "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION"
            + " and "
            + "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID"
            + " and "
            + "cum.user_id = cmf.user_id and cum.folder_id = cmf.folder_id"
            + " and "
            + "cum.housekeep_ind='Y'"
            + " and "
            + "cum.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) "
            + " and "
            + "cum.MESSAGE_ID=?"
            + " order by "
            + "cu.my_id_key,sp.service_provider_id,cm.create_dt,cm.MESSAGE_ID " +
            " LIMIT ?, ? ";

    private static final String GET_FIELDS_FOR_ARCHIVE_MY_MESSAGE = "Select "
            + "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, cum.CREATE_DT as create_dt, cm.message_id as message_id,"
            + "cm.subject_en as subject_en,cm.subject_tc as subject_tc,cm.subject_sc as subject_sc, cm.portal_id as portal_id , cum.read_ind as read_ind, cmf.folder_name as folder_name,"
            + "cm.email_ind as email_ind, cm.mobile_msg_ind as mobile_msg_ind, cum.email_status as email_status, cum.mobile_status as mobile_status,"
            + "cum.broadcast_ind AS broadcast_ind,"
            + "cm.enc_ind AS enc_ind,"
            + "cm.enc_key_store_id AS enc_key_store_id,"
            + "cm.content_en as content_en,cm.content_tc as content_tc,cm.content_sc as content_sc"
            + " from "
            + "CMC_USER_MESSAGE cum, CMC_MESSAGE cm, CMC_USER cu, CMC_TEMPLATE ct, CMC_SERVICE_PROVIDER sp, CMC_MESSAGE_FOLDER cmf"
            + " where "
            + "cum.MESSAGE_ID = cm.MESSAGE_ID"
            + " and "
            + "cum.USER_ID = cu.USER_ID"
            + " and "
            + "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION"
            + " and "
            + "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID"
            + " and "
            + "cum.user_id = cmf.user_id and cum.folder_id = cmf.folder_id"
            + " and "
            + "cum.housekeep_ind='Y'"
            + " and "
            + "cum.CREATE_DT < STR_TO_DATE(?,'%Y-%m-%d') "
            + " and "
            + "cum.MESSAGE_ID=?"
            + " order by "
            + "cu.my_id_key,sp.service_provider_id,cm.create_dt,cm.MESSAGE_ID " +
            " LIMIT ?, ? ";

    private static final String GET_FIELDS_FOR_ARCHIVE_IAS_MESSAGE = "Select "
            + "ium.client_id as client_id, ium.open_id as open_id, ium.noti_id as noti_id, sp.service_provider_id as service_provider_id, ium.create_dt as create_dt, im.ias_msg_id as ias_msg_id,"
            + "ium.user_message_id as user_message_id, "
            + "ium.tran_id as tran_id, "
            + "ium.read_ind as read_ind, "
            + "ium.ias_noti_status as ias_noti_status, "
            + "ium.ias_noti_result as ias_noti_result, "
            + "ium.tx_id as tx_id, "
            + "ium.sent_dt as sent_dt, "
            + "ium.ias_delivery_status as ias_delivery_status, "
            + "im.subject_en as subject_en,im.subject_tc as subject_tc,im.subject_sc as subject_sc, im.portal_id as portal_id, ium.read_ind as read_ind,"
            + "im.ref_id AS ref_id,"
            + "im.content_en as content_en,im.content_tc as content_tc,im.content_sc as content_sc,"
            + "im.ias_es_app_suffix_en as ias_es_app_suffix_en,im.ias_es_app_suffix_tc as ias_es_app_suffix_tc,im.ias_es_app_suffix_sc as ias_es_app_suffix_sc,"
            + "CASE WHEN exists(Select * from IAS_USER_MESSAGE ium2 where ium.ias_msg_id=ium2.ias_msg_id having count(*)=1) THEN 'N' ELSE 'Y' END AS broadcast_ind"
            + " from "
            + "IAS_USER_MESSAGE ium, IAS_MESSAGE im, CMC_TEMPLATE ct, CMC_SERVICE_PROVIDER sp"
            + " where "
            + "ium.IAS_MSG_ID = im.IAS_MSG_ID"
            + " and "
            + "im.TEMPLATE_ID = ct.TEMPLATE_ID and im.TEMPLATE_VERSION = ct.TEMPLATE_VERSION"
            + " and "
            + "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID"
            + " and "
            + "ium.housekeep_ind='Y'"
            + " and "
            + "ium.CREATE_DT < STR_TO_DATE(?,'%Y-%m-%d') "
            + " and "
            + "ium.IAS_MSG_ID=?"
            + " order by "
            + "ium.client_id,ium.open_id,sp.service_provider_id,im.create_dt,im.IAS_MSG_ID " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_MY_MESSAGE_LOB = "select rt.* from (" +
            "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, cum.CREATE_DT as create_dt, cm.message_id as message_id,"
            +
            "cm.subject_en as subject_en, cm.content_en as content_en," +
            "cm.subject_tc as subject_tc, cm.content_tc as content_tc," +
            "cm.subject_sc as subject_sc, cm.content_sc as content_sc" +
            " from " +
            "CMC_USER_MESSAGE cum, CMC_MESSAGE cm, CMC_USER cu, CMC_TEMPLATE ct, CMC_SERVICE_PROVIDER sp" +
            " where " +
            "cum.MESSAGE_ID = cm.MESSAGE_ID" +
            " and " +
            "cum.USER_ID = cu.USER_ID" +
            " and " +
            "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            " and " +
            "cum.housekeep_ind='Y'" +
            " and " +
            "cum.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) " +
            " and " +
            "cum.message_id=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,cm.create_dt,cm.MESSAGE_ID " +
            ")rt inner join (" +
            "select message_id,count(message_id) cnt from CMC_USER_MESSAGE where message_id=? group by message_id " +
            ") ct on rt.message_id=ct.message_id and ct.cnt=1 " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_MOBILE_MESSAGE_CSV = "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, cmm.create_dt as create_dt, cmm.mobile_msg_id as mobile_msg_id,"
            +
            "cmm.subject_en as subject_en, cumm.status as status, cmm.mobile_msg_type as mobile_msg_type, cmm.ref_id as ref_id,"
            +
            "cumm.mobile_device_token as mobile_device_token, cumm.CREATE_DT as file_dt," +
            "CASE WHEN exists(Select * from CMC_USER_MOBILE_MSG cumm2 where cumm.mobile_msg_id=cumm2.mobile_msg_id having count(*)=1) THEN 'N' ELSE 'Y' END AS broadcast_ind"
            +
            " from " +
            "CMC_USER_MOBILE_MSG cumm, CMC_MOBILE_MSG cmm, CMC_USER cu, CMC_TEMPLATE ct, CMC_MESSAGE cm, CMC_SERVICE_PROVIDER sp"
            +
            " where " +
            "cumm.USER_ID = cu.USER_ID" +
            " and " +
            "cumm.MOBILE_MSG_ID = cmm.MOBILE_MSG_ID" +
            " and " +
            "cm.MESSAGE_ID = cmm.REF_ID" +
            " and " +
            "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            " and " +
            "cumm.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cumm.housekeep_ind='Y'" +
            " and " +
            "cumm.MOBILE_MSG_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,cmm.create_dt,cm.MESSAGE_ID " +
            " LIMIT ?, ? ";

    private static final String GET_FIELDS_FOR_ARCHIVE_MOBILE_MESSAGE = "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, cmm.create_dt as create_dt, cmm.mobile_msg_id as mobile_msg_id,"
            +
            "cmm.subject_en as subject_en,cmm.subject_tc as subject_tc,cmm.subject_sc as subject_sc,cumm.status as status, cmm.mobile_msg_type as mobile_msg_type, cmm.ref_id as ref_id,"
            +
            "cumm.mobile_device_token as mobile_device_token, cumm.CREATE_DT as file_dt," +
            "cumm.broadcast_ind AS broadcast_ind," +
            "cmm.content_en as content_en,cmm.content_tc as content_tc,cmm.content_sc as content_sc" +
            " from " +
            "CMC_USER_MOBILE_MSG cumm, CMC_MOBILE_MSG cmm, CMC_USER cu, CMC_TEMPLATE ct, CMC_MESSAGE cm, CMC_SERVICE_PROVIDER sp"
            +
            " where " +
            "cumm.USER_ID = cu.USER_ID" +
            " and " +
            "cumm.MOBILE_MSG_ID = cmm.MOBILE_MSG_ID" +
            " and " +
            "cm.MESSAGE_ID = cmm.REF_ID" +
            " and " +
            "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            " and " +
            "cumm.CREATE_DT < STR_TO_DATE(?,'%Y-%m-%d')" +
            " and " +
            "cumm.housekeep_ind='Y'" +
            " and " +
            "cumm.MOBILE_MSG_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,cmm.create_dt,cm.MESSAGE_ID " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_MOBILE_MESSAGE_BK_CSV = "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, cmm.create_dt as create_dt, cmm.mobile_msg_id as mobile_msg_id,"
            +
            "cmm.subject_en as subject_en, cumm.status as status, cmm.mobile_msg_type as mobile_msg_type, cmm.ref_id as ref_id,"
            +
            "cumm.mobile_device_token as mobile_device_token, cumm.CREATE_DT as file_dt," +
            "CASE WHEN exists(Select * from CMC_USER_MOBILE_MSG_BK cumm2 where cumm.mobile_msg_id=cumm2.mobile_msg_id having count(*)=1) THEN 'N' ELSE 'Y' END AS broadcast_ind"
            +
            " from " +
            "CMC_USER_MOBILE_MSG_BK cumm, CMC_MOBILE_MSG_BK cmm, CMC_USER cu, CMC_TEMPLATE ct, CMC_MESSAGE cm, CMC_SERVICE_PROVIDER sp"
            +
            " where " +
            "cumm.USER_ID = cu.USER_ID" +
            " and " +
            "cumm.MOBILE_MSG_ID = cmm.MOBILE_MSG_ID" +
            " and " +
            "cm.MESSAGE_ID = cmm.REF_ID" +
            " and " +
            "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            " and " +
            "cumm.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cumm.housekeep_ind='Y'" +
            " and " +
            "cumm.MOBILE_MSG_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,cmm.create_dt,cm.MESSAGE_ID " +
            " LIMIT ?, ? ";

    private static final String GET_FIELDS_FOR_ARCHIVE_MOBILE_MESSAGE_BK = "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, cmm.create_dt as create_dt, cmm.mobile_msg_id as mobile_msg_id,"
            +
            "cmm.subject_en as subject_en, cmm.subject_tc as subject_tc,cmm.subject_sc as subject_sc,cumm.status as status, cmm.mobile_msg_type as mobile_msg_type, cmm.ref_id as ref_id,"
            +
            "cumm.mobile_device_token as mobile_device_token, cumm.CREATE_DT as file_dt," +
            "CASE WHEN exists(Select * from CMC_USER_MOBILE_MSG_BK cumm2 where cumm.mobile_msg_id=cumm2.mobile_msg_id having count(*)=1) THEN 'N' ELSE 'Y' END AS broadcast_ind,"
            +
            "cmm.content_en as content_en,cmm.content_tc as content_tc,cmm.content_sc as content_sc" +
            " from " +
            "CMC_USER_MOBILE_MSG_BK cumm, CMC_MOBILE_MSG_BK cmm, CMC_USER cu, CMC_TEMPLATE ct, CMC_MESSAGE cm, CMC_SERVICE_PROVIDER sp"
            +
            " where " +
            "cumm.USER_ID = cu.USER_ID" +
            " and " +
            "cumm.MOBILE_MSG_ID = cmm.MOBILE_MSG_ID" +
            " and " +
            "cm.MESSAGE_ID = cmm.REF_ID" +
            " and " +
            "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            " and " +
            "cumm.CREATE_DT < STR_TO_DATE(?,'%Y-%m-%d')" +
            " and " +
            "cumm.housekeep_ind='Y'" +
            " and " +
            "cumm.MOBILE_MSG_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,cmm.create_dt,cm.MESSAGE_ID " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_MOBILE_MESSAGE_LOB = "select rt.* from (" +
            "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, cmm.create_dt as create_dt, cmm.mobile_msg_id as mobile_msg_id,"
            +
            "cmm.subject_en as subject_en, cmm.content_en as content_en," +
            "cmm.subject_tc as subject_tc, cmm.content_tc as content_tc," +
            "cmm.subject_sc as subject_sc, cmm.content_sc as content_sc," +
            "cumm.CREATE_DT as file_dt" +
            " from " +
            "CMC_USER_MOBILE_MSG cumm, CMC_MOBILE_MSG cmm, CMC_USER cu, CMC_TEMPLATE ct, CMC_MESSAGE cm, CMC_SERVICE_PROVIDER sp"
            +
            " where " +
            "cumm.USER_ID = cu.USER_ID" +
            " and " +
            "cumm.MOBILE_MSG_ID = cmm.MOBILE_MSG_ID" +
            " and " +
            "cmm.REF_ID = cm.MESSAGE_ID" +
            " and " +
            "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            " and " +
            "cumm.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cumm.housekeep_ind='Y'" +
            " and " +
            "cumm.MOBILE_MSG_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,cmm.create_dt,cm.MESSAGE_ID " +
            ")rt inner join (" +
            "select mobile_msg_id,count(mobile_msg_id) cnt from CMC_USER_MOBILE_MSG where mobile_msg_id=? group by mobile_msg_id "
            +
            ") ct on rt.mobile_msg_id=ct.mobile_msg_id and ct.cnt=1 " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_MOBILE_MESSAGE_BK_LOB = "select rt.* from (" +
            "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, cmm.create_dt as create_dt, cmm.mobile_msg_id as mobile_msg_id,"
            +
            "cmm.subject_en as subject_en, cmm.content_en as content_en," +
            "cmm.subject_tc as subject_tc, cmm.content_tc as content_tc," +
            "cmm.subject_sc as subject_sc, cmm.content_sc as content_sc," +
            "cumm.CREATE_DT as file_dt" +
            " from " +
            "CMC_USER_MOBILE_MSG_BK cumm, CMC_MOBILE_MSG_BK cmm, CMC_USER cu, CMC_TEMPLATE ct, CMC_MESSAGE cm, CMC_SERVICE_PROVIDER sp"
            +
            " where " +
            "cumm.USER_ID = cu.USER_ID" +
            " and " +
            "cumm.MOBILE_MSG_ID = cmm.MOBILE_MSG_ID" +
            " and " +
            "cm.MESSAGE_ID = cmm.REF_ID" +
            " and " +
            "cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            " and " +
            "cumm.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cumm.housekeep_ind='Y'" +
            " and " +
            "cumm.MOBILE_MSG_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,cmm.create_dt,cm.MESSAGE_ID " +
            ")rt inner join (" +
            "select mobile_msg_id,count(mobile_msg_id) cnt from CMC_USER_MOBILE_MSG_BK where mobile_msg_id=? group by mobile_msg_id "
            +
            ") ct on rt.mobile_msg_id=ct.mobile_msg_id and ct.cnt=1 " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_MY_TO_DO_ITEM_CSV = "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, ctdi.create_dt as create_dt, ctdi.to_do_item_id as to_do_item_id,"
            +
            "ctdi.title_en as title_en, cutdi.read_ind as read_ind, cutdi.item_date as item_date," +
            "cutdi.complete_ind as complete_ind, cutdi.complete_by as complete_by, cutdi.complete_dt as complete_dt, ctdi.portal_id as portal_id,"
            +
            "CASE WHEN exists(Select * from CMC_USER_TO_DO_ITEM cutdi2 where cutdi.to_do_item_id=cutdi2.to_do_item_id having count(*)=1) THEN 'N' ELSE 'Y' END AS broadcast_ind"
            +
            " from " +
            "CMC_USER_TO_DO_ITEM cutdi, CMC_TO_DO_ITEM ctdi, CMC_TEMPLATE ct, CMC_SERVICE_PROVIDER sp, CMC_USER cu" +
            " where " +
            "cutdi.TO_DO_ITEM_ID = ctdi.TO_DO_ITEM_ID" +
            " and " +
            "ctdi.TEMPLATE_ID = ct.TEMPLATE_ID" +
            " and " +
            "ctdi.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "sp.SERVICE_PROVIDER_ID = ct.SERVICE_PROVIDER_ID" +
            " and " +
            "cu.user_id = cutdi.USER_ID" +
            " and " +
            "cutdi.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cutdi.housekeep_ind='Y'" +
            " and " +
            "cutdi.TO_DO_ITEM_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,ctdi.create_dt " +
            " LIMIT ?, ? ";

    private static final String GET_FIELDS_FOR_ARCHIVE_MY_TO_DO_ITEM = "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, ctdi.create_dt as create_dt, ctdi.to_do_item_id as to_do_item_id,"
            +
            "ctdi.title_en as title_en, ctdi.title_tc as title_tc,ctdi.title_sc as title_sc,cutdi.read_ind as read_ind, cutdi.item_date as item_date,"
            +
            "cutdi.complete_ind as complete_ind, cutdi.complete_by as complete_by, cutdi.complete_dt as complete_dt, ctdi.portal_id as portal_id,"
            +
            "cutdi.broadcast_ind AS broadcast_ind," +
            "ctdi.detail_en as detail_en,ctdi.detail_tc as detail_tc,ctdi.detail_sc as detail_sc" +
            " from " +
            "CMC_USER_TO_DO_ITEM cutdi, CMC_TO_DO_ITEM ctdi, CMC_TEMPLATE ct, CMC_SERVICE_PROVIDER sp, CMC_USER cu" +
            " where " +
            "cutdi.TO_DO_ITEM_ID = ctdi.TO_DO_ITEM_ID" +
            " and " +
            "ctdi.TEMPLATE_ID = ct.TEMPLATE_ID" +
            " and " +
            "ctdi.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "sp.SERVICE_PROVIDER_ID = ct.SERVICE_PROVIDER_ID" +
            " and " +
            "cu.user_id = cutdi.USER_ID" +
            " and " +
            "cutdi.CREATE_DT < STR_TO_DATE(?,'%Y-%m-%d')" +
            " and " +
            "cutdi.housekeep_ind='Y'" +
            " and " +
            "cutdi.TO_DO_ITEM_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,ctdi.create_dt " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_MY_TO_DO_ITEM_LOB = "select rt.* from (" +
            "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, ctdi.create_dt as create_dt, ctdi.to_do_item_id as to_do_item_id,"
            +
            "ctdi.title_en as title_en, ctdi.detail_en as detail_en," +
            "ctdi.title_tc as title_tc, ctdi.detail_tc as detail_tc," +
            "ctdi.title_sc as title_sc, ctdi.detail_sc as detail_sc" +
            " from " +
            "CMC_USER_TO_DO_ITEM cutdi, CMC_TO_DO_ITEM ctdi, CMC_TEMPLATE ct, CMC_SERVICE_PROVIDER sp, CMC_USER cu" +
            " where " +
            "cutdi.TO_DO_ITEM_ID = ctdi.TO_DO_ITEM_ID" +
            " and " +
            "ctdi.TEMPLATE_ID = ct.TEMPLATE_ID" +
            " and " +
            "ctdi.TEMPLATE_VERSION = ct.TEMPLATE_VERSION" +
            " and " +
            "sp.SERVICE_PROVIDER_ID = ct.SERVICE_PROVIDER_ID" +
            " and " +
            "cu.user_id = cutdi.USER_ID" +
            " and " +
            "cutdi.CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "cutdi.housekeep_ind='Y'" +
            " and " +
            "cutdi.TO_DO_ITEM_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,ctdi.create_dt " +
            ")rt inner join (" +
            "select to_do_item_id,count(to_do_item_id) cnt from CMC_USER_TO_DO_ITEM where to_do_item_id=? group by to_do_item_id "
            +
            ") ct on rt.to_do_item_id=ct.to_do_item_id and ct.cnt=1 " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_MY_BILL_CSV = "Select " +
            "cu.my_id_key as my_id_key, sp.service_provider_id as service_provider_id, cupt.create_dt as create_dt, cupt.tran_ref_number as tran_ref_number,"
            +
            "cupt.tran_date as tran_date, cupt.payment_method_cd as payment_method_cd, cupt.paid_amount as paid_amount, cupt.sent_to_rs_ind as sent_to_rs_ind,"
            +
            "cupt.void_ind as void_ind, cupt.void_date as void_date, cuab.account_no as account_no, cupt.issue_date as issue_date, cupt.due_date as due_date,"
            +
            "cupt.merchant_name_en as merchant_name_en, cupt.merchant_name_tc as merchant_name_tc, cupt.merchant_name_sc as merchant_name_sc"
            +
            " from " +
            "cmc_user_payment_tran cupt, cmc_user_acc_balance cuab, cmc_user cu, cmc_service_provider sp" +
            " where " +
            "cupt.account_balance_id = cuab.account_balance_id" +
            " and " +
            "cu.user_id = cuab.user_id" +
            " and " +
            "cuab.service_provider_id = sp.service_provider_id" +
            " and " +
            "cupt.CREATE_DT < STR_TO_DATE(?,'%Y-%m-%d') " +
            " and " +
            "cupt.housekeep_ind='Y'" +
            " and " +
            "cupt.IS_MYGOVHK='Y'" +
            " and " +
            "cupt.PAYMENT_STATUS='APPR'" +
            " and " +
            "cupt.TRAN_ID=?" +
            " order by " +
            "cu.my_id_key,sp.service_provider_id,cupt.create_dt " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_BROADCAST_EMAIL_CSV = "		    select  " +
            "		        distinct '*' as my_id_key, sp.service_provider_id as service_provider_id, ce.create_dt as create_dt, ce.email_id as email_id, "
            +
            "		        ce.subject_en as subject_en, ce.email_type as email_type, ce.ref_id as ref_id, ce.create_dt as file_dt"
            +
            "		    from ( " +
            "		      select * from ( " +
            "		        select email_id,count(email_id) cnt from CMC_USER_EMAIL " +
            "				where email_id=? and housekeep_ind='Y' and CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) group by email_id "
            +
            "		      ) where cnt>1 " +
            "		    ) cue " +
            "		    inner join CMC_EMAIL ce on cue.EMAIL_ID = ce.EMAIL_ID and ce.ref_id not like 'D%' " +
            "		    inner join CMC_MESSAGE cm on ce.REF_ID = cm.MESSAGE_ID " +
            "		    inner join CMC_TEMPLATE ct on cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION "
            +
            "		    inner join CMC_SERVICE_PROVIDER sp on ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            "		    order by  sp.service_provider_id,ce.create_dt " +
            " LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_BROADCAST_EMAIL_BK_CSV = "		    select  " +
            "		        distinct '*' as my_id_key, sp.service_provider_id as service_provider_id, ce.create_dt as create_dt, ce.email_id as email_id, "
            +
            "		        ce.subject_en as subject_en, ce.email_type as email_type, ce.ref_id as ref_id, ce.create_dt as file_dt"
            +
            "		    from ( " +
            "		      select * from ( " +
            "		        select email_id,count(email_id) cnt from CMC_USER_EMAIL_BK " +
            "				where email_id=? and housekeep_ind='Y' and CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) group by email_id "
            +
            "		      ) where cnt>1 " +
            "		    ) cue " +
            "		    inner join CMC_EMAIL_BK ce on cue.EMAIL_ID = ce.EMAIL_ID and ce.ref_id not like 'D%' " +
            "		    inner join CMC_MESSAGE cm on ce.REF_ID = cm.MESSAGE_ID " +
            "		    inner join CMC_TEMPLATE ct on cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION "
            +
            "		    inner join CMC_SERVICE_PROVIDER sp on ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            "		    order by  sp.service_provider_id,ce.create_dt " +
            " 			LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_BROADCAST_EMAIL_LOB = "		    select  " +
            "		        '*' as my_id_key, sp.service_provider_id as service_provider_id, ce.create_dt as create_dt, ce.email_id as email_id, "
            +
            "				ce.subject_en as subject_en, ce.content_en as content_en, " +
            "				ce.subject_tc as subject_tc, ce.content_tc as content_tc, " +
            "				ce.subject_sc as subject_sc, ce.content_sc as content_sc, " +
            "				ce.create_dt as file_dt" +
            "		    from ( " +
            "		      select * from ( " +
            "		        select email_id,count(email_id) cnt from CMC_USER_EMAIL " +
            "				where email_id=? and housekeep_ind='Y' and CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) group by email_id "
            +
            "		      ) where cnt>1 " +
            "		    ) cue " +
            "		    inner join CMC_EMAIL ce on cue.EMAIL_ID = ce.EMAIL_ID and ce.ref_id not like 'D%' " +
            "		    inner join CMC_MESSAGE cm on ce.REF_ID = cm.MESSAGE_ID " +
            "		    inner join CMC_TEMPLATE ct on cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION "
            +
            "		    inner join CMC_SERVICE_PROVIDER sp on ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            "		    order by sp.service_provider_id,ce.create_dt " +
            " 			LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_BROADCAST_MY_MESSAGE_CSV = "    select " +
            "      distinct '*' as my_id_key, sp.service_provider_id as service_provider_id, cm.create_dt as create_dt, cm.message_id as message_id, "
            +
            "      cm.subject_en as subject_en, cm.portal_id as portal_id, cm.email_ind as email_ind, cm.mobile_msg_ind as mobile_msg_ind"
            +
            "    from ( " +
            "      select * from( " +
            "        select message_id,count(message_id) cnt from CMC_USER_MESSAGE where message_id=? and housekeep_ind='Y' "
            +
            "			and CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) group by message_id " +
            "      ) where cnt>1 " +
            "    ) ct " +
            "    inner join CMC_MESSAGE cm on ct.MESSAGE_ID = cm.MESSAGE_ID " +
            "    inner join CMC_TEMPLATE ct on  cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION "
            +
            "    inner join CMC_SERVICE_PROVIDER sp on ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            "    order by sp.service_provider_id,cm.create_dt,cm.MESSAGE_ID " +
            " 	 LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_BROADCAST_EMAIL_BK_LOB = "		    select  " +
            "		        '*' as my_id_key, sp.service_provider_id as service_provider_id, ce.create_dt as create_dt, ce.email_id as email_id, "
            +
            "				ce.subject_en as subject_en, ce.content_en as content_en, " +
            "				ce.subject_tc as subject_tc, ce.content_tc as content_tc, " +
            "				ce.subject_sc as subject_sc, ce.content_sc as content_sc, " +
            "				ce.create_dt as file_dt" +
            "		    from ( " +
            "		      select * from ( " +
            "		        select email_id,count(email_id) cnt from CMC_USER_EMAIL_BK " +
            "				where email_id=? and housekeep_ind='Y' and CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) group by email_id "
            +
            "		      ) where cnt>1 " +
            "		    ) cue " +
            "		    inner join CMC_EMAIL_BK ce on cue.EMAIL_ID = ce.EMAIL_ID and ce.ref_id not like 'D%' " +
            "		    inner join CMC_MESSAGE cm on ce.REF_ID = cm.MESSAGE_ID " +
            "		    inner join CMC_TEMPLATE ct on cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION "
            +
            "		    inner join CMC_SERVICE_PROVIDER sp on ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            "		    order by sp.service_provider_id,ce.create_dt " +
            " 			LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_BROADCAST_MY_MESSAGE_LOB = "select * from (   " +
            "		select a.*, rnum from ( " +
            "	    select " +
            "	    '*' as my_id_key, sp.service_provider_id as service_provider_id, cm.create_dt as create_dt, cm.message_id as message_id,"
            +
            "	    cm.subject_en as subject_en, cm.content_en as content_en, " +
            "	    cm.subject_tc as subject_tc, cm.content_tc as content_tc, " +
            "	    cm.subject_sc as subject_sc, cm.content_sc as content_sc" +
            "	    from ( " +
            "	      select * from( " +
            "	        select message_id,count(message_id) cnt from CMC_USER_MESSAGE where message_id=? " +
            "			and housekeep_ind='Y' and CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) group by message_id "
            +
            "	      ) where cnt>1 " +
            "	    ) ct " +
            "	    inner join CMC_MESSAGE cm on ct.MESSAGE_ID = cm.MESSAGE_ID " +
            "	    inner join CMC_TEMPLATE ct on  cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION"
            +
            "	    inner join CMC_SERVICE_PROVIDER sp on ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            "	    order by sp.service_provider_id,cm.create_dt,cm.MESSAGE_ID " +
            "	  ) a LIMIT ? " +
            "	) where rnum  >= ?";

    private static final String GET_FIELD_FOR_ARCHIVE_BROADCAST_MOBILE_MESSAGE_CSV = "    select " +
            "      distinct '*' as my_id_key, sp.service_provider_id as service_provider_id, cmm.create_dt as create_dt, cmm.mobile_msg_id as mobile_msg_id, "
            +
            "      cmm.subject_en as subject_en, cmm.mobile_msg_type as mobile_msg_type, cmm.ref_id as ref_id, " +
            "      cmm.create_dt as file_dt" +
            "    from " +
            "    ( " +
            "      select * from ( " +
            "        select mobile_msg_id,count(mobile_msg_id) cnt from CMC_USER_MOBILE_MSG " +
            "		 	where mobile_msg_id=? and housekeep_ind='Y' and CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) group by mobile_msg_id "
            +
            "      ) where cnt>1 " +
            "    ) cumm " +
            "    inner join CMC_MOBILE_MSG cmm on cumm.MOBILE_MSG_ID = cmm.MOBILE_MSG_ID " +
            "    inner join CMC_MESSAGE cm on cmm.REF_ID = cm.MESSAGE_ID " +
            "    inner join CMC_TEMPLATE ct on 	cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION "
            +
            "    inner join CMC_SERVICE_PROVIDER sp on ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            "    order by sp.service_provider_id,cmm.create_dt " +
            " 	 LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_BROADCAST_MOBILE_MESSAGE_BK_CSV = "    select " +
            "        distinct '*' as my_id_key, sp.service_provider_id as service_provider_id, cmm.create_dt as create_dt, cmm.mobile_msg_id as mobile_msg_id, "
            +
            "        cmm.subject_en as subject_en, cmm.mobile_msg_type as mobile_msg_type, cmm.ref_id as ref_id, " +
            "        cmm.create_dt as file_dt" +
            "    from " +
            "    ( " +
            "      select * from ( " +
            "        select mobile_msg_id,count(mobile_msg_id) cnt from CMC_USER_MOBILE_MSG_BK " +
            "			where mobile_msg_id=? and housekeep_ind='Y' and CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) group by mobile_msg_id "
            +
            "      ) where cnt>1 " +
            "    ) cumm " +
            "    inner join CMC_MOBILE_MSG_BK cmm on cumm.MOBILE_MSG_ID = cmm.MOBILE_MSG_ID " +
            "    inner join CMC_MESSAGE cm on cmm.REF_ID = cm.MESSAGE_ID " +
            "    inner join CMC_TEMPLATE ct on 	cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION "
            +
            "    inner join CMC_SERVICE_PROVIDER sp on ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            "    order by sp.service_provider_id,cmm.create_dt " +
            " 	 LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_BROADCAST_MOBILE_MESSAGE_LOB = "    select  " +
            "		'*' as my_id_key, sp.service_provider_id as service_provider_id, cmm.create_dt as create_dt, cmm.mobile_msg_id as mobile_msg_id,"
            +
            "		cmm.subject_en as subject_en, cmm.content_en as content_en, " +
            "		cmm.subject_tc as subject_tc, cmm.content_tc as content_tc, " +
            "		cmm.subject_sc as subject_sc, cmm.content_sc as content_sc, " +
            "		cmm.create_dt as file_dt" +
            "    from " +
            "    ( " +
            "      select * from ( " +
            "        select mobile_msg_id,count(mobile_msg_id) cnt from CMC_USER_MOBILE_MSG " +
            "			where mobile_msg_id=? and housekeep_ind='Y' and CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) group by mobile_msg_id "
            +
            "      ) where cnt>1 " +
            "    ) cumm " +
            "    inner join CMC_MOBILE_MSG cmm on cumm.MOBILE_MSG_ID = cmm.MOBILE_MSG_ID " +
            "    inner join CMC_MESSAGE cm on cmm.REF_ID = cm.MESSAGE_ID " +
            "    inner join CMC_TEMPLATE ct on 	cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION "
            +
            "    inner join CMC_SERVICE_PROVIDER sp on ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            "    order by sp.service_provider_id,cmm.create_dt " +
            " 	 LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_BROADCAST_MOBILE_MESSAGE_BK_LOB = "    select " +
            "          '*' as my_id_key, sp.service_provider_id as service_provider_id, cmm.create_dt as create_dt, cmm.mobile_msg_id as mobile_msg_id,"
            +
            "          cmm.subject_en as subject_en, cmm.content_en as content_en, " +
            "          cmm.subject_tc as subject_tc, cmm.content_tc as content_tc, " +
            "          cmm.subject_sc as subject_sc, cmm.content_sc as content_sc, " +
            "          cmm.create_dt as file_dt" +
            "    from " +
            "    ( " +
            "      select * from ( " +
            "        select mobile_msg_id,count(mobile_msg_id) cnt from CMC_USER_MOBILE_MSG_BK " +
            "			where mobile_msg_id=? and housekeep_ind='Y' and CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) group by mobile_msg_id "
            +
            "      ) where cnt>1 " +
            "    ) cumm " +
            "    inner join CMC_MOBILE_MSG_BK cmm on cumm.MOBILE_MSG_ID = cmm.MOBILE_MSG_ID " +
            "    inner join CMC_MESSAGE cm on cmm.REF_ID = cm.MESSAGE_ID " +
            "    inner join CMC_TEMPLATE ct on 	cm.TEMPLATE_ID = ct.TEMPLATE_ID and cm.TEMPLATE_VERSION = ct.TEMPLATE_VERSION "
            +
            "    inner join CMC_SERVICE_PROVIDER sp on ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            "    order by sp.service_provider_id,cmm.create_dt " +
            " 	 LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_BROADCAST_MY_TO_DO_ITEM_CSV = "    select " +
            "      distinct '*' as my_id_key, sp.service_provider_id as service_provider_id, " +
            "      ctdi.create_dt as create_dt, ctdi.to_do_item_id as to_do_item_id, " +
            "      ctdi.title_en as title_en, ctdi.portal_id as portal_id" +
            "    from " +
            "    ( " +
            "      select * from ( " +
            "        select to_do_item_id,count(to_do_item_id) cnt from CMC_USER_TO_DO_ITEM " +
            "			where to_do_item_id=? and housekeep_ind='Y' and CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) group by to_do_item_id "
            +
            "      ) where cnt>1 " +
            "    ) cutdi " +
            "    inner join CMC_TO_DO_ITEM ctdi on cutdi.TO_DO_ITEM_ID = ctdi.TO_DO_ITEM_ID " +
            "    inner join CMC_TEMPLATE ct on ctdi.TEMPLATE_ID = ct.TEMPLATE_ID  and ctdi.TEMPLATE_VERSION = ct.TEMPLATE_VERSION "
            +
            "    inner join CMC_SERVICE_PROVIDER sp on ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            "    order by sp.service_provider_id,ctdi.create_dt " +
            " 	 LIMIT ?, ? ";

    private static final String GET_FIELD_FOR_ARCHIVE_BROADCAST_MY_TO_DO_ITEM_LOB = "    select " +
            "        '*' as my_id_key, sp.service_provider_id as service_provider_id, ctdi.create_dt as create_dt, ctdi.to_do_item_id as to_do_item_id,"
            +
            "        ctdi.title_en as title_en, ctdi.detail_en as detail_en, " +
            "        ctdi.title_tc as title_tc, ctdi.detail_tc as detail_tc, " +
            "        ctdi.title_sc as title_sc, ctdi.detail_sc as detail_sc" +
            "    from " +
            "    ( " +
            "      select * from ( " +
            "        select to_do_item_id,count(to_do_item_id) cnt from CMC_USER_TO_DO_ITEM " +
            "			where to_do_item_id=? and housekeep_ind='Y' and CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH) group by to_do_item_id "
            +
            "      ) where cnt>1 " +
            "    ) cutdi " +
            "    inner join CMC_TO_DO_ITEM ctdi on cutdi.TO_DO_ITEM_ID = ctdi.TO_DO_ITEM_ID " +
            "    inner join CMC_TEMPLATE ct on ctdi.TEMPLATE_ID = ct.TEMPLATE_ID  and ctdi.TEMPLATE_VERSION = ct.TEMPLATE_VERSION "
            +
            "    inner join CMC_SERVICE_PROVIDER sp on ct.SERVICE_PROVIDER_ID = sp.SERVICE_PROVIDER_ID" +
            "    order by sp.service_provider_id,ctdi.create_dt " +
            " 	 LIMIT ?, ? ";

    private static final String SELECT_CMC_ARCHIVE_INDEX = "Select " +
            "my_id_key,sp_id" +
            " from " +
            "CMC_ARCHIVE_INDEX" +
            " where " +
            "my_id_key=?" +
            " and " +
            "sp_id=?";

    private static final String INSERT_CMC_ARCHIVE_INDEX = "insert into " +
            "CMC_ARCHIVE_INDEX(MY_ID_KEY,SP_ID,FIRST_ARCHIVE_REC_CREATE_DT,LAST_ARCHIVE_REC_CREATE_DT)" +
            " values " +
            "(?,?,?,?)";

    private static final String UPDATE_CMC_ARCHIVE_INDEX = "Update " +
            "CMC_ARCHIVE_INDEX" +
            " set " +
            "LAST_ARCHIVE_REC_CREATE_DT=?" +
            " where " +
            "my_id_key=?" +
            " and " +
            "sp_id=?";

    private static final String SELECT_IAS_ARCHIVE_INDEX = "Select " +
            "noti_id,sp_id" +
            " from " +
            "IAS_ARCHIVE_INDEX" +
            " where " +
            "noti_id=?" +
            " and " +
            "sp_id=?";

    private static final String INSERT_IAS_ARCHIVE_INDEX = "insert into " +
            "IAS_ARCHIVE_INDEX(NOTI_ID,SP_ID,FIRST_ARCHIVE_REC_CREATE_DT,LAST_ARCHIVE_REC_CREATE_DT)" +
            " values " +
            "(?,?,?,?)";

    private static final String UPDATE_IAS_ARCHIVE_INDEX = "Update " +
            "IAS_ARCHIVE_INDEX" +
            " set " +
            "LAST_ARCHIVE_REC_CREATE_DT=?" +
            " where " +
            "noti_id=?" +
            " and " +
            "sp_id=?";

    private static final String DEL_CMC_EMAIL = "Delete from " +
            "CMC_EMAIL" +
            " where " +
            "email_id=?";

    private static final String DEL_CMC_EMAIL_H = "Delete from " +
            "CMC_EMAIL_H" +
            " where " +
            "email_id=?";

    private static final String DEL_CMC_EMAIL_BK = "Delete from " +
            "CMC_EMAIL_BK" +
            " where " +
            "email_id=?";

    private static final String DEL_CMC_MESSAGE = "Delete from " +
            "CMC_MESSAGE" +
            " where " +
            "message_id=?";

    private static final String DEL_IAS_MESSAGE = "Delete from " +
            "IAS_MESSAGE" +
            " where " +
            "ias_msg_id=?";

    private static final String DEL_CMC_MESSAGE_H = "Delete from " +
            "CMC_MESSAGE_H" +
            " where " +
            "message_id=?";

    private static final String DEL_CMC_MOBILE_MESSAGE = "Delete from " +
            "CMC_MOBILE_MSG" +
            " where " +
            "mobile_msg_ID=?";

    private static final String DEL_CMC_MOBILE_MESSAGE_H = "Delete from " +
            "CMC_MOBILE_MSG_H" +
            " where " +
            "mobile_msg_ID=?";

    private static final String DEL_CMC_MOBILE_MESSAGE_BK = "Delete from " +
            "CMC_MOBILE_MSG_BK" +
            " where " +
            "mobile_msg_ID=?";

    private static final String DEL_CMC_REMINDER = "Delete from " +
            "CMC_REMINDER" +
            " where " +
            "reminder_id=?";

    private static final String DEL_CMC_REMINDER_H = "Delete from " +
            "CMC_REMINDER_H" +
            " where " +
            "reminder_id=?";

    private static final String DEL_CMC_USER_EMAIL = "Delete from " +
            "CMC_USER_EMAIL" +
            " where " +
            "user_id=? and email_id=?";

    private static final String DEL_CMC_USER_EMAIL_H = "Delete from " +
            "CMC_USER_EMAIL_H" +
            " where " +
            "user_id=? and email_id=?";

    private static final String DEL_CMC_USER_EMAIL_BK = "Delete from " +
            "CMC_USER_EMAIL_BK" +
            " where " +
            "user_id=? and email_id=?";

    private static final String DEL_CMC_USER_MESSAGE = "Delete from " +
            "CMC_USER_MESSAGE" +
            " where " +
            "user_id=? and message_id=?";

    private static final String DEL_CMC_USER_MESSAGE_H = "Delete from " +
            "CMC_USER_MESSAGE_H" +
            " where " +
            "user_id=? and message_id=?";

    private static final String DEL_IAS_USER_MESSAGE = "Delete from " +
            "IAS_USER_MESSAGE" +
            " where " +
            "client_id=? and open_id=? and ias_msg_id=?";

    private static final String DEL_IAS_USER_MESSAGE_H = "Delete from " +
            "IAS_USER_MESSAGE_H" +
            " where " +
            "client_id=? and open_id=? and ias_msg_id=?";

    private static final String DEL_IAS_MSG_STATUS_QUEUE = "Delete from " +
            "IAS_MSG_STATUS_QUEUE" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "JOB_STATUS='C'";

    private static final String DEL_IAS_ASSO_QUEUE = "Delete from " +
            "IAS_ASSO_QUEUE" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "JOB_STATUS='C'";

    private static final String DEL_CMC_USER_MOBILE_MESSAGE = "Delete from " +
            "CMC_USER_MOBILE_MSG" +
            " where " +
            "user_id=? and mobile_msg_id=?";

    private static final String DEL_CMC_USER_MOBILE_MESSAGE_H = "Delete from " +
            "CMC_USER_MOBILE_MSG_H" +
            " where " +
            "user_id=? and mobile_msg_id=?";

    private static final String DEL_CMC_USER_MOBILE_MESSAGE_BK = "Delete from " +
            "CMC_USER_MOBILE_MSG_BK" +
            " where " +
            "user_id=? and mobile_msg_id=?";

    private static final String DEL_CMC_MESSAGE_FOLDER = "Delete from " +
            "CMC_MESSAGE_FOLDER" +
            " where " +
            "folder_id=?";

    private static final String DEL_CMC_MESSAGE_FOLDER_H = "Delete from " +
            "CMC_MESSAGE_FOLDER_H" +
            " where " +
            "folder_id=?";

    private static final String DEL_CMC_TO_DO_ITEM = "Delete from " +
            "CMC_TO_DO_ITEM" +
            " where " +
            "to_do_item_id=?";

    private static final String DEL_CMC_TO_DO_ITEM_H = "Delete from " +
            "CMC_TO_DO_ITEM_H" +
            " where " +
            "to_do_item_id=?";

    private static final String DEL_CMC_USER_TO_DO_ITEM = "Delete from " +
            "CMC_USER_TO_DO_ITEM" +
            " where " +
            "user_id=? and to_do_item_id=?";

    private static final String DEL_CMC_USER_TO_DO_ITEM_H = "Delete from " +
            "CMC_USER_TO_DO_ITEM_H" +
            " where " +
            "user_id=? and to_do_item_id=?";

    private static final String DEL_CMC_USER_ACC_BALANCE = "Delete from " +
            "CMC_USER_ACC_BALANCE" +
            " where " +
            "user_id=? and account_balance_id=?";

    private static final String DEL_CMC_USER_ACC_BALANCE_H = "Delete from " +
            "CMC_USER_ACC_BALANCE_H" +
            " where " +
            "user_id=? and account_balance_id=?";

    private static final String DEL_CMC_USER_PAYMENT_TRAN = "Delete from " +
            "CMC_USER_PAYMENT_TRAN" +
            " where " +
            "tran_id=?";

    private static final String DEL_CMC_USER_PAYMENT_TRAN_H = "Delete from " +
            "CMC_USER_PAYMENT_TRAN_H" +
            " where " +
            "tran_id=?";

    private static final String DEL_PA_POS_PAY_TRAN = "Delete from " +
            "PA_POS_PAY_TRAN" +
            " where " +
            "tran_id=?";

    private static final String DEL_PA_POS_PAY_TRAN_SUMMARY = "Delete from " +
            "PA_POS_PAY_TRAN_SUMMARY" +
            " where " +
            "tran_id=?";
    private static final String DEL_PA_POS_PSDO = "Delete from " +
            "PA_POS_PSDO" +
            " where " +
            "tran_id=?";

    private static final String DEL_PA_POS_PSDR = "Delete from " +
            "PA_POS_PSDR" +
            " where " +
            "tran_id=?";

    private static final String DEL_PR_PSDR = "Delete from " +
            "PR_PSDR" +
            " where " +
            "tran_id=?";

    private static final String DEL_CMC_UNDELIVERED_BILL = "Delete from CMC_UNDELIVERED_BILL" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)";

    private static final String DEL_CMC_UNDELIVERED_BILL_H = "Delete from " +
            "CMC_UNDELIVERED_BILL_H" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)";

    private static final String DEL_CMC_UNDELIVERED_ITEM = "Delete from " +
            "CMC_UNDELIVERED_ITEM" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)";

    private static final String DEL_CMC_UNDELIVERED_ITEM_H = "Delete from " +
            "CMC_UNDELIVERED_ITEM_H" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)";

    private static final String DEL_CMC_UNDELIVERED_MESSAGE = "Delete from " +
            "CMC_UNDELIVERED_MESSAGE" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)";

    private static final String DEL_CMC_UNDELIVERED_MESSAGE_H = "Delete from " +
            "CMC_UNDELIVERED_MESSAGE_H" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)";

    private static final String DEL_CMC_HIDDEN_REINSTATE_BILL = "Delete from " +
            "CMC_HIDDEN_REINSTATE_BILL" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)";

    private static final String DEL_CMC_HIDDEN_REINSTATE_ITEM = "Delete from " +
            "CMC_HIDDEN_REINSTATE_ITEM" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)";

    private static final String DEL_CMC_HIDDEN_REINSTATE_MESG = "Delete from " +
            "CMC_HIDDEN_REINSTATE_MESG" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)";

    private static final String DEL_CMC_ASYN_MESSAGE = "Delete from " +
            "CMC_ASYN_MESSAGE" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "status='S'";

    private static final String DEL_CMC_ASYN_MESSAGE_H = "Delete from " +
            "CMC_ASYN_MESSAGE_H" +
            " where " +
            "msg_id in (" +
            "Select " +
            "msg_id" +
            " from " +
            "CMC_ASYN_MESSAGE" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "status='S'" +
            ")";

    private static final String DEL_CMC_ASYN_RSP_CTRL = "Delete from " +
            "CMC_ASYN_RSP_CTRL" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "(status='F' or status='A')";

    private static final String DEL_CMC_ASYN_RSP_CTRL_H = "Delete from " +
            "CMC_ASYN_RSP_CTRL_H" +
            " where " +
            "corr_id in (" +
            "Select " +
            "corr_id" +
            " from " +
            "CMC_ASYN_RSP_CTRL" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "(status='F' or status='A')" +
            ")";

    private static final String DEL_CMC_EGIS_NOTI_STATUS = "Delete from " +
            "CMC_EGIS_NOTI_STATUS" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "(status='C' or status='F')";

    private static final String DEL_CMC_EGIS_NOTI_STATUS_H = "Delete from " +
            "CMC_EGIS_NOTI_STATUS_H" +
            " where " +
            "noti_job_id in (" +

            "Select " +
            "noti_job_id" +
            " from " +
            "CMC_EGIS_NOTI_STATUS" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "(status='C' or status='F')" +

            ")";

    private static final String DEL_CMC_PERSONAL_EMAIL_SCH = "Delete from " +
            "CMC_PERSONAL_EMAIL_SCH" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "status='Y'";

    private static final String DEL_CMC_PERSONAL_EMAIL_SCH_H = "Delete from " +
            "CMC_PERSONAL_EMAIL_SCH_H" +
            " where " +
            "(user_id,ref_id) in (" +
            "Select " +
            "user_id,ref_id" +
            " from " +
            "CMC_PERSONAL_EMAIL_SCH" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "status='Y'" +
            ")";

    private static final String DEL_CMC_USER_MESSAGE_ALERT = "Delete from " +
            "CMC_USER_MESSAGE_ALERT" +
            " where " +
            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " and " +
            "status='S'";

    private static final String DEL_CMC_ARCHIVE_INDEX = "Delete from " +
            "CMC_ARCHIVE_INDEX" +
            " where " +
            "LAST_ARCHIVE_REC_CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)";

    private static final String DEL_CMC_USER_MESSAGE_H_BY_CREATE_DATE = "Delete from " +
            "CMC_USER_MESSAGE_H" +
            " where " +

            "CREATE_DT < DATE(STR_TO_DATE(?,'%Y-%m-%d') + interval ? MONTH)" +
            " LIMIT ? ";
}
