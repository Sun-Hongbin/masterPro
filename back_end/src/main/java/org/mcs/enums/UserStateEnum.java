package org.mcs.enums;

/**
 * created by SunHongbin on 2018/10/31
 */
public enum  UserStateEnum {

    IN_USER((byte) 1),
    FORBIDDEN((byte) 0);

    byte state;

    public int getState(){
        return state;
    }

    UserStateEnum(byte state) {
        this.state = state;
    }
}
