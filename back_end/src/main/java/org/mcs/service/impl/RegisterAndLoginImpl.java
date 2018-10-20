package org.mcs.service.impl;

import org.mcs.dao.UserDao;
import org.mcs.entity.User;
import org.mcs.service.RegisterAndLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * created by SunHongbin on 2018/10/15
 */
@Service
public class RegisterAndLoginImpl implements RegisterAndLogin {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserDao userDao;

    @Override
    public User register(User user) {
        try {
            user.setCreateTime(System.currentTimeMillis());
            int res = userDao.insert(user);
            switch (res) {
                case 1:
                    logger.info("Service Layer: register success ");
                    break;
                case 0:
                    logger.info("Service Layer: failed to register");
                    break;
            }
        }catch (Exception e){
            logger.error("Service Layer: failed to register", e);
        }
        return null;
    }

    @Override
    public Boolean logIn(User user) {
        if(user.getUserPhone() == null && user.getEmail() == null){
            return false;
        }
        try{
            if(userDao.SelectSelective(user) != null){
                return true;
            } else {
                return false;
            }
        }catch (Exception e) {
            logger.error("Service Layer: failed to LOGIN", e);
        }
        return null;
    }

}
