package com.boniu.shipinbofangqi.mvp.model.entity;

import java.io.Serializable;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-29 12:32
 */
public class BoNiuVideoInfo implements Serializable {
    private int boniu_video_id;
    private String boniu_video_name;
    private String boniu_video_coverimg;
    private String boniu_video_formatmemory;
    private String boniu_video_createtime;
    private double boniu_video_memory;
    private String boniu_video_url;
    private int boniu_video_folder_id;
    private String boniu_video_length;
    private String boniu_video_account;

    @Override
    public String toString() {
        return "BoNiuVideoInfo{" +
                "boniu_video_id=" + boniu_video_id +
                ", boniu_video_name='" + boniu_video_name + '\'' +
                ", boniu_video_coverimg='" + boniu_video_coverimg + '\'' +
                ", boniu_video_formatmemory='" + boniu_video_formatmemory + '\'' +
                ", boniu_video_createtime='" + boniu_video_createtime + '\'' +
                ", boniu_video_memory=" + boniu_video_memory +
                ", boniu_video_url='" + boniu_video_url + '\'' +
                ", boniu_video_folder_id=" + boniu_video_folder_id +
                ", boniu_video_length='" + boniu_video_length + '\'' +
                ", boniu_video_account='" + boniu_video_account + '\'' +
                '}';
    }

    public BoNiuVideoInfo(int boniu_video_id, String boniu_video_name, String boniu_video_coverimg, String boniu_video_formatmemory, String boniu_video_createtime, double boniu_video_memory, String boniu_video_url, int boniu_video_folder_id, String boniu_video_length, String boniu_video_account) {
        this.boniu_video_id = boniu_video_id;
        this.boniu_video_name = boniu_video_name;
        this.boniu_video_coverimg = boniu_video_coverimg;
        this.boniu_video_formatmemory = boniu_video_formatmemory;
        this.boniu_video_createtime = boniu_video_createtime;
        this.boniu_video_memory = boniu_video_memory;
        this.boniu_video_url = boniu_video_url;
        this.boniu_video_folder_id = boniu_video_folder_id;
        this.boniu_video_length = boniu_video_length;
        this.boniu_video_account = boniu_video_account;
    }

    public String getBoniu_video_length() {
        return boniu_video_length;
    }

    public void setBoniu_video_length(String boniu_video_length) {
        this.boniu_video_length = boniu_video_length;
    }

    public int getBoniu_video_id() {
        return boniu_video_id;
    }

    public void setBoniu_video_id(int boniu_video_id) {
        this.boniu_video_id = boniu_video_id;
    }

    public String getBoniu_video_name() {
        return boniu_video_name;
    }

    public void setBoniu_video_name(String boniu_video_name) {
        this.boniu_video_name = boniu_video_name;
    }

    public String getBoniu_video_coverimg() {
        return boniu_video_coverimg;
    }

    public void setBoniu_video_coverimg(String boniu_video_coverimg) {
        this.boniu_video_coverimg = boniu_video_coverimg;
    }

    public String getBoniu_video_formatmemory() {
        return boniu_video_formatmemory;
    }

    public void setBoniu_video_formatmemory(String boniu_video_formatmemory) {
        this.boniu_video_formatmemory = boniu_video_formatmemory;
    }

    public String getBoniu_video_createtime() {
        return boniu_video_createtime;
    }

    public void setBoniu_video_createtime(String boniu_video_createtime) {
        this.boniu_video_createtime = boniu_video_createtime;
    }

    public double getBoniu_video_memory() {
        return boniu_video_memory;
    }

    public void setBoniu_video_memory(double boniu_video_memory) {
        this.boniu_video_memory = boniu_video_memory;
    }

    public String getBoniu_video_url() {
        return boniu_video_url;
    }

    public void setBoniu_video_url(String boniu_video_url) {
        this.boniu_video_url = boniu_video_url;
    }

    public int getBoniu_video_folder_id() {
        return boniu_video_folder_id;
    }

    public void setBoniu_video_folder_id(int boniu_video_folder_id) {
        this.boniu_video_folder_id = boniu_video_folder_id;
    }

    public String getBoniu_video_account() {
        return boniu_video_account;
    }

    public void setBoniu_video_account(String boniu_video_account) {
        this.boniu_video_account = boniu_video_account;
    }

    public BoNiuVideoInfo() {
    }
}
