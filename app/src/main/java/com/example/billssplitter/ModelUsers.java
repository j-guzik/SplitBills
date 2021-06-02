package com.example.billssplitter;

public class ModelUsers {
    String name, email, search, image, uid, paidOff;

    public ModelUsers(){
    }

    public ModelUsers(String name, String email, String search, String image, String uid, String paidOff) {
        this.name = name;
        this.email = email;
        this.search = search;
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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
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
