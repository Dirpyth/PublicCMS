package com.publiccms.logic.component.exchange;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.base.AbstractExchange;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;

/**
 * SiteExchangeComponent 站点导入导出组件
 * 
 */
@Component
public class SiteExchangeComponent {
    protected final static Log log = LogFactory.getLog(SiteExchangeComponent.class);

    @Resource
    private List<AbstractExchange<?, ?>> exchangeList;

    /**
     * @param <E>
     * @param <D>
     * @param siteId
     * @param userId
     * @param overwrite
     * @param dataFileSuffix
     * @param exchangeComponent
     * @param file
     * @param model
     * @return
     */
    public static <E, D> String importData(short siteId, long userId, boolean overwrite, String dataFileSuffix,
            AbstractExchange<E, D> exchangeComponent, MultipartFile file, ModelMap model) {
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            if (originalName.endsWith(dataFileSuffix)) {
                String suffix = CmsFileUtils.getSuffix(originalName);
                try {
                    File dest = File.createTempFile("temp_import_", suffix);
                    file.transferTo(dest);
                    try (ZipFile zipFile = new ZipFile(dest, CommonConstants.DEFAULT_CHARSET_NAME)) {
                        exchangeComponent.importData(siteId, userId, overwrite, zipFile);
                    }
                    dest.delete();
                } catch (IOException e) {
                    log.error(e.getMessage());
                    model.addAttribute("error", e.getMessage());
                    return CommonConstants.TEMPLATE_ERROR;
                }
            } else {
                model.addAttribute("error", "verify.custom.fileType");
                return CommonConstants.TEMPLATE_ERROR;
            }
        } else {
            model.addAttribute("error", "verify.notEmpty.file");
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param siteId
     * @param zipOutputStream
     */
    public void exportAll(short siteId, ZipOutputStream zipOutputStream) {
        for (AbstractExchange<?, ?> exchange : exchangeList) {
            exchange.exportAll(siteId, exchange.getDirectory(), zipOutputStream);
        }
    }

    /**
     * @param siteId
     * @param userId
     * @param overwrite
     * @param dataFileSuffix
     * @param file
     * @param model
     * @return
     */
    public String importData(short siteId, long userId, boolean overwrite, String dataFileSuffix, MultipartFile file,
            ModelMap model) {
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            if (originalName.endsWith(dataFileSuffix)) {
                String suffix = CmsFileUtils.getSuffix(originalName);
                try {
                    File dest = File.createTempFile("temp_import_", suffix);
                    file.transferTo(dest);
                    try (ZipFile zipFile = new ZipFile(dest, CommonConstants.DEFAULT_CHARSET_NAME)) {
                        for (AbstractExchange<?, ?> exchange : exchangeList) {
                            exchange.importData(siteId, userId, exchange.getDirectory(), overwrite, zipFile);
                        }
                    }
                    dest.delete();
                } catch (IOException e) {
                    log.error(e.getMessage());
                    model.addAttribute("error", e.getMessage());
                    return CommonConstants.TEMPLATE_ERROR;
                }
            } else {
                model.addAttribute("error", "verify.custom.fileType");
                return CommonConstants.TEMPLATE_ERROR;
            }
        } else {
            model.addAttribute("error", "verify.notEmpty.file");
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}
