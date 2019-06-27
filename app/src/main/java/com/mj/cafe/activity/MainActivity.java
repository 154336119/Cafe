package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.arch.lifecycle.Observer;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.mj.cafe.BizcContant;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.utils.ActivityUtil;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.R;
import com.mj.cafe.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mj.cafe.bean.LangTypeBean.CN;
import static com.mj.cafe.bean.LangTypeBean.EN;
import static com.mj.cafe.bean.LangTypeBean.KO;

public class MainActivity extends BaseActivity {

    @BindView(R.id.IvZhongWen)
    ImageView IvZhongWen;
    @BindView(R.id.IvHanYu)
    ImageView IvHanYu;
    @BindView(R.id.IvYingYu)
    ImageView IvYingYu;
    @BindView(R.id.IvLogo)
    ImageView IvLogo;
    @BindView(R.id.btn)
    TextView btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setLangView((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE,new LangTypeBean(LangTypeBean.DEFAULT)));
    }

    @OnClick({R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu, R.id.btn})
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
                ActivityUtil.next(this,ChooseWayEatActivity.class);
                break;
        }
    }

    @Override
    public void setLangView(LangTypeBean langTypeBean){
        switch (langTypeBean.getType()) {
            case CN:
                btn.setText(getString(R.string.cn_Order));
                break;
            case EN:
                btn.setText(getString(R.string.en_Order));
                break;
            case KO:
                btn.setText(getString(R.string.ko_Order));
                break;
        }
    }
}
