package com.boniu.shipinbofangqi.mvp.model.event;

import android.net.Uri;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-25 13:57
 */
public class MatisseDataEvent {
    private List<Uri> uris;
    private List<String> strings;
    private String videoUrl;

    public MatisseDataEvent(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public MatisseDataEvent(List<Uri> uris, List<String> strings) {
        this.uris = uris;
        this.strings = strings;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<Uri> getUris() {
        return uris;
    }

    public void setUris(List<Uri> uris) {
        this.uris = uris;
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }
}
