package org.mcs.dao;

import org.mcs.entity.NoiseMessage;

import java.util.Date;
import java.util.List;

/**
 * created by SunHongbin on 2018/10/15
 */
public interface DbFileDao {

    int insert(NoiseMessage record);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Long id, Date updateTime);

    List<NoiseMessage> querySelective(NoiseMessage record);
}
