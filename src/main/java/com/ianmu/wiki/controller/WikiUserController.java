package com.ianmu.wiki.controller;

import com.ianmu.wiki.req.UserLoginReq;
import com.ianmu.wiki.req.WikiUserQueryReq;
import com.ianmu.wiki.req.WikiUserResetPasswordReq;
import com.ianmu.wiki.req.WikiUserSaveReq;
import com.ianmu.wiki.resp.CommonResp;
import com.ianmu.wiki.resp.UserLoginResp;
import com.ianmu.wiki.resp.WikiUserQueryResp;
import com.ianmu.wiki.resp.PageResp;
import com.ianmu.wiki.service.WikiUserService;
import com.ianmu.wiki.utils.SnowFlow;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("user")
public class WikiUserController {

    private static final Logger LOG = LoggerFactory.getLogger(WikiUserController.class);

    @Autowired
    private WikiUserService wikiUserService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SnowFlow snowFlow;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public CommonResp<List<WikiUserQueryResp>> all(@ModelAttribute WikiUserQueryReq req) {
        CommonResp<List<WikiUserQueryResp>> resp = new CommonResp<>();
        List<WikiUserQueryResp> list = wikiUserService.all(req);
        resp.setContent(list);
        return resp;
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResp<PageResp<WikiUserQueryResp>> list(@Valid @ModelAttribute WikiUserQueryReq req) {
        CommonResp<PageResp<WikiUserQueryResp>> resp = new CommonResp<>();
        PageResp<WikiUserQueryResp> list = wikiUserService.list(req);
        resp.setContent(list);
        return resp;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp save(@Valid @RequestBody WikiUserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        return wikiUserService.save(req);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable Long id) {
        wikiUserService.delete(id);
        return new CommonResp();
    }

    @RequestMapping(value = "/reset/password", method = RequestMethod.POST)
    public CommonResp resetPassword(@Valid @RequestBody WikiUserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        return wikiUserService.resetPassword(req);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResp<UserLoginResp> login(@Valid @RequestBody UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = wikiUserService.login(req);

        Long token = snowFlow.nextId();
        LOG.info("生成单点登录token: {}，放入redis中", token);
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token, userLoginResp, 3600 * 24, TimeUnit.SECONDS);
        resp.setContent(userLoginResp);
        return resp;
    }
}
