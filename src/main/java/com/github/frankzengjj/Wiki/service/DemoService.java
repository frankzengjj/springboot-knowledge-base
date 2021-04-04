package com.github.frankzengjj.Wiki.service;

import com.github.frankzengjj.Wiki.domain.Demo;
import com.github.frankzengjj.Wiki.domain.Test;
import com.github.frankzengjj.Wiki.mapper.DemoMapper;
import com.github.frankzengjj.Wiki.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {

    @Autowired
    private DemoMapper demoMapper;

    public List<Demo> list() {
        return demoMapper.selectByExample(null);
    }
}
