package com.sunhongbin.noiseDetect.Entity;

public class Value {

    private static float dbCount = 20;

    private static float lastDbCount = dbCount;

    private static float min = 0.5f;  //设置声音最低变化

    private static float value = 0;   // 声音分贝值

    //extra add

    private int avgOfDb;//3s内分贝的平均值

    private Integer uploadDbValue;//要上传的值，和avgOfDby一样，只不过分开处理

    private long collectTime;

    private long freshLctTime;

    private double longitude;

    private double latitude;

    private String userPhone;

    public static void setDbCount(float dbValue) {
        if (dbValue > lastDbCount) {
            value = dbValue - lastDbCount > min ? dbValue - lastDbCount : min;
        } else {
            value = dbValue - lastDbCount < -min ? dbValue - lastDbCount : -min;
        }
        dbCount = lastDbCount + value * 0.2f; //防止声音变化太快
        lastDbCount = dbCount;
    }

    public static float getDbCount() {
        return dbCount;
    }

    public int getAvgOfDb() {
        return avgOfDb;
    }

    public void setAvgOfDb(int avgOfDb) {
        this.avgOfDb = avgOfDb;
    }

    public Integer getUploadDbValue() {
        return uploadDbValue;
    }

    public void setUploadDbValue(Integer uploadDbValue) {
        this.uploadDbValue = uploadDbValue;
    }

    public long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
    }

    public long getFreshLctTime() {
        return freshLctTime;
    }

    public void setFreshLctTime(long freshLctTime) {
        this.freshLctTime = freshLctTime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
