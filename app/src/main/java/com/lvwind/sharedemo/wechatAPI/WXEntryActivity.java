package com.lvwind.sharedemo.wechatAPI;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Shaw on 2015/6/20.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXShare.getInstance(this).handleIntent(getIntent(), this);
    }

    /**
     * 只有appdata被点击时可以调用第三方应用的进程，所触发的方法是实现了IWXAPIEventHandler接口的类的onReq方法，
     * 类型是ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX
     * 而在微信中和好友聊天时点击第三方应用图标所所触发的方法是实现了IWXAPIEventHandler接口的类的onReq方法，
     * 类型是ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX
     */
    @Override
    public void onReq(BaseReq req) {

        switch (req.getType()) {
            // 微信内部调加号内的第三方应用
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                goToGetMsg();
                break;
            // 点击分享的appdata型消息调用
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        String result = "default";

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "微信分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消分享";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "分享被拒绝";
                break;
            case BaseResp.ErrCode.ERR_COMM:
                result = "一般错误";
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                result = "分享失败";
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = "不支持分享";
                break;
            default:
                result = "未知错误";
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * 从微信启动第三方应用
     */
    private void goToGetMsg() {
		/*
		 * Intent intent = new Intent(this, MainActivity.class);
		 * startActivity(intent); finish();
		 */
    }

    /**
     * 点击微信分享的内容后响应，如跳第三方应用
     *
     * @param showReq
     */
    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
        finish();
    }

}
