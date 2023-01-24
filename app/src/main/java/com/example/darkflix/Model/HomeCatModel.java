package com.example.darkflix.Model;

public class HomeCatModel {
    private String listTitle;
    private String listType;
    private String rqeType;
    private String reqState;
    private String orgLang;
    private String reqRegion;

    public HomeCatModel(String listTitle, String listType, String rqeType, String reqState) {
        this.listTitle = listTitle;
        this.listType = listType;
        this.rqeType = rqeType;
        this.reqState = reqState;
    }

    public HomeCatModel(String listTitle, String listType, String rqeType, String reqState, String orgLang, String reqRegion) {
        this.listTitle = listTitle;
        this.listType = listType;
        this.rqeType = rqeType;
        this.reqState = reqState;
        this.orgLang = orgLang;
        this.reqRegion = reqRegion;
    }

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getRqeType() {
        return rqeType;
    }

    public void setRqeType(String rqeType) {
        this.rqeType = rqeType;
    }

    public String getReqState() {
        return reqState;
    }

    public void setReqState(String reqState) {
        this.reqState = reqState;
    }

    public String getOrgLang() { return orgLang; }

    public void setOrgLang(String orgLang) { this.orgLang = orgLang; }

    public String getReqRegion() { return reqRegion; }

    public void setReqRegion(String reqRegion) { this.reqRegion = reqRegion; }
}
