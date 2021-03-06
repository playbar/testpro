package com.baofeng.mj.vrplayer.view;

import android.content.Context;

import com.baofeng.mj.R;
import com.baofeng.mj.vrplayer.activity.GLBaseActivity;
import com.baofeng.mj.vrplayer.utils.HeadControlUtil;
import com.baofeng.mj.vrplayer.utils.SkyboxManager;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;

/**
 * Created by yushaochen on 2017/4/5.
 */

public class SkyboxView extends GLLinearView{

    private Context mContext;
    private float mWidth = 1000f;
    private float mHeight = 285f;
    private static final int MAX = 3;

    private float mItemWidth = 216f;
    private float mItemHeight = 175f;

    private String selectedId = NO_SELECTED;

    public static final String NO_SELECTED = "-1";

    public SkyboxView(Context context) {
        super(context);
        mContext = context;
        setOrientation(GLConstant.GLOrientation.HORIZONTAL);

        //创建场景选项
        createItemView();
    }

    private void createItemView() {

        for(int x = 0; x < MAX; x++) {
            GLRelativeView glRelativeView = new GLRelativeView(mContext);
            glRelativeView.setId(x+"");
            glRelativeView.setLayoutParams(mItemWidth,mItemHeight);
            glRelativeView.setBackground(R.drawable.play_bg_scene_normal);

            GLImageView glImageView = new GLImageView(mContext);
            glImageView.setLayoutParams(200f,112f);
            glImageView.setMargin(8f,8f,8f,0f);
            glImageView.setId("image");

            GLTextView glTextView = new GLTextView(mContext);
            glTextView.setLayoutParams(216f,41f);
            glTextView.setMargin(0f,112f+14f,0f,0f);
            glTextView.setTextColor(new GLColor(0x888888));
            glTextView.setTextSize(28);
            glTextView.setAlignment(GLTextView.ALIGN_CENTER);
            glTextView.setId("text");

            if(x == 0) {
                glImageView.setBackground(R.drawable.play_bg_scene_ph_cinema);
                glTextView.setText("电影院");
            } else if(x == 1){
                glImageView.setBackground(R.drawable.play_bg_scene_ph_home);
                glTextView.setText("家庭影院");
                glRelativeView.setMargin(71f,0f,0f,0f);
            } else if(x == 2) {
                glImageView.setBackground(R.drawable.play_bg_scene_ph_outcinema);
                glTextView.setText("露天影院");
                glRelativeView.setMargin(71f,0f,0f,0f);
            }
            glRelativeView.addView(glImageView);
            glRelativeView.addView(glTextView);
            glRelativeView.setFocusListener(new GLViewFocusListener() {
                @Override
                public void onFocusChange(GLRectView view, boolean focused) {
                    refreshFocusView(view,focused);
                }
            });
            glRelativeView.setOnKeyListener(new GLOnKeyListener() {
                @Override
                public boolean onKeyDown(GLRectView view, int keycode) {
                    setSelected(view.getId());
                    SkyboxManager.getInstance().notifyChanged(Integer.parseInt(selectedId));
                    ((GLBaseActivity) getContext()).showSkyBox(Integer.parseInt(selectedId));
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
            HeadControlUtil.bindView(glRelativeView);
            addView(glRelativeView);
        }

    }

    public void refreshFocusView(GLRectView view, boolean focused) {
        if(!selectedId.equals(view.getId())) {
            if(view instanceof GLRelativeView) {
                if(focused) {
                    ((GLRelativeView) view).setBackground(R.drawable.play_bg_scene_hover);
                    GLRectView text = ((GLRelativeView) view).getView("text");
                    ((GLTextView) text).setTextColor(new GLColor(0xbbbbbb));
                } else {
                    ((GLRelativeView) view).setBackground(R.drawable.play_bg_scene_normal);
                    GLRectView text = ((GLRelativeView) view).getView("text");
                    ((GLTextView) text).setTextColor(new GLColor(0x888888));
                }
            }
        }
    }

    public void setSelected(String id) {
        refreshItemView(id);
    }

    public String getSelectedId() {
        return selectedId;
    }

    private void refreshItemView(String id) {
        if(!selectedId.equals(id)) {
            clearSlected();
            GLRectView view = getView(id);
            if(view instanceof GLRelativeView) {
                view.setBackground(R.drawable.play_bg_scene_normal);
                GLRectView text = ((GLRelativeView) view).getView("text");
                ((GLTextView) text).setTextColor(new GLColor(0x008cb3));
            }
            selectedId = id;
        }
    }

    private void clearSlected() {
        selectedId = NO_SELECTED;
        for(GLRectView childView : getChildView()) {
            if(childView instanceof GLRelativeView) {
                GLRectView text = ((GLRelativeView) childView).getView("text");
                ((GLTextView) text).setTextColor(new GLColor(0x888888));
            }
        }
    }
}
