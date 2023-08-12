package com.ianmu.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ianmu.wiki.entity.Content;
import com.ianmu.wiki.entity.Doc;
import com.ianmu.wiki.entity.DocExample;
import com.ianmu.wiki.mapper.ContentMapper;
import com.ianmu.wiki.mapper.DocMapper;
import com.ianmu.wiki.req.DocQueryReq;
import com.ianmu.wiki.req.DocSaveReq;
import com.ianmu.wiki.resp.CommonResp;
import com.ianmu.wiki.resp.DocQueryResp;
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
    private ContentMapper contentMapper;

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
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            doc.setId(snowFlow.nextId());
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            docMapper.updateByPrimaryKey(CopyUtil.copy(req, Doc.class));
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0) {
                contentMapper.insert(content);
            }
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

    public String queryContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        return content == null ? "" : content.getContent();
    }
}
