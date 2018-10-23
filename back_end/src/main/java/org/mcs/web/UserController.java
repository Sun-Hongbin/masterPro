package org.mcs.web;

import org.mcs.entity.User;
import org.mcs.service.RegisterAndLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * created by SunHongbin on 2018/10/16
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RegisterAndLogin registerAndLogin;

    @RequestMapping(value = "/register", method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    public String register(@RequestBody User user) {
        System.out.println(user.toString());
        registerAndLogin.register(user);
        return user.toString();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET,
            produces = {"application/json;charset=UTF-8"})
    public void login(@RequestBody User user) {

        try {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            PrintWriter writer = response.getWriter();
            int res = 0;
            if (registerAndLogin.logIn(user) == true) {
                res = 1;
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
            }
        } catch (Exception e) {

        }

    }
}
