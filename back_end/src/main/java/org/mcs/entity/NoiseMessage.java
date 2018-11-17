package org.mcs.entity;

import java.util.Arrays;
import java.util.Date;

/**
 * created by SunHongbin on 2018/10/15
 */
public class NoiseMessage {

    private Long id;//TODO 用多线程确保资源自增顺利

    private Long userId;

    private Integer db;

    private byte[] mp3File;

    private Double longitude;

    private Double latitude;

    private Long collectTime;

    private Long uploadTime;

    private Long taskId;//参加的是哪一条任务，可以为null

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public byte[] getMp3File() {
        return mp3File;
    }

    public void setMp3File(byte[] mp3File) {
        this.mp3File = mp3File;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "NoiseMessage{" +
                "id=" + id +
                ", userId=" + userId +
                ", db=" + db +
                ", mp3File=" + Arrays.toString(mp3File) +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", collectTime=" + collectTime +
                ", uploadTime=" + uploadTime +
                ", taskId=" + taskId +
                '}';
    }
}
