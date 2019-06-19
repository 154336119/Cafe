package com.mj.cafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.mj.cafe.BaseActivity;
import com.mj.cafe.R;
import com.mj.cafe.utils.ActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_way_eat);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.IvBack, R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu, R.id.TvPack, R.id.IvForHere})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.IvBack:
                break;
            case R.id.IvZhongWen:
                break;
            case R.id.IvHanYu:
                break;
            case R.id.IvYingYu:
                break;
            case R.id.TvPack:
                ActivityUtil.next(this,ShopCarActivity.class);
                break;
            case R.id.IvForHere:
                break;
        }
    }
}
