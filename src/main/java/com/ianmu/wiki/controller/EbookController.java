package com.ianmu.wiki.controller;

import com.ianmu.wiki.req.EbookReq;
import com.ianmu.wiki.resp.CommonResp;
import com.ianmu.wiki.resp.EbookResp;
import com.ianmu.wiki.resp.PageResp;
import com.ianmu.wiki.service.EbookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ebook")
public class EbookController {

    private static final Logger LOG = LoggerFactory.getLogger(EbookController.class);

    @Autowired
    private EbookService ebookService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResp<PageResp<EbookResp>> list(@ModelAttribute EbookReq req) {
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
}
