package com.ianmu.wiki.controller;

import com.ianmu.wiki.entity.Ebook;
import com.ianmu.wiki.service.EbookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("ebook")
public class EbookController {

    private static final Logger LOG = LoggerFactory.getLogger(EbookController.class);

    @Autowired
    private EbookService ebookService;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello World!";
    }

    @RequestMapping(value = "/helloM", method = RequestMethod.GET)
    public List<Ebook> helloM() {
        List<Ebook> list = ebookService.list();
        LOG.info("返回结果:\t", list);
        return list;
    }
}
