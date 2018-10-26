package org.mcs.service;

import org.mcs.entity.NoiseMessage;

import java.util.List;

/**
 * created by SunHongbin on 2018/10/20
 */
public interface CollectDb {

    //用户采集噪声数据后记录在数据库
    NoiseMessage createDbRecord(NoiseMessage record, Long userPhone);

    //查某一时间段的噪声地图
    List<NoiseMessage> formMap(NoiseMessage record);
}
