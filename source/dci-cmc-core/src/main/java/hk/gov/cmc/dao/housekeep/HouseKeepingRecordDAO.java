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
    private static final String GET_DEL_IAS_APPLICATION_ID_FROM_IAS_USER_APPLICATION = "Select " +
            "distinct iua.ias_application_id" +
            " from " +
            "ias_user_application iua" +
            " where " +
            "iua.CREATE_DT < DATE(now()) + INTERVAL ? MONTH " +
            " and " +
            "iua.housekeep_ind = 'Y'" +
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
}
