package com.mdx.framework.util;

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
}
