package com.ianmu.wiki.service;

import com.ianmu.wiki.entity.Ebook;
import com.ianmu.wiki.entity.EbookExample;
import com.ianmu.wiki.mapper.EbookMapper;
import com.ianmu.wiki.req.EbookReq;
import com.ianmu.wiki.resp.EbookResp;
import com.ianmu.wiki.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + req.getName() + "%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        return CopyUtil.copyList(ebookList, EbookResp.class);
    }
}
