package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.mj.cafe.BizcContant;
import com.mj.cafe.R;
import com.mj.cafe.adapter.PayTypeAdapter;
import com.mj.cafe.bean.CouponBean;
import com.mj.cafe.bean.CouponOutBean;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.bean.OrderBean;
import com.mj.cafe.bean.PayTypeBean;
import com.mj.cafe.bean.UserBean;
import com.mj.cafe.retorfit.HttpMjResult;
import com.mj.cafe.retorfit.RetrofitSerciveFactory;
import com.mj.cafe.retorfit.rxjava.BaseSubscriber;
import com.mj.cafe.retorfit.rxjava.HttpMjEntityFun;
import com.mj.cafe.retorfit.rxjava.RxUtil;
import com.mj.cafe.utils.ActivityUtil;
import com.mj.cafe.utils.EtInputFilters;
import com.mj.cafe.utils.SharedPreferencesUtil;
import com.mj.cafe.view.spinner.NiceSpinner;
import com.mj.cafe.view.spinner.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mj.cafe.bean.LangTypeBean.CN;
import static com.mj.cafe.bean.LangTypeBean.EN;
import static com.mj.cafe.bean.LangTypeBean.KO;

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
    @BindView(R.id.TvNoCouponTips)
    TextView TvNoCouponTips;
    @BindView(R.id.TvJiFenTips)
    TextView TvJiFenTips;
    private List<PayTypeBean> mPayTypeList = new ArrayList<>();
    //优惠券
    private List<CouponBean> CouponList = new ArrayList<>();
    private Map<String, Integer> CouponMap = new HashMap<>();
    private List<String> mSpinnerList = new ArrayList<>();
    //订单总金额 元
    private String mShowTotialAccount;
    //用户
    private UserBean mUserBean;
    //座位
    private String mSeatArray = "0";
    //1堂食, 2打包
    private Integer mEnjoyway = 2;
    //点数
    private Integer mIntegral=0;
    //优惠券
    private Integer mCouponId=0;
    //支付类型
    private Integer mPayType;
    //点餐数据
    private String mGoodsArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pay_type);
        if(!TextUtils.isEmpty(getIntent().getStringExtra("SeatArray"))){
            mSeatArray = getIntent().getStringExtra("SeatArray");
        }
        mEnjoyway = getIntent().getIntExtra("Enjoyway", 2);
        mGoodsArray = getIntent().getStringExtra("GoodsArray");
        mShowTotialAccount = getIntent().getStringExtra("ShowTotialAccount");
        ButterKnife.bind(this);
        TvAccount.setText(mShowTotialAccount);
        getPayTypeList();
        setLangView((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE,new LangTypeBean(LangTypeBean.DEFAULT)));

    }

    @Override
    protected boolean rxBusRegist() {
        return true;
    }

    @OnClick({R.id.IvBack, R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu, R.id.BtnRegister, R.id.BtnLogin})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
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
        initIntegral();
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
                                mPayType = mPayTypeList.get(position).getId();
                                httpOrderCreate();
                            }
                        });
                    }
                });
    }

    //http - 订单创建
    public void httpOrderCreate() {

        if (mUserBean == null) {
            return;
        }
        if(CheckBox.isChecked()){
            mIntegral = Integer.getInteger(EtJiFen.getText().toString());
        }
        LangTypeBean typeBean = (LangTypeBean)SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE,new LangTypeBean(LangTypeBean.CN));
        RetrofitSerciveFactory.provideComService().orderCreate(typeBean.getUserHttpType(), mUserBean.getToken(), 1, mSeatArray, mEnjoyway, mIntegral, mCouponId, mPayType, mGoodsArray)
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
    private void httpGetCouponList() {
        LangTypeBean typeBean = (LangTypeBean)SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE,new LangTypeBean(LangTypeBean.CN));
        RetrofitSerciveFactory.provideComService().getCouponList(typeBean.getUserHttpType(), mUserBean.getToken())
                .compose(RxUtil.<HttpMjResult<CouponOutBean>>applySchedulersForRetrofit())
                .map(new HttpMjEntityFun<CouponOutBean>())
                .subscribe(new BaseSubscriber<CouponOutBean>(this) {
                    @Override
                    public void onNext(CouponOutBean entity) {
                        super.onNext(entity);
                        initSpinner(entity);
                    }
                });
    }

    //初始化优惠券列表
    private void initSpinner(CouponOutBean entity) {
        niceSpinner.setTextColor(getResources().getColor(R.color.color_green));
        niceSpinner.setTextSize(25);
        if (entity != null && entity.getCoupons().size() > 0) {
            //有优惠券的情况
            niceSpinner.setSelected(true);
            CouponList = entity.getCoupons();
            for (CouponBean couponBean : CouponList) {
                CouponMap.put(couponBean.getName(), couponBean.getId());
                mSpinnerList.add(couponBean.getName());
            }
            niceSpinner.attachDataSource(mSpinnerList);
            niceSpinner.setText(getString(R.string.cam_used_coupon_num,CouponList.size()+""));
            niceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                @Override
                public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                    mCouponId = CouponMap.get(mSpinnerList.get(position));
                }
            });
        } else {
            //没有优惠券的情况
            niceSpinner.setSelected(false);
            niceSpinner.setText(getString(R.string.np_cam_used_coupon));
            niceSpinner.hideArrow();
        }

}

    //初始化点数优惠的情况
    private void initIntegral() {
        if (mUserBean.getIntegral() == null || mUserBean.getIntegral() == 0) {
            //没点数的情况
            CheckBox.setEnabled(false);
            CheckBox.setChecked(false);
            EtJiFen.setEnabled(false);
//            EtJiFen.setText("0");
            TvJiFenTips.setText(getString(R.string.cam_used_integral,"0"));
        }else{
            CheckBox.setEnabled(true);
            EtJiFen.setEnabled(true);
            TvJiFenTips.setText(getString(R.string.cam_used_integral,mUserBean.getIntegral()+""));
            EtInputFilters filter = new EtInputFilters(EtInputFilters.TYPE_MAXNUMBER);
            filter.setMaxNum(1,mUserBean.getIntegral(),0);
            EtJiFen.setFilters(new InputFilter[]{filter});

        }
    }

    @Override
    public void setLangView(LangTypeBean langTypeBean) {
        switch (langTypeBean.getType()) {
            case CN:
                TvAccountTips.setText(getString(R.string.cn_Total_price));
                TvUsedCouponTips.setText(getString(R.string.cn_Do_you_want_to_use_membership_discounts));
                BtnRegister.setText(R.string.cn_Create_account);
                BtnLogin.setText(R.string.cn_Sign_up);
                TvChoosePayTypeTips.setText(R.string.cn_Please_choose_the_way_of_payment);
                TvYinHangKaPayTips.setText(R.string.cn_Credit_or_debit_card);
                break;
            case EN:
                TvAccountTips.setText(getString(R.string.en_Total_price));
                TvUsedCouponTips.setText(getString(R.string.en_Do_you_want_to_use_membership_discounts));
                BtnRegister.setText(R.string.en_Create_account);
                BtnLogin.setText(R.string.en_Sign_up);
                TvChoosePayTypeTips.setText(R.string.en_Please_choose_the_way_of_payment);
                TvYinHangKaPayTips.setText(R.string.en_Credit_or_debit_card);
                break;
            case KO:
                TvAccountTips.setText(getString(R.string.ko_Total_price));
                TvUsedCouponTips.setText(getString(R.string.ko_Do_you_want_to_use_membership_discounts));
                BtnRegister.setText(R.string.ko_Create_account);
                BtnLogin.setText(R.string.ko_Sign_up);
                TvChoosePayTypeTips.setText(R.string.ko_Please_choose_the_way_of_payment);
                TvYinHangKaPayTips.setText(R.string.ko_Credit_or_debit_card);
                break;
        }
    }
}
