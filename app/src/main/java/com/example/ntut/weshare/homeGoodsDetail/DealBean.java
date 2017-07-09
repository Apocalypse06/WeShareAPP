package com.example.ntut.weshare.homeGoodsDetail;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class DealBean implements Serializable {
    private int dealNo;
    private java.sql.Timestamp postDate;
    private String sourceId;
    private String endId;
    private int dealStatus;
    private int endShipWay;
    private String shipNo;
    private String dealNote;


    private java.sql.Timestamp shipDate;

    private String goodsName;
    private int dealQty;
    private java.sql.Blob goodsImage;
    private String goodsImageName;
    private int goodstype;
    private int goodsLoc;
    private String goodsNote;

    public DealBean(Timestamp postDate, String sourceId, String endId, int dealStatus, int endShipWay, String shipNo, Timestamp shipDate, String goodsName, int dealQty, String goodsImageName, int goodstype, int goodsLoc, String goodsNote, String dealNote) {
        this.postDate = postDate;
        this.sourceId = sourceId;
        this.endId = endId;
        this.dealStatus = dealStatus;
        this.endShipWay = endShipWay;
        this.shipNo = shipNo;
        this.shipDate = shipDate;
        this.goodsName = goodsName;
        this.dealQty = dealQty;
        this.goodsImageName = goodsImageName;
        this.goodstype = goodstype;
        this.goodsLoc = goodsLoc;
        this.goodsNote = goodsNote;
        this.dealNote = dealNote;
    }

    public DealBean() {
    }

    public DealBean(int dealNo, Timestamp postDate, String sourceId, String endId, int dealStatus, int endShipWay, String shipNo, Timestamp shipDate, String goodsName, int dealQty, java.sql.Blob goodsImage, String goodsImageName, int goodstype, int goodsLoc, String goodsNote) {
        this.dealNo = dealNo;
        this.postDate = postDate;
        this.sourceId = sourceId;
        this.endId = endId;
        this.dealStatus = dealStatus;
        this.endShipWay = endShipWay;
        this.shipNo = shipNo;
        this.shipDate = shipDate;
        this.goodsName = goodsName;
        this.dealQty = dealQty;
        this.goodsImage = goodsImage;
        this.goodsImageName = goodsImageName;
        this.goodstype = goodstype;
        this.goodsLoc = goodsLoc;
        this.goodsNote = goodsNote;
    }

    public DealBean(int dealNo, Timestamp postDate, String sourceId, String endId, int dealStatus, int endShipWay, String shipNo, String dealNote, Timestamp shipDate, String goodsName, int dealQty, Blob goodsImage, String goodsImageName, int goodstype, int goodsLoc, String goodsNote) {
        this.dealNo = dealNo;
        this.postDate = postDate;
        this.sourceId = sourceId;
        this.endId = endId;
        this.dealStatus = dealStatus;
        this.endShipWay = endShipWay;
        this.shipNo = shipNo;
        this.dealNote = dealNote;
        this.shipDate = shipDate;
        this.goodsName = goodsName;
        this.dealQty = dealQty;
        this.goodsImage = goodsImage;
        this.goodsImageName = goodsImageName;
        this.goodstype = goodstype;
        this.goodsLoc = goodsLoc;
        this.goodsNote = goodsNote;
    }

    public int getDealNo() {
        return dealNo;
    }

    public void setDealNo(int dealNo) {
        this.dealNo = dealNo;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    public void setPostDate(Timestamp postDate) {
        this.postDate = postDate;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getEndId() {
        return endId;
    }

    public void setEndId(String endId) {
        this.endId = endId;
    }

    public int getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(int dealStatus) {
        this.dealStatus = dealStatus;
    }

    public int getEndShipWay() {
        return endShipWay;
    }

    public void setEndShipWay(int endShipWay) {
        this.endShipWay = endShipWay;
    }

    public String getShipNo() {
        return shipNo;
    }

    public void setShipNo(String shipNo) {
        this.shipNo = shipNo;
    }

    public Timestamp getShipDate() {
        return shipDate;
    }

    public void setShipDate(Timestamp shipDate) {
        this.shipDate = shipDate;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getDealQty() {
        return dealQty;
    }

    public void setDealQty(int dealQty) {
        this.dealQty = dealQty;
    }

    public Blob getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(Blob goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsImageName() {
        return goodsImageName;
    }

    public void setGoodsImageName(String goodsImageName) {
        this.goodsImageName = goodsImageName;
    }

    public int getGoodstype() {
        return goodstype;
    }

    public void setGoodstype(int goodstype) {
        this.goodstype = goodstype;
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

    public String getDealNote() {
        return dealNote;
    }

    public void setDealNote(String dealNote) {
        this.dealNote = dealNote;
    }
}

