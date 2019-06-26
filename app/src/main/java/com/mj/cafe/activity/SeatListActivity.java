package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.BizcContant;
import com.mj.cafe.R;
import com.mj.cafe.adapter.SeatAdapter;
import com.mj.cafe.bean.SeatBean;
import com.mj.cafe.bean.TypeBean;
import com.mj.cafe.retorfit.HttpMjResult;
import com.mj.cafe.retorfit.RetrofitSerciveFactory;
import com.mj.cafe.retorfit.rxjava.BaseSubscriber;
import com.mj.cafe.retorfit.rxjava.HttpMjEntityFun;
import com.mj.cafe.retorfit.rxjava.RxUtil;
import com.mj.cafe.utils.ActivityUtil;
import com.mj.cafe.utils.SharedPreferencesUtil;
import com.mj.cafe.utils.SortUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SeatListActivity extends BaseActivity implements SeatAdapter.OnSelectBtnEnable{
    @BindView(R.id.IvBack)
    ImageView IvBack;
    @BindView(R.id.IvZhongWen)
    ImageView IvZhongWen;
    @BindView(R.id.IvHanYu)
    ImageView IvHanYu;
    @BindView(R.id.IvYingYu)
    ImageView IvYingYu;
    @BindView(R.id.LlTopBar)
    LinearLayout LlTopBar;
    @BindView(R.id.TvTips)
    TextView TvTips;
    @BindView(R.id.BtnChoseSet)
    TextView BtnChoseSet;
    @BindView(R.id.tvKexuanTips)
    TextView tvKexuanTips;
    @BindView(R.id.tvBukexuanTips)
    TextView tvBukexuanTips;
    @BindView(R.id.tvYixuanTips)
    TextView tvYixuanTips;
    @BindView(R.id.RcHeard)
    RelativeLayout RcHeard;
    @BindView(R.id.RvSeat)
    RecyclerView RvSeat;
    SeatAdapter mSeatAdapter;
    List<SeatBean> mSeatList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_list);
        ButterKnife.bind(this);
        getSetList();
    }

    @OnClick({R.id.IvBack, R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu, R.id.BtnChoseSet})
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
            case R.id.BtnChoseSet:
                Bundle bundle = new Bundle();
                SeatBean seatBean = mSeatAdapter.getData().get(mSeatAdapter.getCheckedPosition());
                String seatId = seatBean.getId()+"";

                bundle.putString("SeatArray",seatId);
                bundle.putInt("Enjoyway",1);
                ActivityUtil.next(this,ShopCarActivity.class,bundle,false);
                break;
        }
    }

    //http-请求座位列表
    private void getSetList(){
        RetrofitSerciveFactory.provideComService().getSeatList(1)
                .compose(RxUtil.<HttpMjResult<List<SeatBean>>>applySchedulersForRetrofit())
                .map(new HttpMjEntityFun<List<SeatBean>>())
                .subscribe(new BaseSubscriber<List<SeatBean>>(this) {
                    @Override
                    public void onNext(List<SeatBean> entity) {
                        super.onNext(entity);
                        SortSeat(entity);
                    }
                });
    }

    private void SortSeat(List<SeatBean> seatBeanList) {
        int maxCol = SortUtils.getX_MaxNum(seatBeanList);
        int maxRow = SortUtils.getY_MaxNum(seatBeanList);
        mSeatList = SortUtils.getTotalList(seatBeanList,maxCol,maxRow);
        initRv(mSeatList,maxCol);
    }

    private void initRv(List<SeatBean> seatBeanList,int maxRow){
        mSeatList = seatBeanList;
        RvSeat.setLayoutManager(new GridLayoutManager(this, maxRow));
        mSeatAdapter = new SeatAdapter(mSeatList,this,this);
        RvSeat.setAdapter(mSeatAdapter);
    }

    @Override
    public void isEnable(boolean enable) {
        BtnChoseSet.setEnabled(enable);
    }
}
