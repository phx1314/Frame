package com.mdx.framework.utility;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mdx.framework.Frame;

/**
 * Created by DELL on 2019/10/16.
 */

public class GlideLoader {
    public static void loadImage(String url, ImageView img) {
        Glide.with(Frame.CONTEXT).load(url).into(img);
    }

    public static void loadImage(String url, ImageView img, int i) {
        Glide.with(Frame.CONTEXT).load(url).error(i) //异常时候显示的图片
                .placeholder(i) //加载成功前显示的图片
                .fallback(i).into(img);
    }

}
