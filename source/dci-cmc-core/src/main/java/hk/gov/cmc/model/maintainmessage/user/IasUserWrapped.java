package hk.gov.cmc.model.maintainmessage.user;

import java.io.Serializable;
import java.lang.String;

public class IasUserWrapped implements Serializable {

  private static final long serialVersionUID = 1L;

  private IasUser iasUser;
  private String hkid;

  public IasUserWrapped() {
    super();
  }

  public IasUser getIasUser() {
    return iasUser;
  }

  public void setIasUser(IasUser iasUser) {
    this.iasUser = iasUser;
  }

  public String getHkid() {
    return hkid;
  }

  public void setHkid(String hkid) {
    this.hkid = hkid;
  }

  @Override
  public String toString() {
    return "IasUserWrapped [iasUser=" + iasUser + ", hkid=" + hkid + "]";
  }

}
