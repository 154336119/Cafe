package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.BizcContant;
import com.mj.cafe.MyApp;
import com.mj.cafe.R;
import com.mj.cafe.bean.FinishActivityEvent;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.bean.OrderBean;
import com.mj.cafe.bean.PayTypeBean;
import com.mj.cafe.retorfit.HttpMjResult;
import com.mj.cafe.retorfit.RetrofitSerciveFactory;
import com.mj.cafe.retorfit.rxjava.BaseSubscriber;
import com.mj.cafe.retorfit.rxjava.HttpMjEntityFun;
import com.mj.cafe.retorfit.rxjava.RxUtil;
import com.mj.cafe.utils.PortUtils;
import com.mj.cafe.utils.SharedPreferencesUtil;
import com.mj.cafe.utils.StringToHex;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mj.cafe.bean.LangTypeBean.CN;
import static com.mj.cafe.bean.LangTypeBean.EN;
import static com.mj.cafe.bean.LangTypeBean.KO;
import static com.mj.cafe.utils.PortUtils.JOB_RESPONSE_CODE_CARD;
import static com.mj.cafe.utils.PortUtils.JOB_RESPONSE_CODE_CHECK;
import static com.mj.cafe.utils.PortUtils.JOB_RESPONSE_CODE_CONFIRME;
import static com.mj.cafe.utils.PortUtils.JOB_RESPONSE_CODE_WAIT;
import static com.mj.cafe.utils.PortUtils.RESPONSE_SUCCESS;
import static com.mj.cafe.utils.PortUtils.waitConfirm;
import static com.mj.cafe.utils.StringToHex.bytesToHexString;

/**
 * 银行卡支付
 */
