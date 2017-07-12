package com.example.ntut.weshare.homeGoodsDetail;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class FeedbackBean implements Serializable {
    private int dealNo;
    private Timestamp postDate;
    private String fbSourceId;
    private String fbEndId;
    private String fbText;
    private int fbScore;
    private Blob fbImage;
    private String fbFileName;

    public FeedbackBean(int dealNo, String fbSourceId, String fbEndId, String fbText, int fbScore, String fbFileName) {
        this.dealNo = dealNo;
        this.fbSourceId = fbSourceId;
        this.fbEndId = fbEndId;
        this.fbText = fbText;
        this.fbScore = fbScore;
        this.fbFileName = fbFileName;
    }

    public FeedbackBean(int dealNo, Timestamp postDate, String fbSourceId, String fbEndId, String fbText, int fbScore, Blob fbImage, String fbFileName) {
        this.dealNo = dealNo;
        this.postDate = postDate;
        this.fbSourceId = fbSourceId;
        this.fbEndId = fbEndId;
        this.fbText = fbText;
        this.fbScore = fbScore;
        this.fbImage = fbImage;
        this.fbFileName = fbFileName;
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

    public String getFbSourceId() {
        return fbSourceId;
    }

    public void setFbSourceId(String fbSourceId) {
        this.fbSourceId = fbSourceId;
    }

    public String getFbEndId() {
        return fbEndId;
    }

    public void setFbEndId(String fbEndId) {
        this.fbEndId = fbEndId;
    }

    public String getFbText() {
        return fbText;
    }

    public void setFbText(String fbText) {
        this.fbText = fbText;
    }

    public int getFbScore() {
        return fbScore;
    }

    public void setFbScore(int fbScore) {
        this.fbScore = fbScore;
    }

    public Blob getFbImage() {
        return fbImage;
    }

    public void setFbImage(Blob fbImage) {
        this.fbImage = fbImage;
    }

    public String getFbFileName() {
        return fbFileName;
    }

    public void setFbFileName(String fbFileName) {
        this.fbFileName = fbFileName;
    }
}

