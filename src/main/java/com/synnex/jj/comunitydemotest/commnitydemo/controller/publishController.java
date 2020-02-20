package com.synnex.jj.comunitydemotest.commnitydemo.controller;

import com.synnex.jj.comunitydemotest.commnitydemo.mapper.QuestionMapper;
import com.synnex.jj.comunitydemotest.commnitydemo.mapper.UserMapperDemo;
import com.synnex.jj.comunitydemotest.commnitydemo.model.Question;
import com.synnex.jj.comunitydemotest.commnitydemo.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class publishController {

    @Autowired
    private UserMapperDemo userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @RequestMapping(value = "/publish",method = {RequestMethod.GET})
    public String publish() {

        return "publish";
    }

    @RequestMapping(value = "/publish",method = {RequestMethod.POST})
    public String doPublish(@RequestParam(name = "title" ,required = false) String title,
                            @RequestParam(name = "content" ,required = false) String content,
                            @RequestParam(name = "tag" ,required = false) String tag,
                            HttpServletRequest request,
                            Model model) {

        model.addAttribute("title",title);
        model.addAttribute("content",content);
        model.addAttribute("tag",tag);

        if(StringUtils.isBlank(title)) {
            model.addAttribute("error","Title不存在");
            return "/publish";
        }

        if(StringUtils.isBlank(content)) {
            model.addAttribute("error","content不存在");
            return "/publish";
        }

        if(StringUtils.isBlank(tag)) {
            model.addAttribute("error","tag不存在");
            return "/publish";
        }

        User user = null;
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findUserByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }

        if(user == null) {
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTag(tag);
        question.setTitle(title);
        question.setContent(content);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());

        questionMapper.create(question);

        return "redirect:/";
    }
}
