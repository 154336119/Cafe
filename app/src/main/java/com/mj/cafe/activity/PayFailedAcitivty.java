package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.BizcContant;
import com.mj.cafe.MyApp;
import com.mj.cafe.R;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.bean.OrderBean;
import com.mj.cafe.bean.PayTypeBean;
import com.mj.cafe.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mj.cafe.bean.LangTypeBean.CN;
import static com.mj.cafe.bean.LangTypeBean.EN;
import static com.mj.cafe.bean.LangTypeBean.KO;
import static com.mj.cafe.utils.PortUtils.JOB_RESPONSE_CANCEL;
import static com.mj.cafe.utils.PortUtils.RESPONSE_SUCCESS;
import static com.mj.cafe.utils.PortUtils.cancel;
import static com.mj.cafe.utils.PortUtils.testCancel;
import static com.mj.cafe.utils.StringToHex.bytesToHexString;

/**
 * 第三方支付
 */
public class PayFailedAcitivty extends BaseActivity {
    @BindView(R.id.IvBack)
    ImageView IvBack;
    @BindView(R.id.IvHanYu)
    ImageView IvHanYu;
    @BindView(R.id.IvZhongWen)
    ImageView IvZhongWen;
    @BindView(R.id.IvYingYu)
    ImageView IvYingYu;
    @BindView(R.id.TvFailedTips)
    TextView TvFailedTips;
    @BindView(R.id.btn)
    TextView btn;
    private OrderBean mOrderBean;
    private PayTypeBean mPayType;
    StringBuffer sb = new StringBuffer();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_failed);
        ButterKnife.bind(this);
        mPayType = getIntent().getParcelableExtra("type");
        mOrderBean = getIntent().getParcelableExtra("order");
        setLangView((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE, new LangTypeBean(LangTypeBean.DEFAULT)));
        //测试
        if (mPayType.getId() == 5) {
            MyApp.getInstance().getSerialPortManager().setOnSerialPortDataListener(new OnSerialPortDataListener() {
                @Override
                public void onDataReceived(byte[] bytes) {
                    checkResponse(bytes);
                }

                @Override
                public void onDataSent(byte[] bytes) {

                }
            });
            MyApp.getInstance().getSerialPortManager().sendBytes(cancel(mOrderBean.getPayMoney(), "0", mOrderBean.getCancelBankInfo()));
////            sb.append("mOrderBean.getCancelBankInfo:"+mOrderBean.getCancelBankInfo());
//            sb.append("mOrderBean.getPayMoney():"+mOrderBean.getPayMoney());
//            sb.append("\n");
//            sb.append("请求：");
//            sb.append("\n");
////            sb.append(testCancel(mOrderBean.getPayMoney(), mOrderBean.getTaxMoney(), mOrderBean.getCancelBankInfo()));
//            sb.append(testCancel(mOrderBean.getPayMoney(), "0", mOrderBean.getCancelBankInfo()));
//            EtTest.setText(sb.toString());
//            MyApp.getInstance().getSerialPortManager().sendBytes(cancel(mOrderBean.getPayMoney(), mOrderBean.getTaxMoney(), mOrderBean.getCancelBankInfo()));
        }
    }

    @OnClick({R.id.IvBack, R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu, R.id.btn})
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
                finish();
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
                TvFailedTips.setText(getString(R.string.cn_Sorry_the_data_request_failed_the_order_has_been_cancelled));
                btn.setText(getString(R.string.cn_Go_back_to_first_page));
                break;
            case EN:
                TvFailedTips.setText(getString(R.string.en_Sorry_the_data_request_failed_the_order_has_been_cancelled));
                btn.setText(getString(R.string.en_Go_back_to_first_page));
                break;
            case KO:
                TvFailedTips.setText(getString(R.string.ko_Sorry_the_data_request_failed_the_order_has_been_cancelled));
                btn.setText(getString(R.string.ko_Go_back_to_first_page));
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
//            showToastMsg("jobCode_" + jobCode);
            response_code = hexStr.substring(64, 66);
//            showToastMsg("response_code" + jobCode);
            if (response_code.equals(RESPONSE_SUCCESS)) {
                if (jobCode.equals(JOB_RESPONSE_CANCEL)) {
//                    showToastMsg("响应_结算取消成功");
                }
                } else {
//                //错误码
//                showToastMsg("Response_error:"+response_code);
                }
            }
        }
    @Override
    protected boolean setOpenTimeDown() {
        return true;
    }
}
