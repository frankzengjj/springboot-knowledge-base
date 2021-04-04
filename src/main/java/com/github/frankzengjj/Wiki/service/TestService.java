package com.github.frankzengjj.Wiki.service;

import com.github.frankzengjj.Wiki.domain.Test;
import com.github.frankzengjj.Wiki.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestMapper testMapper;

    public List<Test> list() {
        return testMapper.list();
    }
}
