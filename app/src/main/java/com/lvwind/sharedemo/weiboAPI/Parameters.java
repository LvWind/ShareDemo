package com.lvwind.sharedemo.weiboAPI;

import android.content.Context;
import android.graphics.Bitmap;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.utils.Utility;

/**
 * Created by Shaw on 2015/6/20.
 */
public class Parameters {
    private String text;
    private Bitmap image;
    private String url;

    private Context mContext;

    private WeiboMultiMessage weiboMessage;

    public Parameters(Context context) {
        mContext = context;
        weiboMessage = new WeiboMultiMessage();
    }

    /**
     * 分享文本
     *
     * @param text
     */
    public void setText(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        weiboMessage.textObject = textObject;
    }


    /**
     * 设置分享网页信息
     *
     * @param title  网页标题
     * @param desc   网页描述
     * @param url    分享网址
     * @param bitmap 附带图片
     *               这些参数不可少，否则无法唤起客户端分享
     */
    public void setUrl(String title, String desc, String url, Bitmap bitmap) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = desc;
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = url;
        weiboMessage.mediaObject = mediaObject;
    }

    /**
     * 分享图片
     *
     * @param image
     */
    public void setImage(Bitmap image) {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(image);
        weiboMessage.imageObject = imageObject;
    }

    public WeiboMultiMessage getWeiboMessage() {
        return weiboMessage;
    }
}
