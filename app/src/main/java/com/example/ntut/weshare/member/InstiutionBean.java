package com.example.ntut.weshare.member;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class InstiutionBean implements Serializable {

    private String indId;
    private String leader;
    private String orgType;
    private String registerNo;
    private String raiseNo;
    private String intRo;
    private Blob image = null;
    private java.sql.Timestamp updatetime = null;

    public InstiutionBean(String indId, String leader, String orgType, String registerNo, String raiseNo, String intRo, Timestamp updatetime) {
        this.indId = indId;
        this.leader = leader;
        this.orgType = orgType;
        this.registerNo = registerNo;
        this.raiseNo = raiseNo;
        this.intRo = intRo;
        this.updatetime = updatetime;
    }

    public String getIndId() {
        return indId;
    }

    public void setIndId(String indId) {
        this.indId = indId;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getRaiseNo() {
        return raiseNo;
    }

    public void setRaiseNo(String raiseNo) {
        this.raiseNo = raiseNo;
    }

    public String getIntRo() {
        return intRo;
    }

    public void setIntRo(String intRo) {
        this.intRo = intRo;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }
}

