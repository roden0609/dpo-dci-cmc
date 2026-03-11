/*
 * Design, Implementation and Support of Common Middleware Components and Reference Applications 
 * 
 * Representation of Recipient
 * 
 * Developed by: Gilbert Liao
 * Reviewed by: Kai So
 * Tester: Shaohui Wang
 */
package hk.gov.dpo.mars_cmc.cmc.datatype.message;

import java.io.Serializable;
import java.util.Date;

/**
 * Representation of Recipient
 */
public class Recipient implements Serializable {

	public static final String NEW = "New";
	public static final String DELETE = "Delete";
	public static final String REPLACE = "Replace";

	private String m_tranID;
	private String m_idpID;
	private String m_recipientID;
	private Date m_itemDate;
	private String m_action;
	private String m_correlatedTranID;

	// CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
	private String m_appRefNum;
	private String m_appStatus;
	private Date m_appStatusUpdateDate;
	private String m_contactEmail;
	private String m_contactNum;
	private String m_miscInfo;
	// CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END

	// CMC-2025-002: Accept HKID input in IasToDoItem and IasApplication function - BEGIN
	private String m_recipientIDType;
	// CMC-2025-002: Accept HKID input in IasToDoItem and IasApplication function - END

	public String getTranID() {
		return m_tranID;
	}

	public void setTranID(String tranID) {
		m_tranID = tranID;
	}

	public String getIdpID() {
		return m_idpID;
	}

	public void setIdpID(String idpID) {
		m_idpID = idpID;
	}

	public String getRecipientID() {
		return m_recipientID;
	}

	public void setRecipientID(String recipientID) {
		m_recipientID = recipientID;
	}

	public Date getItemDate() {
		return m_itemDate;
	}

	public void setItemDate(Date itemDate) {
		m_itemDate = itemDate;
	}

	public String getAction() {
		return m_action;
	}

	public void setAction(String action) {
		m_action = action;
	}

	public String getCorrelatedTranID() {
		return m_correlatedTranID;
	}

	public void setCorrelatedTranID(String correlatedTranID) {
		m_correlatedTranID = correlatedTranID;
	}

	// CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
	public String getAppRefNum() {
		return m_appRefNum;
	}

	public void setAppRefNum(String appRefNum) {
		m_appRefNum = appRefNum;
	}

	public String getAppStatus() {
		return m_appStatus;
	}

	public void setAppStatus(String appStatus) {
		m_appStatus = appStatus;
	}

	public Date getAppStatusUpdateDate() {
		return m_appStatusUpdateDate;
	}

	public void setAppStatusUpdateDate(Date appStatusUpdateDate) {
		m_appStatusUpdateDate = appStatusUpdateDate;
	}

	public String getContactEmail() {
		return m_contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		m_contactEmail = contactEmail;
	}

	public String getContactNum() {
		return m_contactNum;
	}

	public void setContactNum(String contactNum) {
		m_contactNum = contactNum;
	}

	public String getMiscInfo() {
		return m_miscInfo;
	}

	public void setMiscInfo(String miscInfo) {
		m_miscInfo = miscInfo;
	}
	// CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END

	// CMC-2025-002: Accept HKID input in IasToDoItem and IasApplication function - BEGIN
	public String getRecipientIDType() {
		return m_recipientIDType;
	}

	public void setRecipientIDType(String recipientIDType) {
		m_recipientIDType = recipientIDType;
	}
	// CMC-2025-002: Accept HKID input in IasToDoItem and IasApplication function - END

}
