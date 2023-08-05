package com.ianmu.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ianmu.wiki.entity.Category;
import com.ianmu.wiki.entity.CategoryExample;
import com.ianmu.wiki.mapper.CategoryMapper;
import com.ianmu.wiki.req.CategoryQueryReq;
import com.ianmu.wiki.req.CategorySaveReq;
import com.ianmu.wiki.resp.CommonResp;
import com.ianmu.wiki.resp.CategoryQueryResp;
import com.ianmu.wiki.resp.PageResp;
import com.ianmu.wiki.utils.CopyUtil;
import com.ianmu.wiki.utils.SnowFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SnowFlow snowFlow;

    public List<CategoryQueryResp> all(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        return CopyUtil.copyList(categoryList, CategoryQueryResp.class);
    }

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if (!ObjectUtils.isEmpty(req.getPage()) && req.getPage() > 0 && !ObjectUtils.isEmpty(req.getSize()) && req.getSize() > 0) {
            PageHelper.startPage(req.getPage(), req.getSize());
        }
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        List<CategoryQueryResp> categoryQueryRespList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(categoryQueryRespList);

        return pageResp;
    }

    public CommonResp save(CategorySaveReq req) {
        CommonResp commonResp = new CommonResp();
        if (ObjectUtils.isEmpty(req.getId())) {
            req.setId(snowFlow.nextId());
            categoryMapper.insert(CopyUtil.copy(req, Category.class));
        } else {
            categoryMapper.updateByPrimaryKey(CopyUtil.copy(req, Category.class));
        }
        return commonResp;
    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
