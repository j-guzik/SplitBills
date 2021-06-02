package com.example.billssplitter;

public class ModelDebtor {

        String name, email, image, uid, paidOff;

        public ModelDebtor(){
        }

    public ModelDebtor(String name, String email, String image, String uid, String paidOff) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.uid = uid;
        this.paidOff = paidOff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPaidOff() {
        return paidOff;
    }

    public void setPaidOff(String paidOff) {
        this.paidOff = paidOff;
    }
}
