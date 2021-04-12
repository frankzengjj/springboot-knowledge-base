package com.github.frankzengjj.Wiki.service;

import com.github.frankzengjj.Wiki.domain.User;
import com.github.frankzengjj.Wiki.domain.UserExample;
import com.github.frankzengjj.Wiki.exception.BusinessException;
import com.github.frankzengjj.Wiki.exception.BusinessExceptionCode;
import com.github.frankzengjj.Wiki.mapper.UserMapper;
import com.github.frankzengjj.Wiki.request.UserLoginRequest;
import com.github.frankzengjj.Wiki.request.UserQueryRequest;
import com.github.frankzengjj.Wiki.request.UserResetPasswordRequest;
import com.github.frankzengjj.Wiki.request.UserSaveRequest;
import com.github.frankzengjj.Wiki.response.UserLoginResponse;
import com.github.frankzengjj.Wiki.response.UserQueryResponse;
import com.github.frankzengjj.Wiki.response.PageResponse;
import com.github.frankzengjj.Wiki.util.CopyUtil;
import com.github.frankzengjj.Wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public PageResponse<UserQueryResponse> list(UserQueryRequest userQueryRequest) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(userQueryRequest.getLoginName())) {
            criteria.andLoginNameEqualTo(userQueryRequest.getLoginName());
        }
        PageHelper.startPage(userQueryRequest.getPage(), userQueryRequest.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        PageResponse<UserQueryResponse> pageResponse = new PageResponse<>();
        List<UserQueryResponse> responseList = CopyUtil.copyList(userList, UserQueryResponse.class);
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(responseList);
        return pageResponse;
    }

    public List<UserQueryResponse> all() {
        UserExample userExample = new UserExample();
        userExample.setOrderByClause("sort asc");
        List<User> userList = userMapper.selectByExample(userExample);
        List<UserQueryResponse> list = CopyUtil.copyList(userList, UserQueryResponse.class);
        return list;
    }

    public void save(UserSaveRequest userResetPasswordRequest) {
        User user = CopyUtil.copy(userResetPasswordRequest, User.class);
        if (ObjectUtils.isEmpty(userResetPasswordRequest.getId())) {
            // new user
            if (ObjectUtils.isEmpty(selectByLoginName(userResetPasswordRequest.getLoginName()))) {
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            } else {
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
            user.setLoginName(null);
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName (String loginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(userExample);
        return CollectionUtils.isEmpty(userList) ? null : userList.get(0);
    }

    public void resetPassword(UserResetPasswordRequest userResetPasswordRequest) {
        User user = CopyUtil.copy(userResetPasswordRequest, User.class);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public UserLoginResponse login (UserLoginRequest userLoginRequest) {
        User currentUser = selectByLoginName(userLoginRequest.getLoginName());
        if (ObjectUtils.isEmpty(currentUser)) {
            LOG.info("Login Name Does Not Exist, {}", userLoginRequest.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            if (currentUser.getPassword().equals(userLoginRequest.getPassword())) {
                UserLoginResponse userLoginResponse = CopyUtil.copy(currentUser, UserLoginResponse.class);
                return userLoginResponse;
            } else {
                LOG.info("Wrong Password, Input {}, DB password {}", userLoginRequest.getPassword(), currentUser.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }
}
