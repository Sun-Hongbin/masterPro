package org.mcs.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mcs.entity.User;
import org.mcs.service.RegisterAndLogin;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;

/**
 * created by SunHongbin on 2018/10/15
 */

/**
 * 配置spring和junit整合，Junit启动时加载springIOC容器
 * spring-test,junit(依赖)
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class RegisterImplTest {

    @Resource
    private RegisterAndLogin register;
    
    @Test
    public void regitsr() {
        User user = new User();
        user.setName("adai");
        user.setUserPhone(123456789101L);
        user.setPassword("123456");
        user.setEmail("123@qq.com");
        user.setCreateTime(System.currentTimeMillis());
        User user1 = register.register(user);
        System.out.println(user1.toString());
    }

    @Test
    public void regitsr1() {
    }
}