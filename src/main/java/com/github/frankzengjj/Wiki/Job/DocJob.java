package com.github.frankzengjj.Wiki.Job;

import com.github.frankzengjj.Wiki.service.DocService;
import com.github.frankzengjj.Wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Autowired
    private DocService docService;

    @Autowired
    private SnowFlake snowFlake;

    /**
     * 每30秒更新电子书信息
     */
    @Scheduled(cron = "5/30 * * * * ?")
    public void cron() {
        // 增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("Start updating Ebook info");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("Finish updating Ebook info，Time elapsed：{}ms", System.currentTimeMillis() - start);
    }

}
