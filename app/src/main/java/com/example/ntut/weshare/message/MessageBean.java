package com.example.ntut.weshare.message;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class MessageBean implements Serializable {

    private int msgNo;
    private int msgStatus;
    private Timestamp postDate = null;
    private String msgSourceId;
    private String msgEndId;
    private String msgText;
    private Blob msgImage = null;
    private String msgFileName = "msgFileName";
    private int roomNo;


    public MessageBean() {
    }

    public MessageBean(int msgNo, int msgStatus, Timestamp postDate, String msgSourceId, String msgEndId, String msgText) {
        this.msgNo = msgNo;
        this.msgStatus = msgStatus;
        this.postDate = postDate;
        this.msgSourceId = msgSourceId;
        this.msgEndId = msgEndId;
        this.msgText = msgText;
        this.roomNo = roomNo;
    }

    public MessageBean(int msgNo, int msgStatus, String msgSourceId, String msgEndId, String msgText, int roomNo) {
        this.msgNo = msgNo;
        this.msgStatus = msgStatus;
        this.msgSourceId = msgSourceId;
        this.msgEndId = msgEndId;
        this.msgText = msgText;
        this.roomNo = roomNo;
    }

    public MessageBean(int msgNo, int msgStatus, String msgSourceId, String msgEndId, String msgText, String msgFileName) {
        this.msgNo = msgNo;
        this.msgStatus = msgStatus;
        this.msgSourceId = msgSourceId;
        this.msgEndId = msgEndId;
        this.msgText = msgText;
        this.msgFileName = msgFileName;
    }

    public MessageBean(String msgText, int msgNo, int msgStatus, Timestamp postDate, String msgSourceId, String msgEndId) {
        this.msgText = msgText;
        this.msgNo = msgNo;
        this.msgStatus = msgStatus;
        this.postDate = postDate;
        this.msgSourceId = msgSourceId;
        this.msgEndId = msgEndId;
    }

    public int getMsgNo() {
        return msgNo;
    }

    public void setMsgNo(int msgNo) {
        this.msgNo = msgNo;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    public void setPostDate(Timestamp postDate) {
        this.postDate = postDate;
    }

    public String getMsgSourceId() {
        return msgSourceId;
    }

    public void setMsgSourceId(String msgSourceId) {
        this.msgSourceId = msgSourceId;
    }

    public String getMsgEndId() {
        return msgEndId;
    }

    public void setMsgEndId(String msgEndId) {
        this.msgEndId = msgEndId;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public Blob getMsgImage() {
        return msgImage;
    }

    public void setMsgImage(Blob msgImage) {
        this.msgImage = msgImage;
    }

    public String getMsgFileName() {
        return msgFileName;
    }

    public void setMsgFileName(String msgFileName) {
        this.msgFileName = msgFileName;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }
}