public class BankCardPayAcitivty extends BaseActivity {
    @BindView(R.id.EtTest)
    EditText EtTest;
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
    @BindView(R.id.TvPayTypeTips)
    TextView TvPayTypeTips;
    @BindView(R.id.TvPayTips)
    TextView TvPayTips;
    @BindView(R.id.TvCreditCardTips)
    TextView TvCreditCardTips;
    @BindView(R.id.TvSamsungPayTips)
    TextView TvSamsungPayTips;
    @BindView(R.id.TvSamsungPayDetailTips)
    TextView TvSamsungPayDetailTips;
    @BindView(R.id.TvCreditCardDetailTips)
    TextView TvCreditCardDetailTips;
    private PayTypeBean mPayType;
    private OrderBean mOrderBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_pay);
        ButterKnife.bind(this);
        mPayType = getIntent().getParcelableExtra("type");
        mOrderBean = getIntent().getParcelableExtra("order");
        MyApp.getInstance().getSerialPortManager().setOnSerialPortDataListener(new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes) {
                checkResponse(bytes);
            }

            @Override
            public void onDataSent(byte[] bytes) {

            }
        });
        MyApp.getInstance().getSerialPortManager().sendBytes(waitConfirm());
        setLangView((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE, new LangTypeBean(LangTypeBean.DEFAULT)));
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
            case R.id.IvBack:
                finish();
                break;
        }
    }

    public void checkResponse(byte[] data) {
        String jobCode = null;
        String response_code = null;
        String hexStr = bytesToHexString(data);
//        showToastMsg("hexStr_"+hexStr);
        if (hexStr.length() > 3) {
            jobCode = hexStr.substring(62, 64);
            showToastMsg("jobCode_" + jobCode);
            response_code = hexStr.substring(64, 66);
            showToastMsg("response_code" + jobCode);
            if (response_code.equals(RESPONSE_SUCCESS)) {
                if (jobCode.equals(JOB_RESPONSE_CODE_CHECK)) {
                    //设备确认解析
                    String hexState = hexStr.substring(hexStr.length() - 16, hexStr.length() - 4);
                    Log.d("==================", hexState);
                    String state = StringToHex.convertHexToString(hexState);
                    if ("OOOO".equals(state)) {
                        //检查成功
                    } else {
                        //检查失败
                        showToastMsg("state_error_hexStr_" + state);
                        Logger.d(state);
                    }

                } else if (jobCode.equals(JOB_RESPONSE_CODE_CONFIRME)) {
                    //结算确认解析
                    BankCardPayAcitivty.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            EtTest.setText(hexStr);
                        }
                    });
                    showToastMsg("响应_结算确认");
                    String confirmeData = hexStr.substring(hexStr.length() - 318, hexStr.length() - 4);
                    Logger.d("confirmeData:" + confirmeData);
                    if (confirmeData.startsWith("58")) {
                        //错误
                        showToastMsg("结算确认_错误");
                    } else {
                        //支付成功
                        showToastMsg("结算确认_成功");
                        paySuccess(confirmeData);
                    }

                } else if (jobCode.equals(JOB_RESPONSE_CODE_CARD)) {
                    //结算确认解析
                    showToastMsg("响应_插卡");
                    MyApp.getInstance().getSerialPortManager().sendBytes(PortUtils.confirm("2"));
                } else if (jobCode.equals(JOB_RESPONSE_CODE_WAIT)) {
                    //结算确认解析
                    showToastMsg("响应_等待结算");
                    MyApp.getInstance().getSerialPortManager().sendBytes(PortUtils.confirm("2"));
                }

            } else {
//                //错误码
//                showToastMsg("Response_error:"+response_code);
            }
        } else {
            //插卡后的反应
            if (StringToHex.convertHexToString(hexStr).equals("@")) {
                //执行结算确认
                showToastMsg("插卡了");
            }
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

    private void paySuccess(String data) {
        if (mOrderBean == null) {
            return;
        }
        String cardNumber = StringToHex.createBankCardCode(StringToHex.convertHexToString(data.substring(4, 24)));
        String stageMonth = StringToHex.createBankCardCode(StringToHex.convertHexToString(data.substring(96, 100)));
        String approvalNumber = StringToHex.createBankCardCode(StringToHex.convertHexToString(data.substring(100, 124)));
        String cardCompany = StringToHex.createBankCardCode(StringToHex.convertHexToString(data.substring(242, 274)));
        Logger.d("cardNumber:" + cardNumber);
        Logger.d("stageMonth:" + cardNumber);
        Logger.d("approvalNumber:" + stageMonth);
        Logger.d("cardCompany:" + approvalNumber);
        RetrofitSerciveFactory.provideComService().bankcardPaysuccess(mOrderBean.getOrderCode(), cardNumber, stageMonth, approvalNumber, cardCompany)
                .compose(RxUtil.<HttpMjResult<Object>>applySchedulersForRetrofit())
                .map(new HttpMjEntityFun<Object>())
                .subscribe(new BaseSubscriber<Object>(this) {
                    @Override
                    public void onNext(Object entity) {
                        super.onNext(entity);
                    }
                });
    }


    @Override
    public void setLangView(LangTypeBean langTypeBean) {
        switch (langTypeBean.getType()) {
            case CN:
                TvPayTypeTips.setText(getString(R.string.cn_Credit_Card_Samsung_Pay));
                TvPayTips.setText(getString(R.string.cn_You_can_pay_with_a_credit_card_or_Samsung_Pay));
                TvCreditCardTips.setText(getString(R.string.cn_Credit_Card));
                TvSamsungPayTips.setText(getString(R.string.cn_Samsung_Pay));
                TvSamsungPayDetailTips.setText(getString(R.string.cn_Samsung_Pay_tips));
                TvCreditCardDetailTips.setText(getString(R.string.cn_Card_tips));
                break;
            case EN:
                TvPayTypeTips.setText(getString(R.string.en_Credit_Card_Samsung_Pay));
                TvPayTips.setText(getString(R.string.en_You_can_pay_with_a_credit_card_or_Samsung_Pay));
                TvCreditCardTips.setText(getString(R.string.en_Credit_Card));
                TvSamsungPayTips.setText(getString(R.string.en_Samsung_Pay));
                TvSamsungPayDetailTips.setText(getString(R.string.en_Samsung_Pay_tips));
                TvCreditCardDetailTips.setText(getString(R.string.en_Card_tips));
                break;
            case KO:
                TvPayTypeTips.setText(getString(R.string.ko_Credit_Card_Samsung_Pay));
                TvPayTips.setText(getString(R.string.ko_You_can_pay_with_a_credit_card_or_Samsung_Pay));
                TvCreditCardTips.setText(getString(R.string.ko_Credit_Card));
                TvSamsungPayTips.setText(getString(R.string.ko_Samsung_Pay));
                TvSamsungPayDetailTips.setText(getString(R.string.ko_Samsung_Pay_tips));
                TvCreditCardDetailTips.setText(getString(R.string.ko_Card_tips));
                break;
        }
    }
}
