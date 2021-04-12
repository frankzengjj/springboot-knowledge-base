package com.github.frankzengjj.Wiki.service;

import com.github.frankzengjj.Wiki.domain.Content;
import com.github.frankzengjj.Wiki.domain.ContentExample;
import com.github.frankzengjj.Wiki.domain.Doc;
import com.github.frankzengjj.Wiki.domain.DocExample;
import com.github.frankzengjj.Wiki.exception.BusinessException;
import com.github.frankzengjj.Wiki.exception.BusinessExceptionCode;
import com.github.frankzengjj.Wiki.mapper.ContentMapper;
import com.github.frankzengjj.Wiki.mapper.DocMapper;
import com.github.frankzengjj.Wiki.mapper.DocMapperCustom;
import com.github.frankzengjj.Wiki.request.DocQueryRequest;
import com.github.frankzengjj.Wiki.request.DocSaveRequest;
import com.github.frankzengjj.Wiki.response.DocQueryResponse;
import com.github.frankzengjj.Wiki.response.PageResponse;
import com.github.frankzengjj.Wiki.util.CopyUtil;
import com.github.frankzengjj.Wiki.util.RedisUtil;
import com.github.frankzengjj.Wiki.util.RequestContext;
import com.github.frankzengjj.Wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class DocService {

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private DocMapperCustom docMapperCustom;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private RedisUtil redisUtil;

    public PageResponse<DocQueryResponse> list(DocQueryRequest docQueryRequest) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(docQueryRequest.getPage(), docQueryRequest.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        PageResponse<DocQueryResponse> pageResponse = new PageResponse<>();
        List<DocQueryResponse> responseList = CopyUtil.copyList(docList, DocQueryResponse.class);
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(responseList);
        return pageResponse;
    }

    public List<DocQueryResponse> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        List<DocQueryResponse> list = CopyUtil.copyList(docList, DocQueryResponse.class);
        return list;
    }

    public void save(DocSaveRequest docSaveRequest) {
        Doc doc = CopyUtil.copy(docSaveRequest, Doc.class);
        Content content = CopyUtil.copy(docSaveRequest, Content.class);
        System.out.println(docSaveRequest);
        if (ObjectUtils.isEmpty(docSaveRequest.getId())) {
            // insert doc
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);
            // insert content
            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            docMapper.updateByPrimaryKey(doc);
            int record = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (record == 0) {
                contentMapper.insert(content);
            }
        }
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void vote(Long id) {
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3600 * 24)) {
            docMapperCustom.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
    }

    public String getContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        docMapperCustom.increaseViewCount(id);
        return ObjectUtils.isEmpty(content) ? "" : content.getContent();
    }


    public void delete(List<Long> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);

        ContentExample contentExample = new ContentExample();
        ContentExample.Criteria contentCriteria = contentExample.createCriteria();
        contentCriteria.andIdIn(ids);
        contentMapper.deleteByExample(contentExample);
    }

    public void updateEbookInfo() {
        docMapperCustom.updateEbookInfo();
    }

}
