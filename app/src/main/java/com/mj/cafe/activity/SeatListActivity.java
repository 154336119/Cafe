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
import com.hwangjr.rxbus.annotation.Subscribe;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.BizcContant;
import com.mj.cafe.R;
import com.mj.cafe.adapter.SeatAdapter;
import com.mj.cafe.bean.FinishActivityEvent;
import com.mj.cafe.bean.LangTypeBean;
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

import static com.mj.cafe.bean.LangTypeBean.CN;
import static com.mj.cafe.bean.LangTypeBean.EN;
import static com.mj.cafe.bean.LangTypeBean.KO;

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
    LinearLayout RcHeard;
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
        setLangView((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE, new LangTypeBean(LangTypeBean.DEFAULT)));
    }

    @OnClick({R.id.IvBack, R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu, R.id.BtnChoseSet})
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

    @Override
    public void setLangView(LangTypeBean langTypeBean) {
        switch (langTypeBean.getType()) {
            case CN:
                TvTips.setText(getString(R.string.cn_Please_choose_seat));
                tvKexuanTips.setText(getString(R.string.cn_Can_select));
                tvBukexuanTips .setText(getString(R.string.cn_Cannot_select));
                tvYixuanTips.setText(getString(R.string.cn_Selected));
                BtnChoseSet.setText(getString(R.string.cn_Select_a_seat_and_order));
                break;
            case EN:
                TvTips.setText(getString(R.string.en_Please_choose_seat));
                tvKexuanTips.setText(getString(R.string.en_Can_select));
                tvBukexuanTips .setText(getString(R.string.en_Cannot_select));
                tvYixuanTips.setText(getString(R.string.en_Selected));
                BtnChoseSet.setText(getString(R.string.en_Select_a_seat_and_order));
                break;
            case KO:
                TvTips.setText(getString(R.string.ko_Please_choose_seat));
                tvKexuanTips.setText(getString(R.string.ko_Can_select));
                tvBukexuanTips .setText(getString(R.string.ko_Cannot_select));
                tvYixuanTips.setText(getString(R.string.ko_Selected));
                BtnChoseSet.setText(getString(R.string.ko_Select_a_seat_and_order));
                break;
        }
    }
    @Override
    protected boolean rxBusRegist() {
        return true;
    }
    @Subscribe
    public void onFinishEvent(FinishActivityEvent event) {
        finish();
    }

    @Override
    protected boolean setOpenTimeDown() {
        return true;
    }
}
