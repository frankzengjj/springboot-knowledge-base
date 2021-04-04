package com.github.frankzengjj.Wiki.controller;

import com.github.frankzengjj.Wiki.domain.Demo;
import com.github.frankzengjj.Wiki.domain.Ebook;
import com.github.frankzengjj.Wiki.response.CommonResponse;
import com.github.frankzengjj.Wiki.service.DemoService;
import com.github.frankzengjj.Wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResponse list() {
        CommonResponse<List<Ebook>> commonResponse = new CommonResponse<>();
        List<Ebook> list = ebookService.list();
        commonResponse.setContent(list);
        return commonResponse;
    }
}
