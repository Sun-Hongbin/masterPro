package org.mcs.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mcs.entity.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * created by SunHongbin on 2018/10/15
 */

/**
 * 配置spring和junit整合，Junit启动时加载springIOC容器
 * spring-test,junit(依赖)
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class UserDaoTest {

    @Resource
    private UserDao userDao;

    @Test
    public void insert() {
        User user = new User();
        user.setName("adai");
        user.setUserPhone(123456789101L);
        user.setPassword("123456");
        user.setEmail("123@qq.com");
        Date ctime = new Date();
        user.setCreateTime(ctime);
        if(userDao.insert(user) == 1){
            System.out.println("success to register");
        }else{
            System.out.println("failed to register");
        }
    }

    @Test
    public void deleteByPrimaryKey() {
        if(userDao.deleteByPrimaryKey(1003L) == 1){
            System.out.println("success to del");
        }else {
            System.out.println("failed to del");
        }
    }

    @Test
    public void SelectSelective() {
        User user = new User();
        user.setUserPhone(18500300866L);
        System.out.println("pass: " + user);
        user.setPassword("123456");
        User user1  = userDao.SelectSelective(user);
        System.out.println("res: " + user1);
    }
}