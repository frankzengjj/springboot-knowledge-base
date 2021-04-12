package com.github.frankzengjj.Wiki.controller;

import com.github.frankzengjj.Wiki.request.DocQueryRequest;
import com.github.frankzengjj.Wiki.request.DocSaveRequest;
import com.github.frankzengjj.Wiki.response.DocQueryResponse;
import com.github.frankzengjj.Wiki.response.CommonResponse;
import com.github.frankzengjj.Wiki.response.PageResponse;
import com.github.frankzengjj.Wiki.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocService docService;

    @GetMapping("/list")
    public CommonResponse list(@Valid DocQueryRequest docQueryRequest) {
        CommonResponse<PageResponse<DocQueryResponse>> commonResponse = new CommonResponse<>();
        PageResponse<DocQueryResponse> list = docService.list(docQueryRequest);
        commonResponse.setContent(list);
        return commonResponse;
    }

    @GetMapping("/all/{ebookId}")
    public CommonResponse all(@PathVariable Long ebookId) {
        CommonResponse<List<DocQueryResponse>> commonResponse = new CommonResponse<>();
        List<DocQueryResponse> list = docService.all(ebookId);
        commonResponse.setContent(list);
        return commonResponse;
    }

    @PostMapping("/save")
    public CommonResponse save(@Valid @RequestBody DocSaveRequest docSaveRequest) {
        CommonResponse commonResponse = new CommonResponse<>();
        docService.save(docSaveRequest);
        return commonResponse;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResponse delete(@PathVariable String idsStr) {
        CommonResponse commonResponse = new CommonResponse<>();
        List<Long> list = Arrays
                            .stream(idsStr.split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
        docService.delete(list);
        return commonResponse;
    }

    @GetMapping("/content/{id}")
    public CommonResponse getContent(@PathVariable Long id) {
        CommonResponse<String> commonResponse = new CommonResponse<>();
        String content = docService.getContent(id);
        commonResponse.setContent(content);
        return commonResponse;
    }

    @GetMapping("/vote/{id}")
    public CommonResponse vote(@PathVariable Long id) {
        CommonResponse<String> commonResponse = new CommonResponse<>();
        docService.vote(id);
        return commonResponse;
    }
}
