package com.example.billssplitter;

public class ModelBills {
    String billId, billTitle, billCost;

    public ModelBills(String billId, String billTitle, String billCost) {
        this.billId = billId;
        this.billTitle = billTitle;
        this.billCost = billCost;
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
}
