package com.boniu.shipinbofangqi.mvp.model.entity;

import java.io.Serializable;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-29 13:00
 */
public class BoNiuFolderInfo implements Serializable {
    private int boniu_folder_id;
    private String boniu_folder_name;
    private String boniu_folder_formatmemory = "0.00M";
    private double boniu_folder_memory;
    private String boniu_folder_createtime;
    private int boniu_folder_isdefault;
    private String boniu_folder_account;

    @Override
    public String toString() {
        return "BoNiuFolderInfo{" +
                "boniu_folder_id=" + boniu_folder_id +
                ", boniu_folder_name='" + boniu_folder_name + '\'' +
                ", boniu_folder_formatmemory='" + boniu_folder_formatmemory + '\'' +
                ", boniu_folder_memory=" + boniu_folder_memory +
                ", boniu_folder_createtime='" + boniu_folder_createtime + '\'' +
                ", boniu_folder_isdefault=" + boniu_folder_isdefault +
                ", boniu_folder_account=" + boniu_folder_account +
                '}';
    }

    public BoNiuFolderInfo() {
    }

    public BoNiuFolderInfo(int boniu_folder_id, String boniu_folder_name, String boniu_folder_formatmemory, double boniu_folder_memory, String boniu_folder_createtime, int boniu_folder_isdefault, String boniu_folder_account) {
        this.boniu_folder_id = boniu_folder_id;
        this.boniu_folder_name = boniu_folder_name;
        this.boniu_folder_formatmemory = boniu_folder_formatmemory;
        this.boniu_folder_memory = boniu_folder_memory;
        this.boniu_folder_createtime = boniu_folder_createtime;
        this.boniu_folder_isdefault = boniu_folder_isdefault;
        this.boniu_folder_account = boniu_folder_account;
    }

    public BoNiuFolderInfo(String boniu_folder_name, String boniu_folder_createtime, int boniu_folder_isdefault, String boniu_folder_account) {
        this.boniu_folder_name = boniu_folder_name;
        this.boniu_folder_createtime = boniu_folder_createtime;
        this.boniu_folder_isdefault = boniu_folder_isdefault;
        this.boniu_folder_account = boniu_folder_account;
    }

    public BoNiuFolderInfo(String boniu_folder_name, String boniu_folder_createtime, String boniu_folder_account) {
        this.boniu_folder_name = boniu_folder_name;
        this.boniu_folder_createtime = boniu_folder_createtime;
        this.boniu_folder_account = boniu_folder_account;
    }

    public int getBoniu_folder_id() {
        return boniu_folder_id;
    }

    public void setBoniu_folder_id(int boniu_folder_id) {
        this.boniu_folder_id = boniu_folder_id;
    }

    public String getBoniu_folder_name() {
        return boniu_folder_name;
    }

    public void setBoniu_folder_name(String boniu_folder_name) {
        this.boniu_folder_name = boniu_folder_name;
    }

    public String getBoniu_folder_formatmemory() {
        return boniu_folder_formatmemory;
    }

    public void setBoniu_folder_formatmemory(String boniu_folder_formatmemory) {
        this.boniu_folder_formatmemory = boniu_folder_formatmemory;
    }

    public double getBoniu_folder_memory() {
        return boniu_folder_memory;
    }

    public void setBoniu_folder_memory(double boniu_folder_memory) {
        this.boniu_folder_memory = boniu_folder_memory;
    }

    public int getBoniu_folder_isdefault() {
        return boniu_folder_isdefault;
    }

    public void setBoniu_folder_isdefault(int boniu_folder_isdefault) {
        this.boniu_folder_isdefault = boniu_folder_isdefault;
    }

    public String getBoniu_folder_account() {
        return boniu_folder_account;
    }

    public void setBoniu_folder_account(String boniu_folder_account) {
        this.boniu_folder_account = boniu_folder_account;
    }

    public String getBoniu_folder_createtime() {
        return boniu_folder_createtime;
    }

    public void setBoniu_folder_createtime(String boniu_folder_createtime) {
        this.boniu_folder_createtime = boniu_folder_createtime;
    }
}
