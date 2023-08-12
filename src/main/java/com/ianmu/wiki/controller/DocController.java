package com.ianmu.wiki.controller;

import com.ianmu.wiki.req.DocQueryReq;
import com.ianmu.wiki.req.DocSaveReq;
import com.ianmu.wiki.resp.CommonResp;
import com.ianmu.wiki.resp.DocQueryResp;
import com.ianmu.wiki.resp.PageResp;
import com.ianmu.wiki.service.DocService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("doc")
public class DocController {

    private static final Logger LOG = LoggerFactory.getLogger(DocController.class);

    @Autowired
    private DocService docService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public CommonResp<List<DocQueryResp>> all(DocQueryReq req) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(req);
        resp.setContent(list);
        return resp;
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResp<PageResp<DocQueryResp>> list(@Valid @ModelAttribute DocQueryReq req) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        return docService.save(req);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable String id) {
        List<Long> ids = new ArrayList<>();
        List<String> templateIds = Arrays.asList(id.split(","));
        templateIds.forEach(item -> {
            ids.add(Long.valueOf(item));
        });

        docService.delete(ids);
        return new CommonResp();
    }
}
