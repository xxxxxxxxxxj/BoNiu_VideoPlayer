package com.boniu.shipinbofangqi.mvp.model.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-09 19:35
 */
public class AppInfoBean {
    private VersionInfoVo versionInfoVo;

    @Override
    public String toString() {
        return "AppInfoBean{" +
                "versionInfoVo=" + versionInfoVo +
                '}';
    }

    public VersionInfoVo getVersionInfoVo() {
        return versionInfoVo;
    }

    public void setVersionInfoVo(VersionInfoVo versionInfoVo) {
        this.versionInfoVo = versionInfoVo;
    }

    public static class VersionInfoVo {
        private String content;
        private String linkUrl;
        private String version;
        private boolean forceUp;
        private String title;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public boolean isForceUp() {
            return forceUp;
        }

        public void setForceUp(boolean forceUp) {
            this.forceUp = forceUp;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "VersionInfoVo{" +
                    "content='" + content + '\'' +
                    ", linkUrl='" + linkUrl + '\'' +
                    ", version='" + version + '\'' +
                    ", forceUp=" + forceUp +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}
