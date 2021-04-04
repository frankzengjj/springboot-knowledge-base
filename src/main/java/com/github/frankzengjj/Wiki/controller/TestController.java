package com.github.frankzengjj.Wiki.controller;

import com.github.frankzengjj.Wiki.domain.Test;
import com.github.frankzengjj.Wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test/list")
    public List<Test> list() {
        return testService.list();
    }
}