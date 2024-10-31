package com.simple.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("GlobalVariables")
public class GlobalVariables {

    @Value("${default.cms.api-host:https://api-cms.mirae-n.com}")
    public void setCmsApitUrl(String cmsApiUrl) {
        CmsValue.CMS_API_URL = cmsApiUrl;
    }

    @Value("${app.api.path.cms_view_url:/cms/files/view/}")
    public void setCmsViewUrl(String cmsViewUrl) {
        CmsValue.CMS_VIEW_URL = cmsViewUrl;
    }

    @Value("${app.api.path.cms_download_url:/cms/files/download/}")
    public void setCmsDownloadUrl(String cmsDownloadUrl) { CmsValue.CMS_DOWNLOAD_URL = cmsDownloadUrl; }

    @Value("${app.api.default.cms.service:mteacher}")
    public void setCmsService(String cmsService) { CmsValue.CMS_SERVICE = cmsService; }

    public static class CmsValue {
        public static String CMS_API_URL;
        public static String CMS_VIEW_URL;
        public static String CMS_DOWNLOAD_URL;
        public static String CMS_SERVICE;
    }

    @Value("${app.api.default.local.cms_root:'Z:\\workspace'}")
    public void setLocalValue(String localValue) { LocalValue.CMS_ROOT = localValue; }

    public static class LocalValue {
        public static String CMS_ROOT;
    }
}
