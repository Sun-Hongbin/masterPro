package org.mcs.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mcs.entity.NoiseMessage;
import org.mcs.service.CollectDb;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * created by SunHongbin on 2018/10/20
 */

/**
 * 配置spring和junit整合，Junit启动时加载springIOC容器
 * spring-test,junit(依赖)
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class CollectDbImplTest {

    @Resource
    private CollectDb collectDb;

    @Test
    public void createDbRecord() {

        for(int i = 0; i<5;i++){
            NoiseMessage record = new NoiseMessage();
            record.setUserId((1000L));
            record.setDb((int)Math.random()*100);
            record.setLongtitude(Math.random()+116);
            record.setLatitude(Math.random()+39);
            collectDb.createDbRecord(record);
        }
    }
}