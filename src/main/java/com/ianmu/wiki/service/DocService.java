package com.ianmu.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ianmu.wiki.entity.Doc;
import com.ianmu.wiki.entity.DocExample;
import com.ianmu.wiki.mapper.DocMapper;
import com.ianmu.wiki.req.DocQueryReq;
import com.ianmu.wiki.req.DocSaveReq;
import com.ianmu.wiki.resp.DocQueryResp;
import com.ianmu.wiki.resp.CommonResp;
import com.ianmu.wiki.resp.PageResp;
import com.ianmu.wiki.utils.CopyUtil;
import com.ianmu.wiki.utils.SnowFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class DocService {

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private SnowFlow snowFlow;

    public List<DocQueryResp> all(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getEbookId())) {
            criteria.andEbookIdEqualTo(req.getEbookId());
        }
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        List<Doc> docList = docMapper.selectByExample(docExample);

        return CopyUtil.copyList(docList, DocQueryResp.class);
    }

    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if (!ObjectUtils.isEmpty(req.getPage()) && req.getPage() > 0 && !ObjectUtils.isEmpty(req.getSize()) && req.getSize() > 0) {
            PageHelper.startPage(req.getPage(), req.getSize());
        }
        List<Doc> docList = docMapper.selectByExample(docExample);
        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        List<DocQueryResp> docQueryRespList = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(docQueryRespList);

        return pageResp;
    }

    public CommonResp save(DocSaveReq req) {
        CommonResp commonResp = new CommonResp();
        if (ObjectUtils.isEmpty(req.getId())) {
            req.setId(snowFlow.nextId());
            docMapper.insert(CopyUtil.copy(req, Doc.class));
        } else {
            docMapper.updateByPrimaryKey(CopyUtil.copy(req, Doc.class));
        }
        return commonResp;
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<Long> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }
}
