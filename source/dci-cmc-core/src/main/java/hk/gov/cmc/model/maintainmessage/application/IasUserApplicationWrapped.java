package hk.gov.cmc.model.maintainmessage.application;

public class IasUserApplicationWrapped {

    private IasUserApplication iasUserApplication;
    private String hkid;

    public IasUserApplicationWrapped() {
        super();
    }

    public IasUserApplication getIasUserApplication() {
        return iasUserApplication;
    }

    public void setIasUserApplication(IasUserApplication iasUserApplication) {
        this.iasUserApplication = iasUserApplication;
    }

    public String getHkid() {
        return hkid;
    }

    public void setHkid(String hkid) {
        this.hkid = hkid;
    }

    @Override
    public String toString() {
        return "IasUserApplicationWrapped [iasUserApplication=" + iasUserApplication + ", hkid=" + hkid + "]";
    }
}
