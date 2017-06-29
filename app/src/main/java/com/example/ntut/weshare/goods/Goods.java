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
    private int Qty;
    private int goodsLoc;
    private String goodsNote;
    private int goodsShipWay;
    private Blob goodsImage = null;
    private long deadLine;


    public Goods() {
    }

    public Goods(int goodsNo, int goodsStatus, Timestamp updateTime, String indId, String goodsName, int goodsType, int qty, int goodsLoc, String goodsNote, int goodsShipWay, Blob goodsImage, long deadLine) {
        this.goodsNo = goodsNo;
        this.goodsStatus = goodsStatus;
        this.updateTime = updateTime;
        this.indId = indId;
        this.goodsName = goodsName;
        this.goodsType = goodsType;
        Qty = qty;
        this.goodsLoc = goodsLoc;
        this.goodsNote = goodsNote;
        this.goodsShipWay = goodsShipWay;
        this.goodsImage = goodsImage;
        this.deadLine = deadLine;
    }

    public Goods(int goodsNo, int goodsStatus, Timestamp updateTime, String indId, String goodsName, int goodsType, int qty, int goodsLoc, String goodsNote, int goodsShipWay, long deadLine) {
        this.goodsNo = goodsNo;
        this.goodsStatus = goodsStatus;
        this.updateTime = updateTime;
        this.indId = indId;
        this.goodsName = goodsName;
        this.goodsType = goodsType;
        Qty = qty;
        this.goodsLoc = goodsLoc;
        this.goodsNote = goodsNote;
        this.goodsShipWay = goodsShipWay;
        this.deadLine = deadLine;
    }

    public Goods(int goodsNo, int goodsStatus, Timestamp updateTime, String goodsName, int goodsType, int qty, int goodsLoc, String goodsNote, int goodsShipWay, long deadLine) {
        this.goodsNo = goodsNo;
        this.goodsStatus = goodsStatus;
        this.updateTime = updateTime;
        this.goodsName = goodsName;
        this.goodsType = goodsType;
        Qty = qty;
        this.goodsLoc = goodsLoc;
        this.goodsNote = goodsNote;
        this.goodsShipWay = goodsShipWay;
        this.deadLine = deadLine;
    }

    public Goods(int goodsStatus, Timestamp updateTime, String indId, String goodsName, int goodsType, int qty, int goodsLoc, String goodsNote, int goodsShipWay, long deadLine) {
        this.goodsStatus = goodsStatus;
        this.updateTime = updateTime;
        this.indId = indId;
        this.goodsName = goodsName;
        this.goodsType = goodsType;
        Qty = qty;
        this.goodsLoc = goodsLoc;
        this.goodsNote = goodsNote;
        this.goodsShipWay = goodsShipWay;
        this.deadLine = deadLine;
    }

    public Goods(int goodsNo, int goodsStatus, String indId, String goodsName, int goodsType, int qty, int goodsLoc, String goodsNote, int goodsShipWay, long deadLine) {
        this.goodsNo = goodsNo;
        this.goodsStatus = goodsStatus;
        this.indId = indId;
        this.goodsName = goodsName;
        this.goodsType = goodsType;
        Qty = qty;
        this.goodsLoc = goodsLoc;
        this.goodsNote = goodsNote;
        this.goodsShipWay = goodsShipWay;
        this.deadLine = deadLine;
    }

    public int getGoodsNo() {
        return goodsNo;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public String getIndId() {
        return indId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public int getQty() {
        return Qty;
    }

    public int getGoodsLoc() {
        return goodsLoc;
    }

    public String getGoodsNote() {
        return goodsNote;
    }

    public int getGoodsShipWay() {
        return goodsShipWay;
    }

    public Blob getGoodsImage() {
        return goodsImage;
    }

    public long getDeadLine() {
        return deadLine;
    }

    public void setGoodsNo(int goodsNo) {
        this.goodsNo = goodsNo;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public void setIndId(String indId) {
        this.indId = indId;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public void setGoodsLoc(int goodsLoc) {
        this.goodsLoc = goodsLoc;
    }

    public void setGoodsNote(String goodsNote) {
        this.goodsNote = goodsNote;
    }

    public void setGoodsShipWay(int goodsShipWay) {
        this.goodsShipWay = goodsShipWay;
    }

    public void setGoodsImage(Blob goodsImage) {
        this.goodsImage = goodsImage;
    }

    public void setDeadLine(long deadLine) {
        this.deadLine = deadLine;
    }
}
