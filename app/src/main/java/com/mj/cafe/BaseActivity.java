package com.mj.cafe;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;

public class BaseActivity extends AppCompatActivity {
    private Toast mToast;
    /** 加载等待框 */
    private LoadingDialog mLoadingDialog;
    /**
     * 是否开启rxbus注册以及销毁时取消注册
     * @return
     */
    protected boolean rxBusRegist(){return false;}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (rxBusRegist()){
            RxBus.get().register(this);
        }
    }

    /**
     * 系统toast提示
     *
     * @param msg
     */
    public void showToastMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast = new Toast(BaseActivity.this);
                View view = LayoutInflater.from(BaseActivity.this).inflate(R.layout.frame_view_transient_notification, null, false);
                TextView textView = view.findViewById(R.id.message);
                textView.setText(msg);
                mToast.setView(view);
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
            }
        });
    }

    /**
     * 显示加载框
     *
     * @param msg
     */
    public void showWaitDialog(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.setText(msg);
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    /** 隐藏加载等待框 */
    public void hideWaitDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        if (rxBusRegist()){
            RxBus.get().unregister(this);
        }
    }
}
