package com.mj.cafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.BizcContant;
import com.mj.cafe.R;
import com.mj.cafe.bean.CouponOutBean;
import com.mj.cafe.bean.FinishActivityEvent;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.bean.OrderBean;
import com.mj.cafe.bean.PayTypeBean;
import com.mj.cafe.retorfit.HttpLoggingInterceptor;
import com.mj.cafe.retorfit.HttpMjResult;
import com.mj.cafe.retorfit.RetrofitSerciveFactory;
import com.mj.cafe.retorfit.rxjava.BaseSubscriber;
import com.mj.cafe.retorfit.rxjava.HttpMjEntityFun;
import com.mj.cafe.retorfit.rxjava.RxUtil;
import com.mj.cafe.utils.ActivityUtil;
import com.mj.cafe.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

import static com.mj.cafe.bean.LangTypeBean.CN;
import static com.mj.cafe.bean.LangTypeBean.EN;
import static com.mj.cafe.bean.LangTypeBean.KO;

/**
 * 银行卡支付
 */
public class ThreePayAcitivty extends BaseActivity {
    @BindView(R.id.IvBack)
    ImageView IvBack;
    @BindView(R.id.IvZhongWen)
    ImageView IvZhongWen;
    @BindView(R.id.IvHanYu)
    ImageView IvHanYu;
    @BindView(R.id.IvYingYu)
    ImageView IvYingYu;
    @BindView(R.id.TvAccountSmallTips)
    TextView TvAccountSmallTips;
    @BindView(R.id.TvTotalAccountTips)
    TextView TvTotalAccountTips;
    @BindView(R.id.TvTotalAccount)
    TextView TvTotalAccount;
    @BindView(R.id.TvScoreTips)
    TextView TvScoreTips;
    @BindView(R.id.TvScore)
    TextView TvScore;
    @BindView(R.id.RlScore)
    RelativeLayout RlScore;
    @BindView(R.id.TvCouponTips)
    TextView TvCouponTips;
    @BindView(R.id.TvCoupun)
    TextView TvCoupun;
    @BindView(R.id.RlCoupon)
    RelativeLayout RlCoupon;
    @BindView(R.id.TvRealAccountTips)
    TextView TvRealAccountTips;
    @BindView(R.id.TvRealAccount)
    TextView TvRealAccount;
    @BindView(R.id.TvPayTypeTips)
    TextView TvPayTypeTips;
    @BindView(R.id.TvShadow)
    TextView TvShadow;
    @BindView(R.id.RlShadow)
    RelativeLayout RlShadow;
    @BindView(R.id.IvQrcode)
    ImageView IvQrcode;
    private PayTypeBean mPayType;
    private OrderBean mOrderBean;
    private int httpNum = 0;
    Subscription disposable;
    private Handler handler  = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            httpNum++;
            httpGetPayStatus();
            return true;
        }
    });;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_pay);
        ButterKnife.bind(this);
        mPayType = getIntent().getParcelableExtra("type");
        mOrderBean = getIntent().getParcelableExtra("order");
        TvTotalAccount.setText(mOrderBean.getStringGoodsMoneyMoney());
        TvScore.setText(mOrderBean.getStringIntegralMoney());
        TvCoupun.setText(mOrderBean.getStringCouponMoneyMoney());
        TvRealAccount.setText(mOrderBean.getStringPay_moneyMoney());
        Glide.with(this)
                .load(mOrderBean.getQrcode())
                .into(IvQrcode);
        //4秒后发送
        handler.sendEmptyMessageDelayed(0,4000);
        setLangView((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE, new LangTypeBean(LangTypeBean.DEFAULT)));
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
            case R.id.btn:
                ActivityUtil.next(this, ChooseWayEatActivity.class);
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
                TvAccountSmallTips.setText(R.string.cn_Total_price_small);
                TvTotalAccountTips.setText(R.string.cn_Total_price);
                TvScoreTips.setText(R.string.cn_Point_discount);
                TvCouponTips.setText(R.string.cn_coupons_discount);
                TvShadow.setText(R.string.cn_QR_code_has_been_expired_click_refresh);
                TvRealAccountTips.setText(R.string.cn_Payment_amount);
                if (mPayType.getPay_name().equals("Kakao Pay")) {
                    TvPayTypeTips.setText(R.string.cn_Please_scan_your_barcode_of_Kakao_talk);
                } else if (mPayType.getPay_name().equals("PAYCO")) {
                    TvPayTypeTips.setText(R.string.cn_Please_scan_your_barcode_of_Payco);
                } else if (mPayType.getPay_name().equals("AliPAY")) {
                    TvPayTypeTips.setText(R.string.cn_Please_use_Alipay_to_pay);
                } else if (mPayType.getPay_name().equals("Wechat Pay")) {
                    TvPayTypeTips.setText(R.string.cn_Please_use_Wechat_Pay_to_pay);
                }
                break;
            case EN:
                TvAccountSmallTips.setText(R.string.en_Total_price_small);
                TvTotalAccountTips.setText(R.string.en_Total_price);
                TvScoreTips.setText(R.string.en_Point_discount);
                TvCouponTips.setText(R.string.en_coupons_discount);
                TvShadow.setText(R.string.en_QR_code_has_been_expired_click_refresh);
                TvRealAccountTips.setText(R.string.en_Payment_amount);
                if (mPayType.getPay_name().equals("Kakao Pay")) {
                    TvPayTypeTips.setText(R.string.en_Please_scan_your_barcode_of_Kakao_talk);
                } else if (mPayType.getPay_name().equals("PAYCO")) {
                    TvPayTypeTips.setText(R.string.en_Please_scan_your_barcode_of_Payco);
                } else if (mPayType.getPay_name().equals("AliPAY")) {
                    TvPayTypeTips.setText(R.string.en_Please_use_Alipay_to_pay);
                } else if (mPayType.getPay_name().equals("Wechat Pay")) {
                    TvPayTypeTips.setText(R.string.en_Please_use_Wechat_Pay_to_pay);
                }
                break;
            case KO:
                TvAccountSmallTips.setText(R.string.ko_Total_price_small);
                TvTotalAccountTips.setText(R.string.ko_Total_price);
                TvScoreTips.setText(R.string.ko_Point_discount);
                TvCouponTips.setText(R.string.ko_coupons_discount);
                TvShadow.setText(R.string.ko_QR_code_has_been_expired_click_refresh);
                TvRealAccountTips.setText(R.string.ko_Payment_amount);
                if (mPayType.getPay_name().equals("Kakao Pay")) {
                    TvPayTypeTips.setText(R.string.ko_Please_scan_your_barcode_of_Kakao_talk);
                } else if (mPayType.getPay_name().equals("PAYCO")) {
                    TvPayTypeTips.setText(R.string.ko_Please_scan_your_barcode_of_Payco);
                } else if (mPayType.getPay_name().equals("AliPAY")) {
                    TvPayTypeTips.setText(R.string.ko_Please_use_Alipay_to_pay);

                } else if (mPayType.getPay_name().equals("Wechat Pay")) {
                    TvPayTypeTips.setText(R.string.ko_Please_use_Wechat_Pay_to_pay);
                }
                break;
        }
    }

    //http - 支付状态查询
    private void httpGetPayStatus() {
        disposable= RetrofitSerciveFactory.provideComService().getPayStatus(mOrderBean.getOrderCode())
                .compose(RxUtil.<HttpMjResult<Object>>applySchedulersForRetrofit())
                .map(new HttpMjEntityFun<Object>())
                .subscribe(new BaseSubscriber<Object>(this) {
                    @Override
                    public void onNext(Object entity) {
                        super.onNext(entity);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("type", mPayType);
                        if(mOrderBean == null ){
                            showToastMsg("mOrderBean为空");
                            return;
                        }
                        bundle.putParcelable("order", mOrderBean);
                        ActivityUtil.next(ThreePayAcitivty.this,CountDownActivity.class,bundle,true);
                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMsg("支付失败"+httpNum);
                        handler.sendEmptyMessageDelayed(0,4000);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable!=null){
            disposable.unsubscribe();
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

}
