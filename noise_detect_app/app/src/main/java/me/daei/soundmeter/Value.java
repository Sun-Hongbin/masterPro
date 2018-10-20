package me.daei.soundmeter;

public class Value {

	private static float dbCount = 20;

	private static float lastDbCount = dbCount;

	private static float min = 0.5f;  //设置声音最低变化

	private static float value = 0;   // 声音分贝值

	private int avgOfDb;//3s内分贝的平均值

    private int uploadDbValue;//要上传的值，和avgOfDby一样，只不过分开处理

	public static void setDbCount(float dbValue) {
		if (dbValue > lastDbCount) {
			value = dbValue - lastDbCount > min ? dbValue - lastDbCount : min;
		}else{
			value = dbValue - lastDbCount < -min ? dbValue - lastDbCount : -min;
		}
		dbCount = lastDbCount + value * 0.2f ; //防止声音变化太快
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

    public int getUploadDbValue() {
		return uploadDbValue;
    }

    public void setUploadDbValue(int uploadDbValue) {
        this.uploadDbValue = uploadDbValue;
    }
}
