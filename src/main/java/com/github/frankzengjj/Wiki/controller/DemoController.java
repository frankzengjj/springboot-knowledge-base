package com.github.frankzengjj.Wiki.controller;

import com.github.frankzengjj.Wiki.domain.Demo;
import com.github.frankzengjj.Wiki.domain.Test;
import com.github.frankzengjj.Wiki.service.DemoService;
import com.github.frankzengjj.Wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/list")
    public List<Demo> list() {
        return demoService.list();
    }
}
