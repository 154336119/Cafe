package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mj.cafe.BaseActivity;
import com.mj.cafe.BizcContant;
import com.mj.cafe.R;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.utils.SharedPreferencesUtil;

/**
 * 第三方支付
 */
public class BankCardPayAcitivty extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_pay);
        setLangView((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE,new LangTypeBean(LangTypeBean.DEFAULT)));
    }

    @Override
    public void setLangView(LangTypeBean langTypeBean) {

    }
}
