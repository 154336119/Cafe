package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mj.cafe.BaseActivity;
import com.mj.cafe.R;
import com.mj.cafe.bean.LangTypeBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mj.cafe.bean.LangTypeBean.CN;
import static com.mj.cafe.bean.LangTypeBean.EN;
import static com.mj.cafe.bean.LangTypeBean.KO;

/**
 * 第三方支付
 */
public class PaySuccessAcitivty extends BaseActivity {
    @BindView(R.id.IvBack)
    ImageView IvBack;
    @BindView(R.id.IvHanYu)
    ImageView IvHanYu;
    @BindView(R.id.IvZhongWen)
    ImageView IvZhongWen;
    @BindView(R.id.IvYingYu)
    ImageView IvYingYu;
    @BindView(R.id.IvLogo)
    ImageView IvLogo;
    @BindView(R.id.TvSuccessTips)
    TextView TvSuccessTips;
    @BindView(R.id.TvFoodNumTips)
    TextView TvFoodNumTips;
    @BindView(R.id.TvFoodNum)
    TextView TvFoodNum;
    @BindView(R.id.LLWaiMai)
    LinearLayout LLWaiMai;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.IvBack, R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.IvZhongWen:
                postLangLiveData(new LangTypeBean(CN));
                break;
            case R.id.IvHanYu:
                postLangLiveData(new LangTypeBean(KO));
                break;
            case R.id.IvYingYu:
                postLangLiveData(new LangTypeBean(EN));
                break;
            case R.id.IvBack:
                finish();
                break;
        }
    }

    @Override
    public void setLangView(LangTypeBean langTypeBean) {
        switch (langTypeBean.getType()) {
            case CN:
                TvSuccessTips.setText(getString(R.string.cn_Congratulations_Your_payment_was_successful_Please_wait_a_minute));
                TvFoodNumTips.setText(getString(R.string.cn_Order_code));
                break;
            case EN:
                TvSuccessTips.setText(getString(R.string.en_Congratulations_Your_payment_was_successful_Please_wait_a_minute));
                TvFoodNumTips.setText(getString(R.string.en_Order_code));
                break;
            case KO:
                TvSuccessTips.setText(getString(R.string.ko_Congratulations_Your_payment_was_successful_Please_wait_a_minute));
                TvFoodNumTips.setText(getString(R.string.ko_Order_code));
                break;
        }
    }
}
