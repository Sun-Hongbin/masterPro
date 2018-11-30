package org.mcs.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private Date taskStartTime;

    private Date taskEndTime;

    //extra message: back to front end needed

    private Long userPhone;

    private String name;//userName

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

    public Date getTaskStartTime() {
        return taskStartTime;
    }



    public void setTaskStartTime(String taskStartTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.taskStartTime = dateFormat.parse(taskStartTime);
        } catch (ParseException e) {
            this.taskStartTime = null;
        }
    }

    public Date getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.taskEndTime = dateFormat.parse(taskEndTime);
        } catch (ParseException e) {
            this.taskEndTime = null;
        }
    }

    public Long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(Long userPhone) {
        this.userPhone = userPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", userPhone=" + userPhone +
                ", name='" + name + '\'' +
                '}';
    }
}
