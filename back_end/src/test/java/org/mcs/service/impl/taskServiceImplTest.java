package org.mcs.service.impl;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mcs.entity.TaskRecord;
import org.mcs.service.TaskService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * created by SunHongbin on 2018/11/17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class taskServiceImplTest {

    @Resource
    private TaskService taskService;

    @Test
    public void create() {

        TaskRecord taskRecord = new TaskRecord();
        Date executeTime = new Date();
        System.out.println("执行时间："+executeTime);//执行时间：Sat Nov 17 12:15:47 CST 2018
        taskRecord.setTaskExecuteTime(executeTime);
        taskRecord.setTaskDescription("检测9教南205室的噪声情况");
        taskRecord.setTaskLocation("中国北京市海淀区交通大学路3号院");
        taskRecord.setTaskLongitude(116.348617);
        taskRecord.setTaskLatitude(39.955292);
        taskService.create(taskRecord, 18500300866L);

    }

    @Test
    public void removeById() {
    }

    @Test
    public void changeByIdSelective() {
    }

    @Test
    public void getByTime() {
    }

    @Test
    public void getByLocation() {
    }

    @Test
    public void getByTags() {
    }
}