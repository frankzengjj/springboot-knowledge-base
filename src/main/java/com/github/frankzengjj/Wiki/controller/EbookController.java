package com.github.frankzengjj.Wiki.controller;

import com.github.frankzengjj.Wiki.request.EbookQueryRequest;
import com.github.frankzengjj.Wiki.request.EbookSaveRequest;
import com.github.frankzengjj.Wiki.response.CommonResponse;
import com.github.frankzengjj.Wiki.response.EbookQueryResponse;
import com.github.frankzengjj.Wiki.response.PageResponse;
import com.github.frankzengjj.Wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResponse list(@Valid EbookQueryRequest ebookQueryRequest) {
        CommonResponse<PageResponse<EbookQueryResponse>> commonResponse = new CommonResponse<>();
        PageResponse<EbookQueryResponse> list = ebookService.list(ebookQueryRequest);
        commonResponse.setContent(list);
        return commonResponse;
    }

    @PostMapping("/save")
    public CommonResponse save(@Valid @RequestBody EbookSaveRequest ebookSaveRequest) {
        CommonResponse commonResponse = new CommonResponse<>();
        ebookService.save(ebookSaveRequest);
        return commonResponse;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable Long id) {
        CommonResponse commonResponse = new CommonResponse<>();
        ebookService.delete(id);
        return commonResponse;
    }
}
