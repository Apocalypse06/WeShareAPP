package com.example.ntut.weshare.member;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class User implements Serializable {

    private String userId;
    private String password;
    private String name;
    private String tal;
    private String email;
    private String address;
    private Blob blob = null;
    private int idType = 1;
    private java.sql.Timestamp createDate;

    public User(String userId) {
        this.userId = userId;
    }

    public User(String userId, String password, String name, String tal, String email, String address) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.tal = tal;
        this.email = email;
        this.address = address;
    }

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public User(String name, String tal, String email, String address, int idType) {
        this.name = name;
        this.tal = tal;
        this.email = email;
        this.address = address;
        this.idType = idType;
    }

    public User(String userId, String tal, String email, String address) {
        this.userId = userId;
        this.tal = tal;
        this.email = email;
        this.address = address;
    }

    public User(String userId, String password, String tal, String email, String address) {
        this.userId = userId;
        this.password = password;
        this.tal = tal;
        this.email = email;
        this.address = address;
    }

    public User(String userId, String password, String name, String tal, String email, String address, int idType, Timestamp createDate) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.tal = tal;
        this.email = email;
        this.address = address;
        this.idType = idType;
        this.createDate = createDate;
    }

//    public User(String userId, String password, String name, String tal, String email, String address, Blob blob, int idType, Timestamp createDate, String fileName) {
//        this.userId = userId;
//        this.password = password;
//        this.name = name;
//        this.tal = tal;
//        this.email = email;
//        this.address = address;
//        this.blob = blob;
//        this.idType = idType;
//        this.createDate = createDate;
//        this.fileName = fileName;
//    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTal() {
        return tal;
    }

    public void setTal(String tal) {
        this.tal = tal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Blob getBlob() {
        return blob;
    }

    public void setBlob(Blob blob) {
        this.blob = blob;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
}

