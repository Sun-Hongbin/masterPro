package org.mcs.entity;

import java.util.Date;

/**
 * created by SunHongbin on 2018/10/15
 */
public class NoiseMessage {

    private Long userId;

    private Integer db;

    private double longtitude;

    private double latitude;

    private Long collectTime;

    private Long uploadTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getDb() {
        return db;
    }

    public void setDb(Integer db) {
        this.db = db;
    }

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }

    public Long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "NoiseMessage{" +
                "userId=" + userId +
                ", db=" + db +
                ", collectTime=" + collectTime +
                ", uploadTime=" + uploadTime +
                '}';
    }
}
