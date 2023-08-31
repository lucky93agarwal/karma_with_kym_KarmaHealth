package com.devkraft.karmahealth.Model;

import android.content.ContentValues;
import android.database.Cursor;

import com.devkraft.karmahealth.db.NotificationTable;

import java.util.Objects;

public class Notification {

    private Long id;
    private String icon;
    private String body;
    private String title;
    private String notificationType;
    private boolean read;
    private Long genericNameId;
    private String severity;
    private Long createdDate;
    private String url;
    private Long documentId;
    private Long combinationId;
    private Long productId;
    private Long notificationId;
    private Long userId;
    private String userName;

    private String header;
    private String notificationColorCode;

    public Notification(Long id, String icon, String body, String title, String notificationType, boolean read, Long genericNameId, String severity, Long createdDate, String url, Long documentId, Long combinationId, Long productId, Long notificationId, Long userId, String userName, String header, String notificationColorCode) {
        this.id = id;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.notificationType = notificationType;
        this.read = read;
        this.genericNameId = genericNameId;
        this.severity = severity;
        this.createdDate = createdDate;
        this.url = url;
        this.documentId = documentId;
        this.combinationId = combinationId;
        this.productId = productId;
        this.notificationId = notificationId;
        this.userId = userId;
        this.userName = userName;
        this.header = header;
        this.notificationColorCode = notificationColorCode;
    }

    public ContentValues toCV() {
        ContentValues cv = new ContentValues();

        cv.put(NotificationTable.SID, id);
        cv.put(NotificationTable.ICON, icon);
        cv.put(NotificationTable.BODY, body);
        cv.put(NotificationTable.TITLE, title);
        cv.put(NotificationTable.NOTIFICATION_TYPE, notificationType);
        if(read){
            cv.put(NotificationTable.READ, 1);
        }else {
            cv.put(NotificationTable.READ, 0);
        }
        cv.put(NotificationTable.GENERIC_NAME_ID, genericNameId);
        cv.put(NotificationTable.SEVERITY, severity);
        cv.put(NotificationTable.CREATION_DATE, createdDate);
        cv.put(NotificationTable.URL, url);
        cv.put(NotificationTable.DOCUMENT_ID, documentId);
        cv.put(NotificationTable.COMBINATION_ID, combinationId);
        cv.put(NotificationTable.PRODUCT_ID, productId);

        cv.put(NotificationTable.NOTIFICATION_ID, notificationId);
        cv.put(NotificationTable.USER_ID, userId);
        cv.put(NotificationTable.USER_NAME, userName);

        cv.put(NotificationTable.NOTIFICATION_COLOR_CODE, notificationColorCode);
        cv.put(NotificationTable.HEADER, header);

        return cv;
    }

    public static Notification fromCursor(Cursor cursor) {

        return   new Notification(cursor.getLong(cursor.getColumnIndex(NotificationTable.SID)),
                cursor.getString(cursor.getColumnIndex(NotificationTable.ICON)),
                cursor.getString(cursor.getColumnIndex(NotificationTable.BODY)),
                cursor.getString(cursor.getColumnIndex(NotificationTable.TITLE)),
                cursor.getString(cursor.getColumnIndex(NotificationTable.NOTIFICATION_TYPE)),
                (cursor.getInt(cursor.getColumnIndex(NotificationTable.READ)) == 1),
                cursor.getLong(cursor.getColumnIndex(NotificationTable.GENERIC_NAME_ID)),
                cursor.getString(cursor.getColumnIndex(NotificationTable.SEVERITY)),
                cursor.getLong(cursor.getColumnIndex(NotificationTable.CREATION_DATE)),
                cursor.getString(cursor.getColumnIndex(NotificationTable.URL)),
                cursor.getLong(cursor.getColumnIndex(NotificationTable.DOCUMENT_ID)),
                cursor.getLong(cursor.getColumnIndex(NotificationTable.ICON)),
                cursor.getLong(cursor.getColumnIndex(NotificationTable.PRODUCT_ID)),
                cursor.getLong(cursor.getColumnIndex(NotificationTable.NOTIFICATION_ID)),
                cursor.getLong(cursor.getColumnIndex(NotificationTable.USER_ID)),
                cursor.getString(cursor.getColumnIndex(NotificationTable.USER_NAME)),
                cursor.getString(cursor.getColumnIndex(NotificationTable.HEADER)),
                cursor.getString(cursor.getColumnIndex(NotificationTable.NOTIFICATION_COLOR_CODE)));
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Long getGenericNameId() {
        return genericNameId;
    }

    public void setGenericNameId(Long genericNameId) {
        this.genericNameId = genericNameId;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(Long combinationId) {
        this.combinationId = combinationId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getNotificationColorCode() {
        return notificationColorCode;
    }

    public void setNotificationColorCode(String notificationColorCode) {
        this.notificationColorCode = notificationColorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return read == that.read &&
                Objects.equals(id, that.id) &&
                Objects.equals(icon, that.icon) &&
                Objects.equals(body, that.body) &&
                Objects.equals(title, that.title) &&
                Objects.equals(notificationType, that.notificationType) &&
                Objects.equals(genericNameId, that.genericNameId) &&
                Objects.equals(severity, that.severity) &&
                Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(url, that.url) &&
                Objects.equals(documentId, that.documentId) &&
                Objects.equals(combinationId, that.combinationId) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(notificationId, that.notificationId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(header, that.header) &&
                Objects.equals(notificationColorCode, that.notificationColorCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, icon, body, title, notificationType, read, genericNameId, severity, createdDate, url, documentId, combinationId, productId, notificationId, userId, userName, header, notificationColorCode);
    }
}


