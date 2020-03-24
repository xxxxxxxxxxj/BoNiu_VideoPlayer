package com.duyin.quickscan.baen;

/**
 * 作者：zhangzhongping on 17/2/17 16:50
 * 邮箱：android_dy@163.com
 */
public class ScanResult {
    private String name;
    private String path;
    private String id;
    private String size;
    private String date_added;
    private String date_modified;

    @Override
    public String toString() {
        return "ScanResult{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", id='" + id + '\'' +
                ", size='" + size + '\'' +
                ", date_added='" + date_added + '\'' +
                ", date_modified='" + date_modified + '\'' +
                '}';
    }

    public ScanResult(String name, String path, String id, String size, String date_added, String date_modified) {
        this.name = name;
        this.path = path;
        this.id = id;
        this.size = size;
        this.date_added = date_added;
        this.date_modified = date_modified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
