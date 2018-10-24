package org.mcs.service;

import org.mcs.entity.User;

/**
 * created by SunHongbin on 2018/10/15
 */
public interface RegisterAndLogin {

    int register(User user);

    Boolean logIn(Long userPhone, String password);
}
