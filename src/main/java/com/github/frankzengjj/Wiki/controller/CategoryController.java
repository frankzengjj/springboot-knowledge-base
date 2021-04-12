package com.github.frankzengjj.Wiki.controller;

import com.github.frankzengjj.Wiki.domain.Category;
import com.github.frankzengjj.Wiki.request.CategoryQueryRequest;
import com.github.frankzengjj.Wiki.request.CategorySaveRequest;
import com.github.frankzengjj.Wiki.response.CategoryQueryResponse;
import com.github.frankzengjj.Wiki.response.CommonResponse;
import com.github.frankzengjj.Wiki.response.PageResponse;
import com.github.frankzengjj.Wiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public CommonResponse list(@Valid CategoryQueryRequest categoryQueryRequest) {
        CommonResponse<PageResponse<CategoryQueryResponse>> commonResponse = new CommonResponse<>();
        PageResponse<CategoryQueryResponse> list = categoryService.list(categoryQueryRequest);
        commonResponse.setContent(list);
        return commonResponse;
    }

    @GetMapping("/all")
    public CommonResponse all() {
        CommonResponse<List<CategoryQueryResponse>> commonResponse = new CommonResponse<>();
        List<CategoryQueryResponse> list = categoryService.all();
        commonResponse.setContent(list);
        return commonResponse;
    }

    @PostMapping("/save")
    public CommonResponse save(@Valid @RequestBody CategorySaveRequest categorySaveRequest) {
        CommonResponse commonResponse = new CommonResponse<>();
        categoryService.save(categorySaveRequest);
        return commonResponse;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable Long id) {
        CommonResponse commonResponse = new CommonResponse<>();
        categoryService.delete(id);
        return commonResponse;
    }
}
