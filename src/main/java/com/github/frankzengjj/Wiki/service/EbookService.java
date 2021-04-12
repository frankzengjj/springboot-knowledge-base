package com.github.frankzengjj.Wiki.service;

import com.github.frankzengjj.Wiki.domain.Ebook;
import com.github.frankzengjj.Wiki.domain.EbookExample;
import com.github.frankzengjj.Wiki.mapper.EbookMapper;
import com.github.frankzengjj.Wiki.request.EbookQueryRequest;
import com.github.frankzengjj.Wiki.request.EbookSaveRequest;
import com.github.frankzengjj.Wiki.response.EbookQueryResponse;
import com.github.frankzengjj.Wiki.response.PageResponse;
import com.github.frankzengjj.Wiki.util.CopyUtil;
import com.github.frankzengjj.Wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResponse<EbookQueryResponse> list(EbookQueryRequest ebookQueryRequest) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(ebookQueryRequest.getName())) {
            criteria.andNameLike("%" + ebookQueryRequest.getName() + "%");
        }
        if (!ObjectUtils.isEmpty(ebookQueryRequest.getCategoryId2())) {
            criteria.andCategory2IdEqualTo( ebookQueryRequest.getCategoryId2());
        }
        PageHelper.startPage(ebookQueryRequest.getPage(), ebookQueryRequest.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        PageResponse<EbookQueryResponse> pageResponse = new PageResponse<>();
        List<EbookQueryResponse> responseList = CopyUtil.copyList(ebookList, EbookQueryResponse.class);
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(responseList);
        return pageResponse;
    }

    public void save(EbookSaveRequest ebookSaveRequest) {
        Ebook ebook = CopyUtil.copy(ebookSaveRequest, Ebook.class);
        if (ObjectUtils.isEmpty(ebookSaveRequest.getId())) {
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        } else {
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    public void delete(Long id) {
        ebookMapper.deleteByPrimaryKey(id);
    }
}
