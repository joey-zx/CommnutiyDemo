package com.synnex.jj.comunitydemotest.commnitydemo.provider;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSON;
import com.synnex.jj.comunitydemotest.commnitydemo.dto.GitHubUserDTO;
import com.synnex.jj.comunitydemotest.commnitydemo.dto.accessTokenDTO;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
public class GitHubProvide {
    public String getAccessToken(accessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String ss = response.body().string();
            String token = ss.split("&")[0].split("=")[1];
            //System.out.println(token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GitHubUserDTO findGitHubUserByToken(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try{
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GitHubUserDTO gitHubUser = JSON.parseObject(string,GitHubUserDTO.class);
            //System.out.println(gitHubUser.getName());
            return gitHubUser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
