package com.baofeng.mj.vrplayer.view;

import android.content.Context;
import android.text.TextUtils;

import com.baofeng.mj.R;
import com.baofeng.mj.business.publicbusiness.BaseApplication;
import com.baofeng.mj.vrplayer.utils.HeadControlUtil;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wanghongfang on 2017/4/14.
 * 单个按钮的对话框
 */

public class GLDialogViewSingleBtn extends GLRelativeView{

    private Context mContext;

    private int width = 800;
    private int height = 200;
    private int btnWidth = 240;
    private int btnHeight = 70;

    private GLTextView textView;
    private GLTextView leftBtn;

    private boolean isContinuBtn;
    public GLDialogViewSingleBtn(Context context) {
        super(context);
        mContext = context;
        setLayoutParams(width,height);
        setBackground(new GLColor(0x000000));
        //创建需要显示的view
        createView();
        setVisible(false);
    }

    private void createView() {
        //创建文案显示
        textView = new GLTextView(mContext);
        textView.setLayoutParams(width,100);
        textView.setTextSize(30);
        textView.setTextColor(new GLColor(0x888888));
        textView.setAlignment(GLTextView.ALIGN_CENTER);
        textView.setPadding(0,20,0,0);
        textView.setText(getContext().getResources().getString(R.string.player_load_failed));
        addView(textView);
        //创建退出按钮
        leftBtn = new GLTextView(mContext);
        leftBtn.setLayoutParams(btnWidth,btnHeight);
        leftBtn.setBackground(R.drawable.play_button_bg_ok_normal);
        leftBtn.setMargin((800f-240f)/2,60f+70f,0f,0f);
        leftBtn.setTextSize(32);
        leftBtn.setTextColor(new GLColor(0x888888));
        leftBtn.setAlignment(GLTextView.ALIGN_CENTER);
        leftBtn.setText("重试");
        leftBtn.setPadding(0f,10f,0f,20f);
        leftBtn.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    leftBtn.setBackground(R.drawable.play_button_bg_ok_hover);
                    leftBtn.setTextColor(new GLColor(0x19191a));
                } else {
                    leftBtn.setBackground(R.drawable.play_button_bg_ok_normal);
                    leftBtn.setTextColor(new GLColor(0x888888));
                }
            }
        });
        HeadControlUtil.bindView(leftBtn);
        addView(leftBtn);
        setListener();
    }

    private void setListener(){
        leftBtn.setOnKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                GLDialogViewSingleBtn.this.setVisible(false);
                return false;
            }

            @Override
            public boolean onKeyUp(GLRectView view, int keycode) {
                return false;
            }

            @Override
            public boolean onKeyLongPress(GLRectView view, int keycode) {
                return false;
            }
        });
    }

    public void setTitleText(String text,boolean showBtn) {
        if(!TextUtils.isEmpty(text)) {
            textView.setText(text);
        }
        setBtnVisiable(showBtn);
    }

    public void setBtnVisiable(boolean visiable){
        if(leftBtn!=null){
            leftBtn.setVisible(visiable);
        }
    }

    public void setLeftText(String text) {
        if(!TextUtils.isEmpty(text)) {
            leftBtn.setText(text);
        }
    }

    public boolean getIsContinueBtn(){
        return isContinuBtn;
    }

    //播放loading超时 提示框
    public void showNetworkTips(int duration, boolean ishowBtn,final IGLDismissListener listener){
        setTitleText(BaseApplication.INSTANCE.getResources().getString(R.string.player_exception_tips),ishowBtn);
        setDelayVisiable(duration,listener);
    }

    private Timer timer;

    public void setDelayVisiable(int duration,final IGLDismissListener listener){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                GLDialogViewSingleBtn.this.setVisible(false);
                listener.dismiss();
            }
        };
        timer.schedule(task,duration);
    }


    public interface IGLDismissListener{
        void dismiss();
    }

}
