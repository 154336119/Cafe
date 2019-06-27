package com.mj.cafe;

import android.arch.lifecycle.Observer;
import android.os.Build;
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
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.utils.SharedPreferencesUtil;
import com.mj.cafe.view.LoadingDialog;

import static com.mj.cafe.BizcContant.SP_LANAUAGE;
import static com.mj.cafe.BizcContant._CN;
import static com.mj.cafe.activity.ShopCarActivity.carAdapter;

public abstract class BaseActivity extends AppCompatActivity {
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
        //hideBottomUIMenu();
        if (rxBusRegist()){
            RxBus.get().register(this);
        }
        registerLiveDateBus();
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
    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void goAccount(View view) {
    }

    //发送语言变化事件
    public void postLangLiveData(LangTypeBean langType){
        SharedPreferencesUtil.putData(SP_LANAUAGE,langType);
        LiveEventBus.get().with(LangTypeBean.KEY_LANG_OBSERVE).post(langType);
    }

    public void registerLiveDateBus(){
        LiveEventBus.get().with(LangTypeBean.KEY_LANG_OBSERVE,LangTypeBean.class)
                .observe(this, new Observer<LangTypeBean>() {
                    @Override
                    public void onChanged(@Nullable LangTypeBean langTypeBean) {
                        setLangView(langTypeBean);
                        langChangeForHttp();
                    }
                });
    }
    public abstract void setLangView(LangTypeBean langTypeBean);
    //语言切换时候http的请求的操作
    public void langChangeForHttp(){};
}
