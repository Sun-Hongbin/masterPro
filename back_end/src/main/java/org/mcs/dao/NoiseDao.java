package org.mcs.dao;

import org.apache.ibatis.annotations.Param;
import org.mcs.entity.NoiseMessage;

import java.util.Date;
import java.util.List;

/**
 * created by SunHongbin on 2018/10/15
 */
public interface NoiseDao {

    int insert(NoiseMessage record);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Long id, Date updateTime);

    List<NoiseMessage> querySelective(NoiseMessage noiseMessage);

    //extra mappers

    List<NoiseMessage> queryByRangeOfTime(@Param("minTime") Long min, @Param("maxTime") Long max, @Param("taskId") Long taskId);
}
