package org.mcs.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mcs.entity.NoiseMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * created by SunHongbin on 2018/12/1
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class NoiseDaoTest {

    @Resource
    private NoiseDao noiseDao;

    @Test
    public void queryByRangeOfTime() {
        List<NoiseMessage> list = noiseDao.queryByRangeOfTime(1543592029999L, 1543592050500L,1L);
        for (NoiseMessage message : list) {
            System.out.println(message);
        }
    }
}