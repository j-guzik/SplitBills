package com.example.billssplitter;

public class ModelBills {
    String billId, billTitle, billCost, groupId, billPhoto, paidBy;

    public ModelBills(String billId, String billTitle, String billCost, String groupId, String billPhoto, String paidBy) {
        this.billId = billId;
        this.billTitle = billTitle;
        this.billCost = billCost;
        this.groupId = groupId;
        this.billPhoto = billPhoto;
        this.paidBy = paidBy;
    }

    public ModelBills() {
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillTitle() {
        return billTitle;
    }

    public void setBillTitle(String billTitle) {
        this.billTitle = billTitle;
    }

    public String getBillCost() {
        return billCost;
    }

    public void setBillCost(String billCost) {
        this.billCost = billCost;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getBillPhoto() {
        return billPhoto;
    }

    public void setBillPhoto(String billPhoto) {
        this.billPhoto = billPhoto;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }
}
