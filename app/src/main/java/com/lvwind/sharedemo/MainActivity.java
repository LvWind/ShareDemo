package com.lvwind.sharedemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lvwind.sharedemo.wechatAPI.WXShare;
import com.lvwind.sharedemo.weiboAPI.API;
import com.lvwind.sharedemo.weiboAPI.Parameters;
/**
 * Created by Shaw on 2015/6/20.
 */

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "main";
    private ShareDialog menuView;
    private API mWeiboAPI;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.shareText);
        mWeiboAPI = new API(MainActivity.this);


    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_about) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void actionShare(View v) {
        menuView = new ShareDialog(MainActivity.this, itemsOnClick);
        //show form
        menuView.showAtLocation(MainActivity.this.findViewById(R.id.main), Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "requestCode:" + requestCode + ", resultCode:" + resultCode + ", data:" + data);
        // SSO redirect
        if (mWeiboAPI.getSsoHandler() != null) {
            mWeiboAPI.getSsoHandler().authorizeCallBack(requestCode, resultCode, data);
        }
    }


    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuView.dismiss();
            switch (v.getId()) {
                case R.id.btn_share_to_weibo:
                    Toast.makeText(MainActivity.this, "weibo", Toast.LENGTH_LONG).show();
                    Parameters parameters = new Parameters(MainActivity.this);
                    parameters.setText(mEditText.getText().toString());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lvwind);
                    parameters.setImage(bitmap);
                    parameters.setUrl("lvwind.com","lvwind's page","https://lvwind.com",bitmap);
                    mWeiboAPI.share(parameters);
                    break;
                case R.id.btn_share_to_wechat:
                    Toast.makeText(MainActivity.this, "wechat", Toast.LENGTH_LONG).show();
                    WXShare.getInstance(MainActivity.this).shareTextMessage(true, "分享文本消息内容", mEditText.getText().toString());
                    break;
                case R.id.btn_share_to_fb:
                    Toast.makeText(MainActivity.this, "facebook", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
}
