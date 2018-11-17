package org.mcs.service;

import org.mcs.entity.TaskRecord;

import java.util.Date;
import java.util.List;

/**
 * created by SunHongbin on 2018/10/29
 */
public interface TaskService {

    //任务发布者发布一条任务
    TaskRecord create(TaskRecord taskRecord, Long userPhone);

    //根据Id删除一条任务
    Boolean removeById(Long id);

    //根据传入的信息动态的进行编辑更新
    Boolean changeByIdSelective(TaskRecord taskRecord);

    //根据时间段获取业务信息
    TaskRecord getByTime(Long minTime, Long maxTime);

    //根据任务所在地点获取任务
    List<TaskRecord> getByLocation(Double longitude, Double latitude);

    //TODO 根据任务标签获取任务
    List<TaskRecord> getByTags(int tagNumbs);

}
