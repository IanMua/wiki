package com.ianmu.wiki.controller;

import com.ianmu.wiki.entity.Test;
import com.ianmu.wiki.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestService testService;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello World!";
    }

    @RequestMapping(value = "/helloM", method = RequestMethod.GET)
    public List<Test> helloM() {
        List<Test> list = testService.list();
        LOG.info("返回结果:\t", list);
        return list;
    }
}
