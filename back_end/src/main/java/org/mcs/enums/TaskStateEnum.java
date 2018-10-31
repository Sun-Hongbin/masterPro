package org.mcs.enums;

/**
 * created by SunHongbin on 2018/10/31
 */
public enum TaskStateEnum {

    //TASK
    TASK_PUBLISH_SUCCESS(2,"任务发布成功"),
    TASK_UPDATE_SUCCESS(1,"任务更新成功"),
    INNER_ERROR(-1,"系统异常"),
    DATA_REWRITE(-2,"数据篡改");

    private int state;

    private String stateInfo;

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    TaskStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static TaskStateEnum stateOf(int index) {
        for (TaskStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
