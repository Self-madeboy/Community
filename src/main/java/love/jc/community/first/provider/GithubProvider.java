package love.jc.community.first.provider;

import com.alibaba.fastjson.JSON;
import love.jc.community.first.dto.AccessTokenDTO;
import love.jc.community.first.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.Media;
import java.io.IOException;

@Component  //初始化到spring的上下文，在调用GithubProvider的时候不用new
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
         MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();


            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
            Request.Builder builder = new Request.Builder();
            builder.url("https://github.com/login/oauth/access_token");
            builder.post(body);
            Request request = builder
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string=response.body().string();
                //把得到的字符串分割
                String token= string.split("&")[0].split("=")[1];
                return token;
            } catch (IOException e) {
                e.printStackTrace();
            }

                return null;
    }
    public GithubUser getUser(String accessTokrn){
        OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.github.com/user?access_token="+accessTokrn)
                    .build();
        try {
            Response response = client.newCall(request).execute();
            String string=response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);//自动把json串转化为java的对象
            return  githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }


}
