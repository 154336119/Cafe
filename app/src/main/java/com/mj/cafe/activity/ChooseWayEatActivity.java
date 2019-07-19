package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mj.cafe.BaseActivity;
import com.mj.cafe.BizcContant;
import com.mj.cafe.R;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.utils.ActivityUtil;
import com.mj.cafe.utils.AntiShakeUtils;
import com.mj.cafe.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mj.cafe.bean.LangTypeBean.CN;
import static com.mj.cafe.bean.LangTypeBean.EN;
import static com.mj.cafe.bean.LangTypeBean.KO;

public class ChooseWayEatActivity extends BaseActivity {
    @BindView(R.id.IvBack)
    ImageView IvBack;
    @BindView(R.id.IvZhongWen)
    ImageView IvZhongWen;
    @BindView(R.id.IvHanYu)
    ImageView IvHanYu;
    @BindView(R.id.IvYingYu)
    ImageView IvYingYu;
    @BindView(R.id.IvLogo)
    ImageView IvLogo;
    @BindView(R.id.TvPack)
    ImageView TvPack;
    @BindView(R.id.IvForHere)
    ImageView IvForHere;
    @BindView(R.id.TvChooseTips)
    TextView TvChooseTips;
    @BindView(R.id.TvPackTips)
    TextView TvPackTips;
    @BindView(R.id.TvForHereTips)
    TextView TvForHereTips;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_way_eat);
        ButterKnife.bind(this);
        setLangView((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE, new LangTypeBean(LangTypeBean.DEFAULT)));
    }

    @OnClick({R.id.IvBack, R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu, R.id.TvPack, R.id.IvForHere})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.IvBack:
                finish();
                break;
            case R.id.IvZhongWen:
                postLangLiveData(new LangTypeBean(CN));
                break;
            case R.id.IvHanYu:
                postLangLiveData(new LangTypeBean(KO));
                break;
            case R.id.IvYingYu:
                postLangLiveData(new LangTypeBean(EN));
                break;
            case R.id.TvPack:
                if (AntiShakeUtils.isInvalidClick(view)) return;
                ActivityUtil.next(this, ShopCarActivity.class);
                break;
            case R.id.IvForHere:
                if (AntiShakeUtils.isInvalidClick(view)) return;
                ActivityUtil.next(this, SeatListActivity.class);
                break;
        }
    }
    @Override
    public void setLangView(LangTypeBean langTypeBean) {
        switch (langTypeBean.getType()) {
            case CN:
                TvChooseTips.setText(getString(R.string.cn_Please_select_way_of_order));
                TvPackTips .setText(getString(R.string.cn_To_go));
                TvForHereTips.setText(getString(R.string.cn_For_Here));
                break;
            case EN:
                TvChooseTips.setText(getString(R.string.en_Please_select_way_of_order));
                TvPackTips .setText(getString(R.string.en_To_go));
                TvForHereTips.setText(getString(R.string.en_For_Here));
                break;
            case KO:
                TvChooseTips.setText(getString(R.string.ko_Please_select_way_of_order));
                TvPackTips .setText(getString(R.string.ko_To_go));
                TvForHereTips.setText(getString(R.string.ko_For_Here));
                break;
        }
    }
}
