package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mj.cafe.BaseActivity;
import com.mj.cafe.R;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.utils.ActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mj.cafe.bean.LangTypeBean.CN;
import static com.mj.cafe.bean.LangTypeBean.EN;
import static com.mj.cafe.bean.LangTypeBean.KO;

/**
 * 第三方支付
 */
public class PayFailedAcitivty extends BaseActivity {
    @BindView(R.id.IvBack)
    ImageView IvBack;
    @BindView(R.id.IvHanYu)
    ImageView IvHanYu;
    @BindView(R.id.IvZhongWen)
    ImageView IvZhongWen;
    @BindView(R.id.IvYingYu)
    ImageView IvYingYu;
    @BindView(R.id.TvFailedTips)
    TextView TvFailedTips;
    @BindView(R.id.btn)
    TextView btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_failed);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.IvBack, R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu,R.id.btn})
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
            case R.id.btn:
                finish();
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
                TvFailedTips.setText(getString(R.string.cn_Sorry_the_data_request_failed_the_order_has_been_cancelled));
                btn.setText(getString(R.string.cn_Go_back_to_first_page));
                break;
            case EN:
                TvFailedTips.setText(getString(R.string.en_Go_back_to_first_page));
                btn.setText(getString(R.string.en_Sorry_the_data_request_failed_the_order_has_been_cancelled));
                break;
            case KO:
                TvFailedTips.setText(getString(R.string.ko_Sorry_the_data_request_failed_the_order_has_been_cancelled));
                btn.setText(getString(R.string.ko_Go_back_to_first_page));
                break;
        }
    }
}
