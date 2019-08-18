package com.mj.cafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mj.cafe.BaseActivity;
import com.mj.cafe.BizcContant;
import com.mj.cafe.MyApp;
import com.mj.cafe.R;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.bean.OrderBean;
import com.mj.cafe.bean.PrintEntity;
import com.mj.cafe.retorfit.HttpMjResult;
import com.mj.cafe.retorfit.RetrofitSerciveFactory;
import com.mj.cafe.retorfit.rxjava.BaseSubscriber;
import com.mj.cafe.retorfit.rxjava.HttpMjEntityFun;
import com.mj.cafe.retorfit.rxjava.RxUtil;
import com.mj.cafe.utils.SharedPreferencesUtil;
import com.mj.cafe.utils.print.TaskEnPrint;
import com.mj.cafe.utils.print.TaskKoPrint;
import com.mj.cafe.utils.print.TaskPrint;

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
    String mMealCode; //取餐码
    String mOrderCode;//订单号
    private PrintEntity mPrintEntity;
    private Handler handler  = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            finish();
            return true;
        }
    });;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderCode = getIntent().getStringExtra("order_code");
        mMealCode = getIntent().getStringExtra("meal_code");
        setContentView(R.layout.activity_pay_success);
        ButterKnife.bind(this);
        TvFoodNum.setText(mMealCode);
        httpGetPrintData();
//        showToastMsg("mOrderCode:"+mOrderCode);
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

    //http - 小票打印数据
    private void httpGetPrintData() {
        LangTypeBean typeBean = (LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE, new LangTypeBean(CN));
        RetrofitSerciveFactory.provideComService().getPrintData(mOrderCode,typeBean.getUserHttpType())
                .compose(RxUtil.<HttpMjResult<PrintEntity>>applySchedulersForRetrofit())
                .map(new HttpMjEntityFun<PrintEntity>())
                .subscribe(new BaseSubscriber<PrintEntity>(this) {
                    @Override
                    public void onNext(PrintEntity entity) {
                        super.onNext(entity);
                        mPrintEntity = entity;
                        printTiket((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE, new LangTypeBean(LangTypeBean.DEFAULT)),mPrintEntity);
                        handler.sendEmptyMessageDelayed(0,3000);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    //打印小票
    private void printTiket(LangTypeBean langTypeBean,PrintEntity printEntity){
        if(printEntity == null){
            showToastMsg("error_printEntity_null");
            return;
        }
        switch (langTypeBean.getType()) {
            case CN:
                new Thread(new TaskPrint(MyApp.getInstance().getPos(), printEntity)).start();
                break;
            case EN:
                new Thread(new TaskEnPrint(MyApp.getInstance().getPos(), printEntity)).start();
                break;
            case KO:
                new Thread(new TaskKoPrint(MyApp.getInstance().getPos(), printEntity)).start();
                break;
        }
    }
}
