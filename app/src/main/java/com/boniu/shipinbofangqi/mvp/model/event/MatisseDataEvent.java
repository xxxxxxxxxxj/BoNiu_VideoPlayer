package com.boniu.shipinbofangqi.mvp.model.event;

import android.net.Uri;

import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuVideoInfo;

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
    private List<BoNiuVideoInfo> videoList;

    public MatisseDataEvent(List<BoNiuVideoInfo> videoList) {
        this.videoList = videoList;
    }

    public MatisseDataEvent(List<Uri> uris, List<String> strings) {
        this.uris = uris;
        this.strings = strings;
    }

    public List<BoNiuVideoInfo> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<BoNiuVideoInfo> videoList) {
        this.videoList = videoList;
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
