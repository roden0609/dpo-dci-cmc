package hk.gov.cmc.eid.bean.pushNotification.request;

import java.util.ArrayList;

public class PushNotificationBean {

    private ArrayList<PushNotificationItem> notifications;

    public PushNotificationBean() {
    }

    public PushNotificationBean(ArrayList<PushNotificationItem> notifications) {
        this.notifications = notifications;
    }

    public ArrayList<PushNotificationItem> getPushNotifications() {
        return notifications;
    }

    public void setPushNotifications(ArrayList<PushNotificationItem> notifications) {
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "PushNotificationBean [notifications=" + notifications + "]";
    }
}
