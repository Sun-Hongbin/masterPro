package org.mcs.entity;

import java.util.Date;

/**
 * created by SunHongbin on 2018/10/15
 */
public class NoiseMessage {

    private Long userId;

    private Integer db;

    private Date createTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getDb() {
        return db;
    }

    public void setDb(Integer db) {
        this.db = db;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "NoiseMessage{" +
                "userId=" + userId +
                ", db=" + db +
                ", createTime=" + createTime +
                '}';
    }
}
