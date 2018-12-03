package org.mcs.service.impl;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mcs.entity.NoiseMessage;
import org.mcs.service.NoiseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * created by SunHongbin on 2018/12/3
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class NoiseServiceImplTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Resource
    private NoiseService collectDb;

    @Test
    public void createDbRecord() {

        NoiseMessage record = new NoiseMessage();
        record.setDb((int) Math.random() * 100);
        record.setLongitude(Math.random() + 116);
        record.setLatitude(Math.random() + 39);
        record.setCollectTime(System.currentTimeMillis());

        try {
            File file = new File("D:\\CloudMusic\\20181026_124401.m4a");
            if (file.exists()) {
                //mp3的存储
                byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
                record.setMp3File(bytes);
                collectDb.create(record, 18500300866L);
            } else {
                System.out.println("文件不存在！");
            }
        } catch (IOException e) {
            logger.info("createDbRecord不通过：", e);
        }
    }

    @Test
    public void formMap() {
    }
}