package com.ianmu.wiki.service;

import com.ianmu.wiki.entity.Ebook;
import com.ianmu.wiki.entity.EbookExample;
import com.ianmu.wiki.mapper.EbookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    public List<Ebook> list(){
        return ebookMapper.selectByExample(new EbookExample());
    }
}
