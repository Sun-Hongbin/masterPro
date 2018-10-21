package org.mcs.entity;

/**
 * created by SunHongbin on 2018/10/20
 */

//该类用来取APP传来的JSON数据，与NosiseMessage基本重复，暂未找到解决办法
public class Value {

    private float dbCount;

    private float lastDbCount;

    private float min;

    private float value;

    private Integer avgOfDb;

    private Integer uploadDbValue;

    private Long collectTime;

    private Long uploadTime;

    private double longtitude;

    private double latitude;

    public float getDbCount() {
        return dbCount;
    }

    public void setDbCount(float dbCount) {
        this.dbCount = dbCount;
    }

    public float getLastDbCount() {
        return lastDbCount;
    }

    public void setLastDbCount(float lastDbCount) {
        this.lastDbCount = lastDbCount;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Integer getAvgOfDb() {
        return avgOfDb;
    }

    public void setAvgOfDb(Integer avgOfDb) {
        this.avgOfDb = avgOfDb;
    }

    public Integer getUploadDbValue() {
        return uploadDbValue;
    }

    public void setUploadDbValue(Integer uploadDbValue) {
        this.uploadDbValue = uploadDbValue;
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
}
