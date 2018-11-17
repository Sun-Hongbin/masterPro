package org.mcs.entity;

import java.util.Date;

/**
 * created by SunHongbin on 2018/10/26
 */
public class TaskRecord {

    private Long id;

    private Long publisherId;

    private Long ctime;

    private Long utime;

    private String taskDescription;

    private String taskLocation;//TODO 这里传的值应该是在APP上地图检索后获得的经纬度值，这里为检索的字符串

    private Double taskLatitude;

    private Double taskLongitude;

    private Date taskExecuteTime;//TODO 这里应该是一个范围

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

    public Date getTaskExecuteTime() {
        return taskExecuteTime;
    }

    public void setTaskExecuteTime(Date taskExecuteTime) {
        this.taskExecuteTime = taskExecuteTime;
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
                ", taskExecuteTime=" + taskExecuteTime +
                '}';
    }
}
