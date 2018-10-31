package org.mcs.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.mcs.dao.TaskDao;
import org.mcs.dao.UserDao;
import org.mcs.entity.TaskRecord;
import org.mcs.entity.User;
import org.mcs.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * created by SunHongbin on 2018/10/29
 */
@Service
public class taskServiceImpl implements TaskService {

    @Resource
    private TaskDao taskDao;

    @Resource
    private UserDao userDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public TaskRecord create(TaskRecord taskRecord, Long userPhone) {
        //1、验证关键参数是否规范
        if (taskRecord.getTaskExecuteTime() == null || StringUtils.isBlank(taskRecord.getTaskDescription()) ||
                StringUtils.isBlank(taskRecord.getTaskLocation())) {
            logger.error("TaskService - create parameters error : ", taskRecord);
            return null;
        }
        //2、插入数据库
        try {
            User user = new User();
            user.setUserPhone(userPhone);
            Long publisherId = userDao.selectSelective(user).get(0).getId();
            taskRecord.setPublisherId(publisherId);
            taskRecord.setCtime(System.currentTimeMillis());
            taskDao.insert(taskRecord);
        } catch (Exception e) {
            logger.error("TaskService - create error : ", e);
        }
        return null;
    }

    @Override
    public Boolean removeById(Long id) {
        try {
            if (taskDao.deleteByPrimaryKey(id) == 1)
                return true;
        } catch (Exception e) {
            logger.error("TaskService -removeById error: ", e);
        }
        return false;
    }

    @Override
    public Boolean changeByIdSelective(TaskRecord taskRecord) {
        if (taskRecord.getPublisherId() == null ||
                taskRecord.getTaskExecuteTime() == null ||
                StringUtils.isBlank(taskRecord.getTaskDescription()) ||
                StringUtils.isBlank(taskRecord.getTaskLocation())) {
            logger.error("changeByIdSelective lack of params");
            return false;
        }
        try {
            long updateTime = System.currentTimeMillis();
            taskRecord.setUtime(updateTime);
            return taskDao.updateByPrimaryKeySelective(taskRecord) == 1;
        } catch (Exception e) {
            logger.error("TaskService - changeByIdSelective error：" + e);
        }
        return false;
    }

    @Override
    public TaskRecord getByTime(Long time, Long range) {
        try {
            taskDao.queryByRangeOfTime(time, range);
        } catch (Exception e) {
            logger.error("TaskService - getByTime: ", e);
        }
        return null;
    }

    @Override
    public List<TaskRecord> getByLocation(Double longtitude, Double latitude) {
        return null;
    }

    @Override
    public List<TaskRecord> getByTags(int tagNumbs) {
        return null;
    }
}
