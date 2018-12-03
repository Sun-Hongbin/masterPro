package org.mcs.service.impl;

import org.mcs.dao.NoiseDao;
import org.mcs.dao.UserDao;
import org.mcs.entity.NoiseMessage;
import org.mcs.entity.User;
import org.mcs.service.NoiseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * created by SunHongbin on 2018/10/20
 */
@Service
public class NoiseServiceImpl implements NoiseService {

    @Resource
    private UserDao userDao;

    @Resource
    private NoiseDao noiseDao;

    @Override
    public NoiseMessage create(NoiseMessage noiseMessage, Long userPhone) {

        User user = new User();
        user.setUserPhone(userPhone);
        long participantId = userDao.selectSelective(user).get(0).getId();
        noiseMessage.setUploadTime(System.currentTimeMillis());
        noiseMessage.setUserId(participantId);
        try {
            noiseDao.insert(noiseMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<NoiseMessage> formMap(String minTime, String maxTime, String taskId) {
        Long task_id;
        if(minTime.equals("") || maxTime.equals("")){
            return null;
        }
        if (taskId == null || taskId.equals("")) {
            task_id = null;
        } else {
            task_id = Long.valueOf(taskId);
        }
        try {
            return noiseDao.queryByRangeOfTime(Long.valueOf(minTime), Long.valueOf(maxTime), task_id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
