package com.baofeng.mj.vrplayer.view;

import android.content.Context;

import com.baofeng.mj.R;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLTextView;

/**
 * Created by yushaochen on 2017/4/7.
 */

public class SettingBottomRightView extends GLLinearView{

    private Context mContext;

    private GLImageView icon;
    private GLTextView textView;
    private GLImageView rightIcon;

    public SettingBottomRightView(Context context) {
        super(context);
        mContext = context;
        setOrientation(GLConstant.GLOrientation.HORIZONTAL);
        setLayoutParams(890,80);
        setBackground(R.drawable.play_bg_control_normal_890_80);
        //创建view
        createView();
    }

    private void createView() {
        icon = new GLImageView(mContext);
        icon.setLayoutParams(40,40);
        icon.setBackground(R.drawable.play_icon_setting_scene_normal);
        icon.setMargin(348f,20f,0f,0f);
        addView(icon);

        textView = new GLTextView(mContext);
        textView.setLayoutParams(160f,40f);
        textView.setTextSize(32);
        textView.setTextColor(new GLColor(0x888888));
        textView.setAlignment(GLTextView.ALIGN_CENTER);
        textView.setText("场景选择");
        textView.setMargin(30f,20f,0f,0f);
        addView(textView);

        rightIcon = new GLImageView(mContext);
        rightIcon.setLayoutParams(40,40);
        rightIcon.setBackground(R.drawable.play_icon_setting_go_normal);
        rightIcon.setMargin(232f,20f,0f,0f);
        addView(rightIcon);
        setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    setBackground(R.drawable.play_bg_control_hover_890_80);
                    icon.setBackground(R.drawable.play_icon_setting_scene_hover);
                    textView.setTextColor(new GLColor(0x888888));
                    rightIcon.setBackground(R.drawable.play_icon_setting_go_hover);
                } else {
                    setBackground(R.drawable.play_bg_control_normal_890_80);
                    icon.setBackground(R.drawable.play_icon_setting_scene_normal);
                    textView.setTextColor(new GLColor(0xbbbbbb));
                    rightIcon.setBackground(R.drawable.play_icon_setting_go_normal);
                }
            }
        });
    }

//    public void setKeyListener(GLOnKeyListener listener) {
//        setOnKeyListener(listener);
//    }
}
