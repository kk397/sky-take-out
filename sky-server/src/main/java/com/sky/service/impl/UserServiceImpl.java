package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    public static final String LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    WeChatProperties weChatProperties;
    @Autowired
    UserMapper userMapper;
    @Override
    public User login(UserLoginDTO userLoginDTO) throws URISyntaxException, IOException {
        /* 调用doGet方法
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        map.put("grant_type", "authorization_code");
        String result = HttpClientUtil.doGet(LOGIN_URL, map);
        */
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URIBuilder builder = new URIBuilder(LOGIN_URL);
        builder.addParameter("appid", weChatProperties.getAppid());
        builder.addParameter("secret", weChatProperties.getSecret());
        builder.addParameter("js_code", userLoginDTO.getCode());
        builder.addParameter("grant_type", "authorization_code");
        URI uri = builder.build();
        log.info(uri.toString());
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse execute = httpClient.execute(get);
        String result = "";
        if(execute.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            result = EntityUtils.toString(execute.getEntity());
        }else {
            throw new RuntimeException("errorCode:"+ execute.getStatusLine().getStatusCode());
        }
        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.getString("openid");
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getOpenid, openid);
        User user = userMapper.selectOne(lqw);
        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .build();
            userMapper.insert(user);
        }
        return user;
    }
}
