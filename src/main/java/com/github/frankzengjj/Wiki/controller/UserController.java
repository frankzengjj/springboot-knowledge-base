package com.github.frankzengjj.Wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.frankzengjj.Wiki.request.UserLoginRequest;
import com.github.frankzengjj.Wiki.request.UserQueryRequest;
import com.github.frankzengjj.Wiki.request.UserResetPasswordRequest;
import com.github.frankzengjj.Wiki.request.UserSaveRequest;
import com.github.frankzengjj.Wiki.response.UserLoginResponse;
import com.github.frankzengjj.Wiki.response.UserQueryResponse;
import com.github.frankzengjj.Wiki.response.CommonResponse;
import com.github.frankzengjj.Wiki.response.PageResponse;
import com.github.frankzengjj.Wiki.service.UserService;
import com.github.frankzengjj.Wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @GetMapping("/list")
    public CommonResponse list(@Valid UserQueryRequest userQueryRequest) {
        CommonResponse<PageResponse<UserQueryResponse>> commonResponse = new CommonResponse<>();
        PageResponse<UserQueryResponse> list = userService.list(userQueryRequest);
        commonResponse.setContent(list);
        return commonResponse;
    }

    @GetMapping("/all")
    public CommonResponse all() {
        CommonResponse<List<UserQueryResponse>> commonResponse = new CommonResponse<>();
        List<UserQueryResponse> list = userService.all();
        commonResponse.setContent(list);
        return commonResponse;
    }

    @PostMapping("/save")
    public CommonResponse save(@Valid @RequestBody UserSaveRequest userSaveRequest) {
        userSaveRequest.setPassword(DigestUtils.md5DigestAsHex(userSaveRequest.getPassword().getBytes(StandardCharsets.UTF_8)));
        CommonResponse commonResponse = new CommonResponse<>();
        userService.save(userSaveRequest);
        return commonResponse;
    }

    @PostMapping("/reset_password")
    public CommonResponse resetPassowrd(@Valid @RequestBody UserResetPasswordRequest userResetPasswordRequest) {
        userResetPasswordRequest.setPassword(DigestUtils.md5DigestAsHex(userResetPasswordRequest.getPassword().getBytes(StandardCharsets.UTF_8)));
        CommonResponse commonResponse = new CommonResponse<>();
        userService.resetPassword(userResetPasswordRequest);
        return commonResponse;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable Long id) {
        CommonResponse commonResponse = new CommonResponse<>();
        userService.delete(id);
        return commonResponse;
    }

    @PostMapping("/login")
    public CommonResponse login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        userLoginRequest.setPassword(DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes()));
        CommonResponse commonResponse = new CommonResponse<>();
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest);
        // single sign on token
        Long token = snowFlake.nextId();
        userLoginResponse.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResponse), 3600*24, TimeUnit.SECONDS);
        LOG.info("Generated token {} and saved into redis", token);
        commonResponse.setContent(userLoginResponse);
        return commonResponse;
    }

    @GetMapping("/logout/{token}")
    public CommonResponse logut(@PathVariable String token) {
        CommonResponse commonResponse = new CommonResponse<>();
        redisTemplate.delete(token);
        LOG.info("Deleted token from redis {}", token);
        return commonResponse;
    }

}
