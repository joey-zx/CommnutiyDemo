package com.synnex.jj.comunitydemotest.commnitydemo.controller;

import com.synnex.jj.comunitydemotest.commnitydemo.dto.GitHubUserDTO;
import com.synnex.jj.comunitydemotest.commnitydemo.dto.accessTokenDTO;
import com.synnex.jj.comunitydemotest.commnitydemo.mapper.UserMapperDemo;
import com.synnex.jj.comunitydemotest.commnitydemo.model.User;
import com.synnex.jj.comunitydemotest.commnitydemo.provider.GitHubProvide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private UserMapperDemo userMapper;

    @Autowired
    private GitHubProvide gitHubProvide;

    @Value("${GitHub.client.id}")
    private String client_id;

    @Value("${GitHub.client.secret}")
    private String client_secret;

    @Value("${GitHub.redirect.uri}")
    private String redirect_uri;

    @RequestMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {

        accessTokenDTO accessTokenDTO = new accessTokenDTO();
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        String accessToken = gitHubProvide.getAccessToken(accessTokenDTO);
        GitHubUserDTO gitHubUser = gitHubProvide.findGitHubUserByToken(accessToken);
        System.out.println(gitHubUser);

        if (gitHubUser != null) {
            User user = new User();
            user.setClientId(client_id);
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(gitHubUser.getName());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }

        return "redirect:/";
    }

}
