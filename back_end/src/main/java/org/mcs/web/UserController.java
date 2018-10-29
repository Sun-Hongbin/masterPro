package org.mcs.web;

import org.mcs.entity.User;
import org.mcs.service.RegisterAndLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * created by SunHongbin on 2018/10/16
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RegisterAndLogin registerAndLogin;

    @RequestMapping(value = "/register", method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String register(@RequestBody User user) throws IOException {

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        PrintWriter writer = response.getWriter();
        int num = registerAndLogin.register(user);
        switch (num) {
            case 0:
                writer.write("register failed");
                writer.flush();
                break;
            case 1:
                writer.write("register success");
                writer.flush();
                break;
            case 10:
                writer.write("User name has been registered");
                writer.flush();
                break;
            case 11:
                writer.write("PhoneNumber has been registered");
                writer.flush();
                break;
            case 12:
                writer.write("Email has been registered");
                writer.flush();
                break;
        }
        writer.close();
        return user.toString();
    }



    @RequestMapping(value = "/login", method = RequestMethod.GET,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void login(HttpServletRequest request) throws IOException {

        String userPhone = request.getParameter("userPhone");
        String password = request.getParameter("password");
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        PrintWriter writer = response.getWriter();
        int res = 0;
        if (registerAndLogin.logIn(Long.valueOf(userPhone), password) == true) {
            res = 1;
            logger.debug("登陆成功： 账号为：" + Long.valueOf(userPhone));
        }
        switch (res) {
            case 1:
                writer.write("Login in success");
                writer.flush();
                break;
            case 0:
                writer.write("Wrong password / User doesn't exist");
                writer.flush();
                break;
            default:
                writer.write("check server logs");
                writer.flush();
                break;
        }
        writer.close();
    }


}
