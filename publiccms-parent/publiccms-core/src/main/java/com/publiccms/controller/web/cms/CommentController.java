package com.publiccms.controller.web.cms;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.Config;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsComment;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCommentService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;

import freemarker.template.TemplateException;

/**
 * 
 * ContentController 内容
 *
 */
@Controller
@RequestMapping("comment")
public class CommentController {
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    protected SiteConfigComponent siteConfigComponent;
    @Autowired
    protected ConfigComponent configComponent;
    @Autowired
    private CmsContentService contentService;
    @Autowired
    private TemplateComponent templateComponent;

    private String[] ignoreProperties = new String[] { "siteId", "userId", "createDate", "checkUserId", "checkDate", "replyId",
            "replyUserId", "replies", "scores", "disabled" };

    /**
     * 使用示例
     * <p>
     * 
     * <pre>
     * &lt;#if user?has_content&gt;
     * &lt;form method="post" action="${site.dynamicPath}comment/save"&gt;
            &lt;@cms.comment id=id&gt;
                &lt;input type="hidden" name="id" value="${object.id}"/&gt;
                &lt;#assign text=object.text!/&gt;
            &lt;/@cms.comment&gt;
            &lt;input type="hidden" name="replyId" value="${replyId!}"/&gt;
            &lt;input type="hidden" name="_csrf" value="&lt;@tools.csrfToken/&gt;"/&gt;
            &lt;input type="hidden" name="contentId" value="${content.id}"/&gt;
            &lt;input name="returnUrl" type="hidden" value="${content.url!}" /&gt;
            &lt;p&gt;&lt;label&gt;Comment:&lt;/label&gt;&lt;/p&gt;
            &lt;p&gt;&lt;textarea name="text" cols="100" rows="4" maxlength="1000"&gt;${text!}&lt;/textarea&gt;&lt;/p&gt;
            &lt;p&gt;&lt;input type="submit" value="Submit"/&gt;&lt;/p&gt;
        &lt;/form&gt;
        &lt;/#if&gt;
     * </pre>
     * 
     * @param site
     * @param user
     * @param entity
     * @param returnUrl
     * @param request
     * @return redirect url
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser user, CmsComment entity, String returnUrl,
            HttpServletRequest request) {
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        CmsContent content = null;
        if (CommonUtils.notEmpty(entity.getText())) {
            Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
            boolean needCheck = ConfigComponent.getBoolean(config.get(SiteConfigComponent.CONFIG_COMMENT_NEED_CHECK), true);
            boolean needStatic = ConfigComponent.getBoolean(config.get(SiteConfigComponent.CONFIG_STATIC_AFTER_COMMENT), false);
            entity.setStatus(CmsCommentService.STATUS_PEND);
            if (null != entity.getId()) {
                CmsComment oldEntity = service.getEntity(entity.getId());
                if (null != oldEntity && !oldEntity.isDisabled()
                        && (oldEntity.getUserId() == user.getId() || user.isSuperuser())) {
                    entity.setUpdateDate(CommonUtils.getDate());
                    entity = service.update(entity.getId(), entity, ignoreProperties);
                    logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(),
                            LogLoginService.CHANNEL_WEB, "update.cmsComment", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), JsonUtils.getString(entity)));
                }
            } else {
                Date now = CommonUtils.getDate();
                entity.setSiteId(site.getId());
                entity.setUserId(user.getId());
                entity.setDisabled(false);
                entity.setReplies(0);
                if (!needCheck) {
                    entity.setStatus(CmsCommentService.STATUS_NORMAL);
                    content = contentService.updateComments(site.getId(), entity.getContentId(), 1);
                }
                if (null != entity.getReplyId()) {
                    CmsComment reply = service.getEntity(entity.getReplyId());
                    if (null == reply) {
                        entity.setReplyId(null);
                    } else {
                        entity.setContentId(reply.getContentId());
                        if (null == entity.getReplyUserId()) {
                            entity.setReplyUserId(reply.getUserId());
                        }
                    }
                }
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                        "save.cmsComment", RequestUtils.getIpAddress(request), now, JsonUtils.getString(entity)));
            }
            if (needStatic && CmsCommentService.STATUS_NORMAL == entity.getStatus()) {
                if (null == content) {
                    content = contentService.getEntity(entity.getContentId());
                }
                if (null != content && !content.isDisabled()) {
                    try {
                        templateComponent.createContentFile(site, content, null, null);
                    } catch (IOException | TemplateException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    @Autowired
    private CmsCommentService service;

}
