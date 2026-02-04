package hk.gov.cmc.appserver.ejb.session.housekeep;

import jakarta.ejb.EJBException;

public interface IHouseKeepingRecordSessionBM {

    public void markHouseKeepIndicator(String inputDate) throws EJBException;

    public void removeHistoricalRecord(String inputDate) throws EJBException;

    public void deleteMainTableRecord(String inputDate) throws EJBException;
}
