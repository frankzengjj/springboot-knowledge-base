package com.github.frankzengjj.Wiki.service;

import com.github.frankzengjj.Wiki.domain.Demo;
import com.github.frankzengjj.Wiki.domain.Ebook;
import com.github.frankzengjj.Wiki.domain.EbookExample;
import com.github.frankzengjj.Wiki.mapper.DemoMapper;
import com.github.frankzengjj.Wiki.mapper.EbookMapper;
import com.github.frankzengjj.Wiki.request.EbookRequest;
import com.github.frankzengjj.Wiki.response.EbookResponse;
import com.github.frankzengjj.Wiki.util.CopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    public List<EbookResponse> list(EbookRequest ebookRequest) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(ebookRequest.getName())) {
            criteria.andNameLike("%" + ebookRequest.getName() + "%");
        }
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        List<EbookResponse> responseList = CopyUtil.copyList(ebookList, EbookResponse.class);
        return responseList;
    }
}
