package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.BizcContant;
import com.mj.cafe.MyApp;
import com.mj.cafe.R;
import com.mj.cafe.bean.FinishActivityEvent;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.utils.SharedPreferencesUtil;
import com.mj.cafe.utils.StringToHex;
import com.orhanobut.logger.Logger;

import static com.mj.cafe.utils.PortUtils.JOB_RESPONSE_CODE_CHECK;
import static com.mj.cafe.utils.PortUtils.JOB_RESPONSE_CODE_CONFIRME;
import static com.mj.cafe.utils.PortUtils.RESPONSE_SUCCESS;
import static com.mj.cafe.utils.PortUtils.check;
import static com.mj.cafe.utils.StringToHex.bytesToHexString;

/**
 * 第三方支付
 */
public class BankCardPayAcitivty extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_pay);
        setLangView((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE,new LangTypeBean(LangTypeBean.DEFAULT)));
        MyApp.getInstance().getSerialPortManager().setOnSerialPortDataListener(new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes) {
                checkResponse(bytes);
            }

            @Override
            public void onDataSent(byte[] bytes) {

            }
        });
        MyApp.getInstance().getSerialPortManager().sendBytes(check());
    }

    @Override
    public void setLangView(LangTypeBean langTypeBean) {

    }

    public  void checkResponse(byte[] data){
        String jobCode = null;
        String response_code =null;
        String hexStr = bytesToHexString(data);
        showToastMsg("hexStr_"+hexStr);
        if(hexStr.length()>3){
            jobCode = hexStr.substring(62,64);
            showToastMsg("jobCode_"+jobCode);
            response_code = hexStr.substring(64,66);
            showToastMsg("response_code"+jobCode);
            if(response_code.equals(RESPONSE_SUCCESS)){
                if(jobCode.equals(JOB_RESPONSE_CODE_CHECK)){
                    //设备确认解析
                    String hexState = hexStr.substring(hexStr.length()-16,hexStr.length()-4);
                    Log.d("==================",hexState);
                    String state = StringToHex.convertHexToString(hexState);
                    if("OOOO".equals(state)){
                        //检查成功
                    }else{
                        //检查失败
                        showToastMsg("state_error_"+state);
                        Logger.d(state);
                    }

                }else if(jobCode.equals(JOB_RESPONSE_CODE_CONFIRME)){
                    //结算确认解析

                }
            }else{
//                //错误码
//                showToastMsg("Response_error:"+response_code);
            }
        }else{
            //插卡后的反应
            if(StringToHex.convertHexToString(hexStr).equals("@")){
                //执行结算确认
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
}
