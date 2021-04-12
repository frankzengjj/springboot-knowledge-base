package com.github.frankzengjj.Wiki.service;

import com.github.frankzengjj.Wiki.domain.Category;
import com.github.frankzengjj.Wiki.domain.CategoryExample;
import com.github.frankzengjj.Wiki.mapper.CategoryMapper;
import com.github.frankzengjj.Wiki.request.CategoryQueryRequest;
import com.github.frankzengjj.Wiki.request.CategorySaveRequest;
import com.github.frankzengjj.Wiki.response.CategoryQueryResponse;
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
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResponse<CategoryQueryResponse> list(CategoryQueryRequest categoryQueryRequest) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        PageHelper.startPage(categoryQueryRequest.getPage(), categoryQueryRequest.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        PageResponse<CategoryQueryResponse> pageResponse = new PageResponse<>();
        List<CategoryQueryResponse> responseList = CopyUtil.copyList(categoryList, CategoryQueryResponse.class);
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(responseList);
        return pageResponse;
    }

    public List<CategoryQueryResponse> all() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        List<CategoryQueryResponse> list = CopyUtil.copyList(categoryList, CategoryQueryResponse.class);
        return list;
    }

    public void save(CategorySaveRequest categorySaveRequest) {
        Category category = CopyUtil.copy(categorySaveRequest, Category.class);
        if (ObjectUtils.isEmpty(categorySaveRequest.getId())) {
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
