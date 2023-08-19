package com.ianmu.wiki.service;

import com.github.pagehelper.PageInfo;
import com.ianmu.wiki.entity.WikiUser;
import com.ianmu.wiki.entity.WikiUserExample;
import com.ianmu.wiki.exception.BusinessException;
import com.ianmu.wiki.exception.BusinessExceptionCode;
import com.ianmu.wiki.mapper.WikiUserMapper;
import com.ianmu.wiki.req.UserLoginReq;
import com.ianmu.wiki.req.WikiUserQueryReq;
import com.ianmu.wiki.req.WikiUserResetPasswordReq;
import com.ianmu.wiki.req.WikiUserSaveReq;
import com.ianmu.wiki.resp.CommonResp;
import com.ianmu.wiki.resp.PageResp;
import com.ianmu.wiki.resp.UserLoginResp;
import com.ianmu.wiki.resp.WikiUserQueryResp;
import com.ianmu.wiki.utils.CopyUtil;
import com.ianmu.wiki.utils.SnowFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class WikiUserService {

    private final Logger LOG = LoggerFactory.getLogger(WikiUserService.class);

    @Autowired
    private WikiUserMapper wikiUserMapper;

    @Autowired
    private SnowFlow snowFlow;

    public List<WikiUserQueryResp> all(WikiUserQueryReq req) {
        WikiUserExample wikiUserExample = new WikiUserExample();
        WikiUserExample.Criteria criteria = wikiUserExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameLike("%" + req.getLoginName() + "%");
        }
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameEqualTo("%" + req.getName() + "%");
        }
        List<WikiUser> wikiUserList = wikiUserMapper.selectByExample(wikiUserExample);

        return CopyUtil.copyList(wikiUserList, WikiUserQueryResp.class);
    }

    public PageResp<WikiUserQueryResp> list(WikiUserQueryReq req) {
        WikiUserExample wikiUserExample = new WikiUserExample();
        WikiUserExample.Criteria criteria = wikiUserExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameLike("%" + req.getLoginName() + "%");
        }
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameEqualTo("%" + req.getName() + "%");
        }
        List<WikiUser> wikiUserList = wikiUserMapper.selectByExample(wikiUserExample);
        PageInfo<WikiUser> pageInfo = new PageInfo<>(wikiUserList);
        List<WikiUserQueryResp> wikiUserQueryRespList = CopyUtil.copyList(wikiUserList, WikiUserQueryResp.class);

        PageResp<WikiUserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(wikiUserQueryRespList);

        return pageResp;
    }

    public CommonResp save(WikiUserSaveReq req) {
        CommonResp commonResp = new CommonResp();
        if (ObjectUtils.isEmpty(req.getId())) {

            WikiUser user = this.queryByLoginName(req.getLoginName());
            if (!ObjectUtils.isEmpty(user)) {
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }

            req.setId(snowFlow.nextId());
            wikiUserMapper.insert(CopyUtil.copy(req, WikiUser.class));
        } else {
            //更新
            WikiUser user = CopyUtil.copy(req, WikiUser.class);
            user.setLoginName(null);
            user.setPassword(null);
            wikiUserMapper.updateByPrimaryKeySelective(user);
        }
        return commonResp;
    }

    public void delete(Long id) {
        wikiUserMapper.deleteByPrimaryKey(id);
    }

    public WikiUser queryByLoginName(String loginName) {
        WikiUserExample wikiUserExample = new WikiUserExample();
        WikiUserExample.Criteria criteria = wikiUserExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<WikiUser> userList = wikiUserMapper.selectByExample(wikiUserExample);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    public CommonResp resetPassword(WikiUserResetPasswordReq req) {
        CommonResp commonResp = new CommonResp();
        WikiUser user = CopyUtil.copy(req, WikiUser.class);
        wikiUserMapper.updateByPrimaryKeySelective(user);
        return commonResp;
    }

    public UserLoginResp login(UserLoginReq req) {
        WikiUser userDb = queryByLoginName(req.getLoginName());
        if (ObjectUtils.isEmpty(userDb)) {
            LOG.info("用户名不存在，{}", req.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            if (userDb.getPassword().equals(req.getPassword())) {
                return CopyUtil.copy(userDb, UserLoginResp.class);
            } else {
                LOG.info("密码不正确，输入密码：{}，数据库密码：{}", req.getPassword(), userDb.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }
}
