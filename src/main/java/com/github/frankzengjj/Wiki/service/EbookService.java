package com.github.frankzengjj.Wiki.service;

import com.github.frankzengjj.Wiki.domain.Demo;
import com.github.frankzengjj.Wiki.domain.Ebook;
import com.github.frankzengjj.Wiki.mapper.DemoMapper;
import com.github.frankzengjj.Wiki.mapper.EbookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }
}
