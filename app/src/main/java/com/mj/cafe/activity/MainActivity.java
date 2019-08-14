package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.arch.lifecycle.Observer;

import com.bigkoo.pickerview.OptionsPickerView;
import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortFinder;
import com.kongqw.serialportlibrary.SerialPortManager;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;
import com.mj.cafe.BizcContant;
import com.mj.cafe.MyApp;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.utils.ActivityUtil;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.R;
import com.mj.cafe.utils.SharedPreferencesUtil;
import com.mj.cafe.utils.StringToHex;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mj.cafe.bean.LangTypeBean.CN;
import static com.mj.cafe.bean.LangTypeBean.EN;
import static com.mj.cafe.bean.LangTypeBean.KO;
import static com.mj.cafe.utils.PortUtils.JOB_CODE_CHECK;
import static com.mj.cafe.utils.PortUtils.JOB_RESPONSE_CODE_CHECK;
import static com.mj.cafe.utils.PortUtils.RESPONSE_SUCCESS;
import static com.mj.cafe.utils.PortUtils.bbc;
import static com.mj.cafe.utils.PortUtils.def_date_code;
import static com.mj.cafe.utils.PortUtils.etx;
import static com.mj.cafe.utils.PortUtils.getStringDateLong1;
import static com.mj.cafe.utils.PortUtils.stx;
import static com.mj.cafe.utils.PortUtils.terminaloID;
import static com.mj.cafe.utils.StringToHex.bytesToHexString;
import static com.mj.cafe.utils.StringToHex.getXor;

public class MainActivity extends BaseActivity implements OnOpenSerialPortListener {

    @BindView(R.id.IvZhongWen)
    ImageView IvZhongWen;
    @BindView(R.id.IvHanYu)
    ImageView IvHanYu;
    @BindView(R.id.IvYingYu)
    ImageView IvYingYu;
    @BindView(R.id.IvLogo)
    ImageView IvLogo;
    @BindView(R.id.btn)
    TextView btn;
    private OptionsPickerView pvOptionsDevice;
    private List<Device> mDeviceList = new ArrayList<>();
    private List<String> mDeviceNameList = new ArrayList<>();
    private SerialPortFinder serialPortFinder;
    boolean openSerialPort = false;
    Device  mChoiseDevice;

    SerialPortManager mSerialPortManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setLangView((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE,new LangTypeBean(LangTypeBean.DEFAULT)));
//        if(MyApp.getInstance().getmSelectDevice() == null){
//            serialPortFinder = new SerialPortFinder();
//            sortDevice(serialPortFinder.getDevices());
//            initOptionPickerDevice(mDeviceNameList);
//        }
    }

    @OnClick({R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu, R.id.btn})
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
                ActivityUtil.next(this,ChooseWayEatActivity.class);
