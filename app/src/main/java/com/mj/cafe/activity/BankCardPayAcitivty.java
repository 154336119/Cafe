package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mj.cafe.BaseActivity;
import com.mj.cafe.R;

/**
 * 第三方支付
 */
public class BankCardPayAcitivty extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_pay);
    }
}
