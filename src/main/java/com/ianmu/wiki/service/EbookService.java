package com.ianmu.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ianmu.wiki.entity.Ebook;
import com.ianmu.wiki.entity.EbookExample;
import com.ianmu.wiki.mapper.EbookMapper;
import com.ianmu.wiki.req.EbookReq;
import com.ianmu.wiki.resp.EbookResp;
import com.ianmu.wiki.resp.PageResp;
import com.ianmu.wiki.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    public List<EbookResp> all(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        List<EbookResp> ebookRespList = CopyUtil.copyList(ebookList, EbookResp.class);

        return ebookRespList;
    }

    public PageResp<EbookResp> list(EbookReq req) {
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
        List<EbookResp> ebookRespList = CopyUtil.copyList(ebookList, EbookResp.class);

        PageResp<EbookResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(ebookRespList);

        return pageResp;
    }
}
