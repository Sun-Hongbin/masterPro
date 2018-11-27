package org.mcs.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mcs.entity.TaskRecord;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * created by SunHongbin on 2018/11/19
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class TaskDaoTest {

    @Resource
    private TaskDao taskDao;

    @Test
    public void selectSelective() {
        TaskRecord taskRecord = new TaskRecord();
        taskRecord.setPublisherId(1001L);
        List<TaskRecord> list = taskDao.selectSelective(taskRecord);
        for (TaskRecord task : list) {
            System.out.println(task.toString());
        }
    }
}