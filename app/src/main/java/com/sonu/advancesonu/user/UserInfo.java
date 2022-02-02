package com.sonu.advancesonu.user;

public class UserInfo {
    private String userName;
    private String userEmail;
    private String userImage;
    private String userMob;
    private String address;
    private String pin;
    private String cableOperator;

    public UserInfo(){}

    public UserInfo(String userName, String userEmail, String userImage, String userMob) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userImage = userImage;
        this.userMob = userMob;
    }


    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserMob() {
        return userMob;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setUserMob(String userMob) {
        this.userMob = userMob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCableOperator() {
        return cableOperator;
    }

    public void setCableOperator(String cableOperator) {
        this.cableOperator = cableOperator;
    }
}
