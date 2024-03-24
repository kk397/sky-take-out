package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.vo.UserLoginVO;

import java.io.IOException;
import java.net.URISyntaxException;


public interface UserService extends IService<User> {
    User login(UserLoginDTO userLoginDTO) throws URISyntaxException, IOException;

}
