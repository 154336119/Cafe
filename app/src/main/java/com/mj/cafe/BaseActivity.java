package com.mj.cafe;

import android.arch.lifecycle.Observer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.mj.cafe.activity.MainActivity;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.utils.ActivityUtil;
import com.mj.cafe.utils.SharedPreferencesUtil;
import com.mj.cafe.view.LoadingDialog;
import com.orhanobut.logger.Logger;

import static com.mj.cafe.BizcContant.SP_LANAUAGE;
import static com.mj.cafe.BizcContant._CN;
import static com.mj.cafe.activity.ShopCarActivity.carAdapter;

public abstract class BaseActivity extends AppCompatActivity {
    public final String TAG = getClass().getSimpleName();
    private Toast mToast;
    /** 加载等待框 */
    private LoadingDialog mLoadingDialog;
    /**
     * 是否开启rxbus注册以及销毁时取消注册
     * @return
     */
    private int num = 0;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            num++;
            Logger.d(TAG+"____"+num);
            if(num<60){
                startTimeDown();
            }else{
                handler.removeMessages(0);
                ActivityUtil.next(BaseActivity.this, MainActivity.class);
            }
            return true;
        }
    });
    protected boolean rxBusRegist(){return false;}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        // 隐藏标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // 隐藏状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                // 全屏显示，隐藏状态栏和导航栏，拉出状态栏和导航栏显示一会儿后消失。
//                getWindow().getDecorView().setSystemUiVisibility(
//                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//            } else {
//                // 全屏显示，隐藏状态栏
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//            }
//        }


        hideNavigationBar();
        if (rxBusRegist()){
            RxBus.get().register(this);
        }
        registerLiveDateBus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
        if(setOpenTimeDown()){
            startTimeDown();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//                    Logger.d(TAG+"===================================onStop");
//        if(setOpenTimeDown()){
//            Logger.d(TAG+"===================================onStop");
//            resetTimeDown();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d(TAG+"===================================onPause");
        if(setOpenTimeDown()){
            Logger.d(TAG+"===================================onStop");
            resetTimeDown();
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
    /**
     * 隐藏虚拟按键，并且全屏
     */
    public void hideNavigationBar() {
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


    public void startTimeDown(){
        handler.sendEmptyMessageDelayed(0,1000);
    }

    public void resetTimeDown(){
        num =0;
//        handler.removeMessages(0);
        handler.removeCallbacksAndMessages(null);
    }

    protected boolean setOpenTimeDown(){return false;}

}
