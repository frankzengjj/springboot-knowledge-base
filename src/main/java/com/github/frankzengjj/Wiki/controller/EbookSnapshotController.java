package com.github.frankzengjj.Wiki.controller;

import com.github.frankzengjj.Wiki.response.CommonResponse;
import com.github.frankzengjj.Wiki.response.StatisticResp;
import com.github.frankzengjj.Wiki.service.EbookSnapshotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {

    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @GetMapping("/get-statistic")
    public CommonResponse getStatistic() {
        List<StatisticResp> statisticResp = ebookSnapshotService.getStatistic();
        CommonResponse<List<StatisticResp>> commonResp = new CommonResponse<>();
        commonResp.setContent(statisticResp);
        return commonResp;
    }

    @GetMapping("/get-30-statistic")
    public CommonResponse get30Statistic() {
        List<StatisticResp> statisticResp = ebookSnapshotService.get30Statistic();
        CommonResponse<List<StatisticResp>> commonResp = new CommonResponse<>();
        commonResp.setContent(statisticResp);
        return commonResp;
    }

}