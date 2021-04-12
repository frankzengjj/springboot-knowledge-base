package com.github.frankzengjj.Wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.frankzengjj.Wiki.domain.Test;
import com.github.frankzengjj.Wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/test/list")
    public List<Test> list() {
        return testService.list();
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable Long key) {
        Object object = redisTemplate.opsForValue().get(key);
        return JSONObject.toJSON(object);
    }
}
