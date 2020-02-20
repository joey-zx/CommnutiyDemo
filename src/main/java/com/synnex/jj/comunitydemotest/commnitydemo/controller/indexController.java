package com.synnex.jj.comunitydemotest.commnitydemo.controller;

import com.synnex.jj.comunitydemotest.commnitydemo.mapper.UserMapperDemo;
import com.synnex.jj.comunitydemotest.commnitydemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class indexController {

    @Autowired
    private UserMapperDemo userMapper;

    @RequestMapping("/")
    public String index(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findUserByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        return "index";
    }
}
