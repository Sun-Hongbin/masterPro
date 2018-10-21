package org.mcs.service.impl;

import org.mcs.dao.DbFileDao;
import org.mcs.entity.NoiseMessage;
import org.mcs.service.CollectDb;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * created by SunHongbin on 2018/10/20
 */
@Service
public class CollectDbImpl implements CollectDb {

    @Resource
    private DbFileDao dbFileDao;

    @Override
    public NoiseMessage createDbRecord(NoiseMessage record) {

        record.setDb(record.getDb());
        record.setUserId(1002L);
        try {
            dbFileDao.insert(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
