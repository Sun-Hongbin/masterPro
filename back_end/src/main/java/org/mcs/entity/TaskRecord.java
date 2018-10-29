package org.mcs.entity;

/**
 * created by SunHongbin on 2018/10/26
 */
public class TaskRecord {

    private Long id;

    private Long publisherId;

    private Long ctime;

    private Long utime;

    private String taskDescription;

    private String taskLocation;

    private Long taskExecuteTime;

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

    public Long getTaskExecuteTime() {
        return taskExecuteTime;
    }

    public void setTaskExecuteTime(Long taskExecuteTime) {
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
                ", taskExecuteTime=" + taskExecuteTime +
                '}';
    }
}
