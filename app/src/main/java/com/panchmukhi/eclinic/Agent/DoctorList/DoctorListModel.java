package com.panchmukhi.eclinic.Agent.DoctorList;

public class DoctorListModel {

    String uid,imageUrl,name,specialist,degree,userNotificationToken;
    int onlineStatus;

    public DoctorListModel(String uid, String imageUrl, String name, String specialist, String degree,int onlineStatus,String userNotificationToken) {
        this.uid = uid;
        this.imageUrl = imageUrl;
        this.name = name;
        this.specialist = specialist;
        this.degree = degree;
        this.onlineStatus=onlineStatus;
        this.userNotificationToken=userNotificationToken;
    }

    public String getUserNotificationToken() {
        return userNotificationToken;
    }

    public void setUserNotificationToken(String userNotificationToken) {
        this.userNotificationToken = userNotificationToken;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
