package org.mcs.entity;

/**
 * created by SunHongbin on 2018/10/20
 */

//该类用来取分贝的JSON数据
public class Value {

    private float dbCount;

    private float lastDbCount;

    private float min;

    private float value;

    private int avgOfDb;

    private int uploadDbValue;

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

    public int getAvgOfDb() {
        return avgOfDb;
    }

    public void setAvgOfDb(int avgOfDb) {
        this.avgOfDb = avgOfDb;
    }

    public int getUploadDbValue() {
        return uploadDbValue;
    }

    public void setUploadDbValue(int uploadDbValue) {
        this.uploadDbValue = uploadDbValue;
    }
}
