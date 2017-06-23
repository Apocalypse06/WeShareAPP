package com.example.ntut.weshare.goods;


import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

public class Goods implements Serializable {
    private int goodsNo;
    private int goodsStatus;
    private java.sql.Timestamp updateTime;
    private String indId;
    private String goodsName;
    private int goodsType;
    private int goodsQty;
    private int goodsLoc;
    private String goodsNote;
    private int goodsShipWay;
    //    private Blob image = null;
    private java.sql.Timestamp deadLine;


    public Goods() {
    }


    public Goods(int goodsNo, int goodsStatus, Timestamp updateTime, String indId, String goodsName, int goodsType,
                 int goodsQty, int goodsLoc, String goodsNote, int goodsShipWay, Timestamp deadLine) {
        super();
        this.goodsNo = goodsNo;
        this.goodsStatus = goodsStatus;
        this.updateTime = updateTime;
        this.indId = indId;
        this.goodsName = goodsName;
        this.goodsType = goodsType;
        this.goodsQty = goodsQty;
        this.goodsLoc = goodsLoc;
        this.goodsNote = goodsNote;
        this.goodsShipWay = goodsShipWay;
        this.deadLine = deadLine;
    }

    public int getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(int goodsNo) {
        this.goodsNo = goodsNo;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public java.sql.Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(java.sql.Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getIndId() {
        return indId;
    }

    public void setIndId(String indId) {
        this.indId = indId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getGoodsQty() {
        return goodsQty;
    }

    public void setGoodsQty(int goodsQty) {
        this.goodsQty = goodsQty;
    }

    public int getGoodsLoc() {
        return goodsLoc;
    }

    public void setGoodsLoc(int goodsLoc) {
        this.goodsLoc = goodsLoc;
    }

    public String getGoodsNote() {
        return goodsNote;
    }

    public void setGoodsNote(String goodsNote) {
        this.goodsNote = goodsNote;
    }

    public int getGoodsShipWay() {
        return goodsShipWay;
    }

    public void setGoodsShipWay(int goodsShipWay) {
        this.goodsShipWay = goodsShipWay;
    }


    public java.sql.Timestamp getDeadTime() {
        return deadLine;
    }

    public void setDeadTime(java.sql.Timestamp deadLine) {
        this.deadLine = deadLine;
    }
}
