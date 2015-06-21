package com.lvwind.sharedemo;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
/**
 * Created by Shaw on 2015/6/20.
 */

public class ShareDialog extends PopupWindow {

    private Button btnWeibo;
    private Button btnWechat;

    private View mMenuView;
    public ShareDialog() {}

    public ShareDialog(Context context, View.OnClickListener itemsOnClick) {

        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.activity_share_dialog, null);
        btnWeibo = (Button) mMenuView.findViewById(R.id.btn_share_to_weibo);
        btnWechat = (Button) mMenuView.findViewById(R.id.btn_share_to_wechat);
        btnWeibo.setOnClickListener(itemsOnClick);
        btnWechat.setOnClickListener(itemsOnClick);

        this.setContentView(mMenuView);
        this.setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);

        //半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        //OnTouchListener
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.layout_dialog_share).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}
