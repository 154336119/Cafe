package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.R;
import com.mj.cafe.adapter.PayTypeAdapter;
import com.mj.cafe.adapter.SeatAdapter;
import com.mj.cafe.bean.CouponBean;
import com.mj.cafe.bean.CouponOutBean;
import com.mj.cafe.bean.OrderBean;
import com.mj.cafe.bean.PayTypeBean;
import com.mj.cafe.bean.SeatBean;
import com.mj.cafe.bean.UserBean;
import com.mj.cafe.retorfit.HttpMjResult;
import com.mj.cafe.retorfit.RetrofitSerciveFactory;
import com.mj.cafe.retorfit.rxjava.BaseSubscriber;
import com.mj.cafe.retorfit.rxjava.HttpMjEntityFun;
import com.mj.cafe.retorfit.rxjava.RxUtil;
import com.mj.cafe.utils.ActivityUtil;
import com.mj.cafe.view.spinner.NiceSpinner;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoosePayTypeActitiy extends BaseActivity {

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
    @BindView(R.id.TvAccountTips)
    TextView TvAccountTips;
    @BindView(R.id.TvAccount)
    TextView TvAccount;
    @BindView(R.id.TvUsedCouponTips)
    TextView TvUsedCouponTips;
    @BindView(R.id.BtnRegister)
    TextView BtnRegister;
    @BindView(R.id.BtnLogin)
    TextView BtnLogin;
    @BindView(R.id.RlLogin)
    RelativeLayout RlLogin;
    @BindView(R.id.TvDiscountTips)
    TextView TvDiscountTips;
    @BindView(R.id.CheckBox)
    android.widget.CheckBox CheckBox;
    @BindView(R.id.EtJiFen)
    EditText EtJiFen;
    @BindView(R.id.RlUserJiFen)
    RelativeLayout RlUserJiFen;
    @BindView(R.id.nice_spinner)
    NiceSpinner niceSpinner;
    @BindView(R.id.RlUsedCoupon)
    RelativeLayout RlUsedCoupon;
    @BindView(R.id.RlCoupon)
    LinearLayout RlCoupon;
    @BindView(R.id.TvChoosePayTypeTips)
    TextView TvChoosePayTypeTips;
    @BindView(R.id.TvYinHangKaPayTips)
    TextView TvYinHangKaPayTips;
    @BindView(R.id.RvPayType)
    RecyclerView RvPayType;
    PayTypeAdapter mPayTypeAdapter;
    private List<PayTypeBean> mPayTypeList = new ArrayList<>();
    //优惠券
    private List<CouponBean> CouponList = new ArrayList<>();
    private Map<String,Integer> CouponMap = new HashMap<>();
    private List<String> mSpinnerList = new ArrayList<>();
    //订单总金额 元
    private String mShowTotialAccount;
    //用户
    private UserBean mUserBean;
    //座位
    private String mSeatArray = null;
    //1堂食, 2打包
    private Integer mEnjoyway = 2;
    //点数
    private Integer mIntegral;
    //优惠券
    private Integer mCouponId;
    //支付类型
    private Integer mPayType;
    //点餐数据
    private String mGoodsArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pay_type);
        mSeatArray = getIntent().getStringExtra("SeatArray");
        mEnjoyway = getIntent().getIntExtra("Enjoyway",2);
        mGoodsArray = getIntent().getStringExtra("GoodsArray");
        mShowTotialAccount =  getIntent().getStringExtra("ShowTotialAccount");
        ButterKnife.bind(this);
        getPayTypeList();
    }

    @Override
    protected boolean rxBusRegist() {
        return true;
    }

    @OnClick({R.id.IvBack, R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu, R.id.BtnRegister, R.id.BtnLogin})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.IvBack:
                break;
            case R.id.IvZhongWen:
                break;
            case R.id.IvHanYu:
                break;
            case R.id.IvYingYu:
                break;
            case R.id.BtnRegister:
                bundle.putInt("type", LoginRegisterActvitiy.REIGSTER);
                ActivityUtil.next(this, LoginRegisterActvitiy.class, bundle, false);
                break;
            case R.id.BtnLogin:
                bundle.putInt("type", LoginRegisterActvitiy.LOGIN);
                ActivityUtil.next(this, LoginRegisterActvitiy.class, bundle, false);
                break;
        }
    }

    @Subscribe
    public void setUser(UserBean user) {
        mUserBean = user;
        httpGetCouponList();
        RlCoupon.setVisibility(View.VISIBLE);
        RlLogin.setVisibility(View.GONE);
    }

    //http-支付方式
    private void getPayTypeList() {
        RetrofitSerciveFactory.provideComService().getPaytypeList(null)
                .compose(RxUtil.<HttpMjResult<List<PayTypeBean>>>applySchedulersForRetrofit())
                .map(new HttpMjEntityFun<List<PayTypeBean>>())
                .subscribe(new BaseSubscriber<List<PayTypeBean>>(this) {
                    @Override
                    public void onNext(List<PayTypeBean> list) {
                        super.onNext(list);
                        mPayTypeList = list;
                        RvPayType.setLayoutManager(new GridLayoutManager(ChoosePayTypeActitiy.this, 2));
                        mPayTypeAdapter = new PayTypeAdapter(mPayTypeList);
                        RvPayType.setAdapter(mPayTypeAdapter);
                        mPayTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                httpOrderCreate();
                            }
                        });
                    }
                });
    }

    //http - 订单创建
    public void httpOrderCreate(){
        if(mUserBean == null){
            return;
        }
        RetrofitSerciveFactory.provideComService().orderCreate("cn",mUserBean.getToken(),1,mSeatArray,mEnjoyway,mIntegral,mCouponId,mPayType,mGoodsArray)
                .compose(RxUtil.<HttpMjResult<OrderBean>>applySchedulersForRetrofit())
                .map(new HttpMjEntityFun<OrderBean>())
                .subscribe(new BaseSubscriber<OrderBean>(this) {
                    @Override
                    public void onNext(OrderBean entity) {
                        super.onNext(entity);
                        RxBus.get().post(entity);
                        finish();
                    }
                });
    }

    //http - 优惠券列表
    private void httpGetCouponList(){
        RetrofitSerciveFactory.provideComService().getCouponList("cn",mUserBean.getToken())
                .compose(RxUtil.<HttpMjResult<CouponOutBean>>applySchedulersForRetrofit())
                .map(new HttpMjEntityFun<CouponOutBean>())
                .subscribe(new BaseSubscriber<CouponOutBean>(this) {
                    @Override
                    public void onNext(CouponOutBean entity) {
                        super.onNext(entity);
                        if(entity!=null && entity.getCoupons().size()>0){
                            CouponList = entity.getCoupons();
                            initSpinner();
                        }
                    }
                });
    }

    //初始化优惠券列表
    private void initSpinner(){
        for(CouponBean couponBean : CouponList){
            CouponMap.put(couponBean.getName(),couponBean.getId());
            mSpinnerList.add(couponBean.getName());
        }
        niceSpinner.attachDataSource(mSpinnerList);
//        niceSpinner.setBackgroundResource(R.drawable.textview_round_border);
        niceSpinner.setTextColor(getResources().getColor(R.color.color_green));
        niceSpinner.setTextSize(25);
    }
}
