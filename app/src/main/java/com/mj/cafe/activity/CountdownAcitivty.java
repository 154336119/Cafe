package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mj.cafe.BaseActivity;
import com.mj.cafe.R;
import com.mj.cafe.bean.LangTypeBean;

/**
 * 失败页面
 */
public class CountdownAcitivty extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_failed);
    }

    @Override
    public void setLangView(LangTypeBean langTypeBean) {

    }
}
