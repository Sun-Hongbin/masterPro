package org.mcs.entity;

/**
 * created by SunHongbin on 2018/10/26
 */
public class TaskRecord {

    private Long id;

    private Long publisherId;

    private Long publishTime;

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

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
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
                ", publishTime=" + publishTime +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskLocation='" + taskLocation + '\'' +
                ", taskExecuteTime=" + taskExecuteTime +
                '}';
    }
}
