package com.github.frankzengjj.Wiki.controller;

import com.github.frankzengjj.Wiki.domain.Demo;
import com.github.frankzengjj.Wiki.domain.Ebook;
import com.github.frankzengjj.Wiki.request.EbookRequest;
import com.github.frankzengjj.Wiki.response.CommonResponse;
import com.github.frankzengjj.Wiki.response.EbookResponse;
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
    public CommonResponse list(EbookRequest ebookRequest) {
        CommonResponse<List<EbookResponse>> commonResponse = new CommonResponse<>();
        List<EbookResponse> list = ebookService.list(ebookRequest);
        commonResponse.setContent(list);
        return commonResponse;
    }
}
