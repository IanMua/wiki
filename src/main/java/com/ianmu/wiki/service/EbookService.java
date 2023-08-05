package com.ianmu.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ianmu.wiki.entity.Ebook;
import com.ianmu.wiki.entity.EbookExample;
import com.ianmu.wiki.mapper.EbookMapper;
import com.ianmu.wiki.req.EbookQueryReq;
import com.ianmu.wiki.req.EbookSaveReq;
import com.ianmu.wiki.resp.CommonResp;
import com.ianmu.wiki.resp.EbookQueryResp;
import com.ianmu.wiki.resp.PageResp;
import com.ianmu.wiki.utils.CopyUtil;
import com.ianmu.wiki.utils.SnowFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    @Autowired
    private SnowFlow snowFlow;

    public List<EbookQueryResp> all(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        return CopyUtil.copyList(ebookList, EbookQueryResp.class);
    }

    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if (!ObjectUtils.isEmpty(req.getPage()) && req.getPage() > 0 && !ObjectUtils.isEmpty(req.getSize()) && req.getSize() > 0) {
            PageHelper.startPage(req.getPage(), req.getSize());
        }
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        List<EbookQueryResp> ebookQueryRespList = CopyUtil.copyList(ebookList, EbookQueryResp.class);

        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(ebookQueryRespList);

        return pageResp;
    }

    public CommonResp save(EbookSaveReq req) {
        CommonResp commonResp = new CommonResp();
        if (ObjectUtils.isEmpty(req.getId())) {
            req.setId(snowFlow.nextId());
            ebookMapper.insert(CopyUtil.copy(req, Ebook.class));
        } else {
            ebookMapper.updateByPrimaryKey(CopyUtil.copy(req, Ebook.class));
        }
        return commonResp;
    }

    public void delete(Long id) {
        ebookMapper.deleteByPrimaryKey(id);
    }
}
