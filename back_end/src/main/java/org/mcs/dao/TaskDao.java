package org.mcs.dao;

import org.mcs.entity.TaskRecord;

import java.util.List;

/**
 * created by SunHongbin on 2018/10/26
 */
public interface TaskDao {

    int insert(TaskRecord record);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TaskRecord record);

    List<TaskRecord> querySelective(TaskRecord record);

    List<TaskRecord> queryByRangeOfTime(Long minTime, Long maxTime);
}