//                boolean sendBytes = mSerialPortManager.sendBytes(check());
//                showToastMsg(sendBytes ? "发送成功" : "发送失败");
//                if(MyApp.getInstance().getmSelectDevice() == null){
//                    initOptionPickerDevice(mDeviceNameList);
//                }else{
//                    ActivityUtil.next(this,ChooseWayEatActivity.class);
//                }
                break;
        }
    }

    @Override
    public void setLangView(LangTypeBean langTypeBean){
        switch (langTypeBean.getType()) {
            case CN:
                btn.setText(getString(R.string.cn_Order));
                break;
            case EN:
                btn.setText(getString(R.string.en_Order));
                break;
            case KO:
                btn.setText(getString(R.string.ko_Order));
                break;
        }
    }

    private void sortDevice(List<Device> devices){
        for(Device device : devices){
            File file = device.getFile();
            if(file.canRead() && file.canWrite() && file.canExecute()){
                mDeviceList.add(device);
                mDeviceNameList.add(device.getName());
            }
        }
    }

    private void initOptionPickerDevice( List<String> deviceNameList) {//条件选择器初始化
        pvOptionsDevice = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mChoiseDevice = mDeviceList.get(options1);
                openSerialPort();
            }
        })
                .setContentTextSize(30)//设置滚轮文字大小
                .setSelectOptions(0, 1)//默认选中项\
                .setSubCalSize(25)
                .setSubmitText(getString(R.string.en_Confirm))
                .setCancelText(getString(R.string.en_Cancel))
                .isDialog(true)
                .setTitleText("Port")
                .setCancelColor(R.color.color_green)
                .setSubmitColor(R.color.color_green)
                .setTitleColor(R.color.com_txt_color)
                .setTitleSize(30)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        pvOptionsDevice.setPicker(deviceNameList);//二级选择器
        pvOptionsDevice.show();
    }


    private void openSerialPort(){
        showToastMsg("正在打开串口");
        mSerialPortManager = new SerialPortManager();
        // 打开串口
        boolean openSerialPort = mSerialPortManager.setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                    @Override
                    public void onDataReceived(byte[] bytes) {
                        final byte[] finalBytes = bytes;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkResponse(finalBytes);
//                                checkResponse(finalBytes);
                                mSerialPortManager.closeSerialPort();
                            }
                        });
                    }
                    @Override
                    public void onDataSent(byte[] bytes) {
                        final byte[] finalBytes = bytes;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                showToastMsg(String.format("发送\n%s", new String(finalBytes)));
                            }
                        });
                    }
                })
                .openSerialPort(mChoiseDevice.getFile(), 115200);
    }

    @Override
    public void onSuccess(File file) {
        openSerialPort = true;
        boolean sendBytes = mSerialPortManager.sendBytes(check());
//        showToastMsg(sendBytes ? "发送成功" : "发送失败");
    }

    @Override
    public void onFail(File file, Status status) {
        showToastMsg("SerialPort_error");
    }


    /**
     * 设备检查
     * HEADER FORMAT : STX[1] + Terminal ID[16] + DateTime[14] + JobCode[1] + Response Code[1] + Data Length[2]
     * @return
     */
    private byte[] check(){
        StringBuffer sb = new StringBuffer();
        sb.append(stx);
        sb.append(terminaloID);
        sb.append(StringToHex.convertStringToHex(getStringDateLong1()));
        sb.append(JOB_CODE_CHECK);
        sb.append(RESPONSE_SUCCESS);
        sb.append(def_date_code);
        sb.append(etx);
        bbc = getXor(StringToHex.hexStringToBytes10(sb.toString()))+"";
        bbc = StringToHex.intToHex(Integer.parseInt(bbc));
        sb.append(bbc);
//        showToastMsg(sb.toString());
        return  StringToHex.hexStringToBytes16(sb.toString());
    }

    private void checkResponse(byte[] data) {
        showToastMsg("checkResponse");
        String jobCode = null;
        String response_code = null;
        String hexStr = bytesToHexString(data);
        showToastMsg("hexStr_"+hexStr);
        if (hexStr.length() > 3) {
            jobCode = hexStr.substring(62, 64);
            response_code = hexStr.substring(64, 66);
            showToastMsg("jobCode_"+jobCode);
            showToastMsg("response_code"+response_code);
            if (response_code.equals(RESPONSE_SUCCESS)) {
                if (jobCode.equals(JOB_RESPONSE_CODE_CHECK)) {
                    //设备确认解析
                    String hexState = hexStr.substring(hexStr.length() - 16, hexStr.length() - 4);
                    Log.d("==================", hexState);
                    showToastMsg("hexState_"+hexState);
                    String state = StringToHex.convertHexToString(hexState);
                    showToastMsg("state_"+state);
                    if ("OOOO".equals(state)) {
                        //检查成功
                        MyApp.getInstance().setmSelectDevice(mChoiseDevice);
                    } else {
                        //检查失败
                        Logger.d(state);
                        showToastMsg("checkResponse_"+state+"_error");
                    }
                }
            }else{
                //检查失败
                Logger.d("error");
                showToastMsg("checkResponse_error");
            }
        }
    }
}
