package org.mcs.dao;

import org.apache.ibatis.annotations.Param;
import org.mcs.entity.User;

import java.util.List;

/**
 * created by SunHongbin on 2018/10/15
 */
public interface UserDao {

    int insert(User user);

    int deleteByPrimaryKey(Long id);

    User SelectSelective(User user);

}
