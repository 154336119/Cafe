package com.mj.cafe.retorfit.rxjava;


import android.text.TextUtils;

import com.mj.cafe.BaseActivity;

import rx.Subscriber;

/**
 * 描述：
 * Created by Lee
 * on 2017/1/19.
 */
public class BaseSubscriber<T> extends Subscriber<T> {
    /**MVP view接口*/
    private BaseActivity mView;
    public BaseSubscriber(BaseActivity view) {
        mView = view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if(!mView.isFinishing()){
            mView.showWaitDialog("loading...");
        }
    }

    @Override
    public void onError(Throwable e) {
        mView.hideWaitDialog();
        if(!TextUtils.isEmpty(e.getMessage())){
            mView.showToastMsg(e.getMessage());
        }
    }

    @Override
    public void onNext(T t) {
        if(!mView.isFinishing()){
            mView.hideWaitDialog();
        }
    }

    @Override
    public void onCompleted() {

    }

}
