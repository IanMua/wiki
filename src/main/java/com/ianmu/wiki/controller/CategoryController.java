package com.ianmu.wiki.controller;

import com.ianmu.wiki.req.CategoryQueryReq;
import com.ianmu.wiki.req.CategorySaveReq;
import com.ianmu.wiki.resp.CommonResp;
import com.ianmu.wiki.resp.CategoryQueryResp;
import com.ianmu.wiki.resp.PageResp;
import com.ianmu.wiki.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public CommonResp<List<CategoryQueryResp>> all(CategoryQueryReq req) {
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.all(req);
        resp.setContent(list);
        return resp;
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResp<PageResp<CategoryQueryResp>> list(@Valid @ModelAttribute CategoryQueryReq req) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp save(@Valid @RequestBody CategorySaveReq req) {
        return categoryService.save(req);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable Long id) {
        categoryService.delete(id);
        return new CommonResp();
    }
}
