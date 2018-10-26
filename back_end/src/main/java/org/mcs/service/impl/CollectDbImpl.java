package org.mcs.service.impl;

import org.mcs.dao.NoiseDao;
import org.mcs.dao.UserDao;
import org.mcs.entity.NoiseMessage;
import org.mcs.entity.User;
import org.mcs.service.CollectDb;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * created by SunHongbin on 2018/10/20
 */
@Service
public class CollectDbImpl implements CollectDb {

    @Resource
    private UserDao userDao;

    @Resource
    private NoiseDao noiseDao;

    @Override
    public NoiseMessage createDbRecord(NoiseMessage noiseMessage, Long userPhone) {

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
    public List<NoiseMessage> formMap(NoiseMessage noiseMessage) {
        if (noiseMessage.getUploadTime() == null) {
            return null;
        }
        try {
            return noiseDao.querySelective(noiseMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
