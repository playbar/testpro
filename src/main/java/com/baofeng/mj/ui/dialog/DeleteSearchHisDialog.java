package com.baofeng.mj.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baofeng.mj.R;

/**
 * Created by sunshine on 16/9/21.
 * 删除搜索记录对话框
 */
public class DeleteSearchHisDialog {
    private Dialog subCancleDialog;//取消对话框

    /**
     * 创建取消对话框
     *
     * @param context
     */
    public void createCancleDialog(final Context context, final CancleBusiness cancleCallBack) {
        subCancleDialog = new Dialog(context, R.style.cancle_sub_style);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_delete_search_his, null);
        subCancleDialog.setContentView(view);
        subCancleDialog.setCancelable(true);
        final TextView cancle_btn = (TextView) view.findViewById(R.id.cancle_btn);
        TextView ok_btn = (TextView) view.findViewById(R.id.ok_btn);
        //取消按钮点击事件
        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        //确定按钮点击事件
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                cancleCallBack.cancleCallBack();
            }
        });
    }

    /**
     * 显示对话框
     */
    public void showDialog(Context context, CancleBusiness cancleCallBack) {
        if (subCancleDialog == null) {
            createCancleDialog(context, cancleCallBack);
        }
        if (!subCancleDialog.isShowing()) {
            subCancleDialog.show();
        }
    }

    /**
     * 隐藏对话框
     */
    public void dismissDialog() {
        if (subCancleDialog != null && subCancleDialog.isShowing()) {
            subCancleDialog.dismiss();
        }
    }

    public interface CancleBusiness {
        void cancleCallBack();
    }
}
