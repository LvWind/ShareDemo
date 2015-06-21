package com.lvwind.sharedemo.weiboAPI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by Shaw on 2015/6/20.
 */
public class API {
    private static final String TAG = "weiboAPI";

    private Activity mActivity;
    private AuthInfo mAuthInfo;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private IWeiboShareAPI mWeiboShareAPI;

    public API(Activity activity) {
        mActivity = activity;
        mAuthInfo = new AuthInfo(activity, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(activity, mAuthInfo);
        mAccessToken = TokenKeeper.readAccessToken(mActivity);
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mActivity, Constants.APP_KEY);
        mWeiboShareAPI.registerApp(); // 将应用注册到微博客户端

        mWeiboShareAPI.handleWeiboResponse(new Intent(), new IWeiboHandler.Response() {
            @Override
            public void onResponse(BaseResponse baseResponse) {

            }
        });
    }

    public IWeiboShareAPI getWeiboShareAPI() {
        return mWeiboShareAPI;
    }

    public void share(Parameters parameters) {
        Log.d(TAG, "share...");
        checkToken();

        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = parameters.getWeiboMessage();
        mWeiboShareAPI.sendRequest(mActivity, request, mAuthInfo, mAccessToken.getToken(), new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {

                Log.d(TAG, "complete");
            }

            @Override
            public void onWeiboException(WeiboException e) {

                Log.d(TAG, "exception");
            }

            @Override
            public void onCancel() {

                Log.d(TAG, "cancel");
            }
        });

//        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
//        request.transaction = String.valueOf(System.currentTimeMillis());
//        WeiboMessage weiboMessage = new WeiboMessage();
//        weiboMessage.mediaObject = getTextObj();
//        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
//        mWeiboShareAPI.sendRequest(mActivity, request);

    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "test";
        return textObject;
    }

    private void checkToken() {
        if (mAccessToken.isSessionValid()) {
            Log.d(TAG, "isSessionValid");
        } else {
            mSsoHandler.authorize(new AuthListener());
        }
    }

    public SsoHandler getSsoHandler() {
        return mSsoHandler;
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {


        @Override
        public void onComplete(Bundle bundle) {
            Log.d(TAG, "onComplete...");
            mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
            TokenKeeper.writeAccessToken(mActivity, mAccessToken);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.d(TAG, "onWeiboException...");
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel...");

        }
    }


}
