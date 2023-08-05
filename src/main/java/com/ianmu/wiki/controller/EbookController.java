package com.ianmu.wiki.controller;

import com.ianmu.wiki.req.EbookQueryReq;
import com.ianmu.wiki.req.EbookSaveReq;
import com.ianmu.wiki.resp.CommonResp;
import com.ianmu.wiki.resp.EbookQueryResp;
import com.ianmu.wiki.resp.PageResp;
import com.ianmu.wiki.service.EbookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ebook")
public class EbookController {

    private static final Logger LOG = LoggerFactory.getLogger(EbookController.class);

    @Autowired
    private EbookService ebookService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public CommonResp<List<EbookQueryResp>> all(@ModelAttribute EbookQueryReq req) {
        CommonResp<List<EbookQueryResp>> resp = new CommonResp<>();
        List<EbookQueryResp> list = ebookService.all(req);
        resp.setContent(list);
        return resp;
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResp<PageResp<EbookQueryResp>> list(@Valid @ModelAttribute EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp save(@Valid @RequestBody EbookSaveReq req) {
        return ebookService.save(req);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable Long id) {
        ebookService.delete(id);
        return new CommonResp();
    }
}
