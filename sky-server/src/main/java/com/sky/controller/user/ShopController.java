package com.sky.controller.user;

import com.sky.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController("UserShopController")
@RequestMapping("/user/shop")
public class ShopController {
    public static final String KEY = "shopStatus";
    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @PutMapping("/{status}")
    public Result setStatus(@PathVariable String status){
        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();
        stringObjectValueOperations.set(KEY, status);
        return Result.success();
    }

    @GetMapping("/status")
    public Result<Integer> getStatus(){
        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();
        Integer shopStatus = (Integer) stringObjectValueOperations.get(KEY);
        return Result.success(shopStatus);
    }

}
