package hk.gov.cmc.appserver.ejb.session.housekeep;

import jakarta.ejb.EJBException;

public interface IHouseKeepingRecordSessionBM {

    public void markHouseKeepIndicator() throws EJBException;

    public void removeHistoricalRecord(String inputDate) throws EJBException;

    public void deleteMainTableRecord() throws EJBException;
}
