package com.warehouse.system;

import java.sql.Timestamp;

public class InOut {
    private int id;
    private String inoutNo;
    private Timestamp inoutDate;
    private String operatorCode;
    private String remark;
    private int mmid;
    private int inQuantity;
    private int outQuantity;



    public InOut(String inoutNo, String operatorCode, int mmid) {
        this.inoutNo = inoutNo;
        this.operatorCode = operatorCode;
        this.mmid = mmid;
        this.inoutDate = new Timestamp(System.currentTimeMillis());
    }
    // getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInoutNo() {
        return inoutNo;
    }

    public void setInoutNo(String inoutNo) {
        this.inoutNo = inoutNo;
    }

    public Timestamp getInoutDate() {
        return inoutDate;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getMmid() {
        return mmid;
    }

    public void setMmid(int mmid) {
        this.mmid = mmid;
    }

    public int getInQuantity() {
        return inQuantity;
    }

    public void setInQuantity(Integer inQuantity) {
        this.inQuantity = inQuantity;
    }

    public Integer getOutQuantity() {
        return outQuantity;
    }

    public void setOutQuantity(Integer outQuantity) {
        this.outQuantity = outQuantity;
    }
}