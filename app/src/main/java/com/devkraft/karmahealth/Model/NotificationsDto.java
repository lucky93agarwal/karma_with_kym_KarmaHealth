package com.devkraft.karmahealth.Model;

import java.util.List;

public class NotificationsDto {

    private List<Notification> notification;
    private int unreadCount;

    public List<Notification> getNotification() {
        return notification;
    }

    public void setNotification(List<Notification> notification) {
        this.notification = notification;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
