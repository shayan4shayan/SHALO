package com.syfract.sabt.download;

/**
 * Created by shayan on 12/14/2017.
 */

public class DownloadCache {
    private String url,path;
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }
}
