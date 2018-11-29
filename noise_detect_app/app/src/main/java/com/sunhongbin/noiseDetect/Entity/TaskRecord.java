package com.sunhongbin.noiseDetect.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * created by SunHongbin on 2018/10/26
 */
public class TaskRecord implements Parcelable {

    private Long id;

    private Long publisherId;

    private Long ctime;

    private Long utime;

    private String taskDescription;

    private String taskLocation;//TODO 这里传的值应该是在APP上地图检索后获得的经纬度值，这里为检索的字符串

    private Double taskLatitude;

    private Double taskLongitude;

    private String taskStartTime;

    private String taskEndTime;

    private Long userPhone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getUtime() {
        return utime;
    }

    public void setUtime(Long utime) {
        this.utime = utime;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskLocation() {
        return taskLocation;
    }

    public void setTaskLocation(String taskLocation) {
        this.taskLocation = taskLocation;
    }

    public Double getTaskLatitude() {
        return taskLatitude;
    }

    public void setTaskLatitude(Double taskLatitude) {
        this.taskLatitude = taskLatitude;
    }

    public Double getTaskLongitude() {
        return taskLongitude;
    }

    public void setTaskLongitude(Double taskLongitude) {
        this.taskLongitude = taskLongitude;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public Long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(Long userPhone) {
        this.userPhone = userPhone;
    }

    @Override
    public String toString() {
        return "TaskRecord{" +
                "id=" + id +
                ", publisherId=" + publisherId +
                ", ctime=" + ctime +
                ", utime=" + utime +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskLocation='" + taskLocation + '\'' +
                ", taskLatitude=" + taskLatitude +
                ", taskLongitude=" + taskLongitude +
                ", taskStartTime=" + taskStartTime +
                ", taskEndTime=" + taskEndTime +
                ", userPhone='" + userPhone + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.taskDescription);
        dest.writeString(this.taskLocation);
        dest.writeString(this.taskStartTime);
    }

    public static final Creator<TaskRecord> CREATOR = new Creator<TaskRecord>() {
        @Override
        public TaskRecord createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public TaskRecord[] newArray(int size) {
            return new TaskRecord[size];
        }
    };
}



























































