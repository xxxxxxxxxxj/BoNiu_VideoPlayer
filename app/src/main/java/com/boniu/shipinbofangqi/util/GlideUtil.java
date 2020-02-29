package com.boniu.shipinbofangqi.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.boniu.shipinbofangqi.R;

/**
 * Glide图片加载工具类
 */
public class GlideUtil {
    /**
     * 加载第四秒的帧数作为封面
     * url就是视频的地址
     */
    public static void displayVideoCoverImg(Context mContext, String videoUrl,
                                            ImageView imageView) {
        if (StringUtil.isNotEmpty(videoUrl)) {
            Glide.with(mContext)
                    .setDefaultRequestOptions(
                            new RequestOptions()
                                    .frame(1000000)
                                    .centerCrop()
                                    .error(R.mipmap.ic_image_load)//可以忽略
                                    .placeholder(R.mipmap.ic_image_load)//可以忽略
                    )
                    .load(videoUrl)
                    .into(imageView);
        }
    }

    public static void displayImage(Context mContext, String imgUrl,
                                    ImageView imageView) {
        if (StringUtil.isNotEmpty(imgUrl)) {
            if (imgUrl.toUpperCase().endsWith(".GIF")) {
                Glide.with(mContext)
                        .asGif()
                        .load(imgUrl)
                        .placeholder(R.mipmap.ic_image_load)
                        .fitCenter()
                        .error(R.mipmap.ic_image_load)
                        .into(imageView);
            } else {
                Glide.with(mContext)
                        .load(imgUrl)
                        .placeholder(R.mipmap.ic_image_load)
                        .fitCenter()
                        .error(R.mipmap.ic_image_load)
                        .into(imageView);
            }
        }
    }

    public static void displayImage(Context mContext, String imgUrl,
                                    ImageView imageView, int imgWidth, int imgHeight) {
        if (StringUtil.isNotEmpty(imgUrl)) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.override(imgWidth, imgHeight);
            if (imgUrl.toUpperCase().endsWith(".GIF")) {
                Glide.with(mContext)
                        .asGif()
                        .load(imgUrl)
                        .apply(requestOptions)
                        .placeholder(R.mipmap.ic_image_load)
                        .fitCenter()
                        .error(R.mipmap.ic_image_load)
                        .into(imageView);
            } else {
                Glide.with(mContext)
                        .load(imgUrl)
                        .apply(requestOptions)
                        .placeholder(R.mipmap.ic_image_load)
                        .fitCenter()
                        .error(R.mipmap.ic_image_load)
                        .into(imageView);
            }
        }
    }
}
