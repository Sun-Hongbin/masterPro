package org.mcs.service.impl;

import org.mcs.dao.UserDao;
import org.mcs.entity.User;
import org.mcs.service.RegisterAndLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * created by SunHongbin on 2018/10/15
 */
@Service
public class RegisterAndLoginImpl implements RegisterAndLogin {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserDao userDao;

    @Override
    public int register(User user) {
        if (user.getUserPhone() == null || user.getEmail() == null || user.getName() == null) {
            return 0;
        }
        int nums = 0;
        try {
            //判断是否有重复注册
            //1、用户名
            User user_name = new User();
            user_name.setName(user.getName());
            List<User> list1 = userDao.selectSelective(user_name);
            if (list1.size() != 0) {//list != null 是在这种语境下是错误写法
                System.out.println(list1.get(0));
                return nums = 10;
            }
            //2、电话
            User user_phone = new User();
            user_phone.setUserPhone(user.getUserPhone());
            List<User> list2 = userDao.selectSelective(user_phone);
            if (list2.size() != 0) {
                System.out.println(list2.get(0));
                return nums = 11;
            }
            //3、邮箱
            User user_email = new User();
            user_email.setEmail(user.getEmail());
            List<User> list3 = userDao.selectSelective(user_email);
            if (list3.size() != 0) {
                System.out.println(list3.get(0));
                return nums = 12;
            }
            //注册，插入一条数据
            user.setCreateTime(System.currentTimeMillis());
            int res = userDao.insert(user);
            switch (res) {
                case 1:
                    nums = 1;
                    break;
                case 0:
                    nums = 0;
                    break;
            }
        } catch (Exception e) {
            logger.error("Service Layer: failed to register", e);
        }
        return nums;
    }

    @Override
    public Boolean logIn(Long userPhone, String password) {
        User user = new User();
        user.setUserPhone(userPhone);
        user.setPassword(password);
        user.setCreateTime(System.currentTimeMillis());
        try {
            if (userDao.selectSelective(user) != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Service Layer: failed to LOGIN", e);
        }
        return null;
    }

}
