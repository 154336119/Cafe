package com.mj.cafe.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortFinder;
import com.kongqw.serialportlibrary.SerialPortManager;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;
import com.lvrenyang.io.IOCallBack;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.USBPrinting;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.MyApp;
import com.mj.cafe.R;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.utils.ActivityUtil;
import com.mj.cafe.utils.PrintUtils;
import com.mj.cafe.utils.StringToHex;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class CopyConnectUSBActivity extends BaseActivity implements OnClickListener, IOCallBack {
    @BindView(R.id.btnSelectPort)
    Button btnSelectPort;
    @BindView(R.id.btnSelectUsb)
    Button btnSelectUsb;
    @BindView(R.id.btnNext)
    Button btnNext;
    private boolean isPrintUsbOpen;
    private boolean isSerialPortOpen;
    private OptionsPickerView pvOptionsUsb;
    private List<UsbDevice> mUsbDeviceList = new ArrayList<>();
    private List<String> mUsbDeviceNameList = new ArrayList<>();
    private UsbDevice mChoiseUsbDevice;
    UsbManager mUsbManager ;
    private LinearLayout linearlayoutdevices;
    Button btnDisconnect, btnPrint;
    static CopyConnectUSBActivity mActivity;
    USBPrinting mUsb = new USBPrinting();
    ExecutorService es = Executors.newScheduledThreadPool(30);
    //串口
    private OptionsPickerView pvOptionsDevice;
    private List<Device> mDeviceList = new ArrayList<>();
    private List<String> mDeviceNameList = new ArrayList<>();
    private SerialPortFinder serialPortFinder;
    boolean openSerialPort = false;
    Device mChoiseDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectusb);
        ButterKnife.bind(this);
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        mActivity = this;
        linearlayoutdevices = (LinearLayout) findViewById(R.id.linearlayoutdevices);
        btnDisconnect = (Button) findViewById(R.id.buttonDisconnect);
        btnPrint = (Button) findViewById(R.id.buttonPrint);
        btnDisconnect.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnDisconnect.setEnabled(false);
        btnPrint.setEnabled(false);
        MyApp.getInstance().getPos().Set(mUsb);
        mUsb.SetCallBack(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            btnSelectUsb.setEnabled(false);
            searchUsbDevice();
        } else {
            finish();
        }
        serialPortFinder = new SerialPortFinder();
            sortDevice(serialPortFinder.getDevices());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnDisconnect.performClick();
    }

    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.buttonDisconnect:
//                es.submit(new TaskClose(mUsb));
                break;
            case R.id.buttonPrint:
                btnPrint.setEnabled(false);
                ActivityUtil.next(this,CountdownAcitivty.class);
                break;
            default:
                break;

        }

    }

    /**
     * 搜寻所有sub
     */
    private void searchUsbDevice() {
        linearlayoutdevices.removeAllViews();
        final UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        if (deviceList.size() > 0) {
            // 初始化选择对话框布局，并添加按钮和事件
            while (deviceIterator.hasNext()) { // 这里是if不是while，说明我只想支持一种device
                final UsbDevice device = deviceIterator.next();
                mUsbDeviceList.add(device);
                mUsbDeviceNameList.add(String.format(" VID:%04X PID:%04X",
                        device.getVendorId(), device.getProductId()));
            }
        }
        btnSelectUsb.setEnabled(true);
    }

    @OnClick({R.id.btnSelectPort, R.id.btnSelectUsb, R.id.btnNext,R.id.buttonPrint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSelectPort:
                initOptionPickerDevice(mDeviceNameList);
                break;
            case R.id.btnSelectUsb:
                initOptionPickerUsebDevice(mUsbDeviceNameList);
                break;
            case R.id.btnNext:
                ActivityUtil.next(this,BankCardPayAcitivty.class);
//                ActivityUtil.next(this,MainActivity.class);
                break;
            case R.id.buttonPrint:
                es.submit(new TaskEnPrint(MyApp.getInstance().getPos()));
                break;
        }
    }





    public static class TaskOpen implements Runnable {
        USBPrinting usb = null;
        UsbManager usbManager = null;
        UsbDevice usbDevice = null;
        public TaskOpen(USBPrinting usb, UsbManager usbManager, UsbDevice usbDevice) {
            this.usb = usb;
            this.usbManager = usbManager;
            this.usbDevice = usbDevice;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            usb.Open(usbManager, usbDevice);
        }
    }

    static int dwWriteIndex = 1;

    public static class TaskClose implements Runnable {
        USBPrinting usb = null;

        public TaskClose(USBPrinting usb) {
            this.usb = usb;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            usb.Close();
        }
    }

    @Override
    public void OnOpen() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnPrint.setEnabled(true);
                Toast.makeText(mActivity, "Connected", Toast.LENGTH_SHORT).show();
                isPrintUsbOpen = true;
            }
        });
    }

    @Override
    public void OnOpenFailed() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnPrint.setEnabled(false);
                Toast.makeText(mActivity, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnClose() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                btnDisconnect.setEnabled(false);
//                btnPrint.setEnabled(false);
//                linearlayoutdevices.setEnabled(true);
//                for (int i = 0; i < linearlayoutdevices.getChildCount(); ++i) {
//                    Button btn = (Button) linearlayoutdevices.getChildAt(i);
//                    btn.setEnabled(true);
//                }
//                searchUsbDevice(); // 如果因为打印机关机导致Close。那么这里需要重新枚举一下。
            }
        });
    }

    @Override
    public void OnMessage(final String msg) {
        // TODO Auto-generated method stub

    }

    private void initOptionPickerUsebDevice( List<String> usbDeviceNameList) {//条件选择器初始化
        pvOptionsUsb = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if(mUsbDeviceList.size()>0){
                    mChoiseUsbDevice = mUsbDeviceList.get(options1);


                    PendingIntent mPermissionIntent = PendingIntent
                            .getBroadcast(
                                    CopyConnectUSBActivity.this,
                                    0,
                                    new Intent(
                                            CopyConnectUSBActivity.this
                                                    .getApplicationInfo().packageName),
                                    0);
                    if (!mUsbManager.hasPermission(mChoiseUsbDevice)) {
                        mUsbManager.requestPermission(mChoiseUsbDevice,
                                mPermissionIntent);
                        Toast.makeText(getApplicationContext(),
                                "没有权限", Toast.LENGTH_LONG)
                                .show();
                    }else{
                        es.submit(new CopyConnectUSBActivity.TaskOpen(mUsb, mUsbManager, mChoiseUsbDevice));
                    }

                }
            }
        })
                .setContentTextSize(30)//设置滚轮文字大小
                .setSelectOptions(0, 1)//默认选中项\
                .setSubCalSize(25)
                .setSubmitText(getString(R.string.en_Confirm))
                .setCancelText(getString(R.string.en_Cancel))
                .isDialog(true)
                .setTitleText("UsbPrint")
                .setCancelColor(R.color.color_green)
                .setSubmitColor(R.color.color_green)
                .setTitleColor(R.color.com_txt_color)
                .setTitleSize(30)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        pvOptionsUsb.setPicker(mUsbDeviceNameList);//二级选择器
        pvOptionsUsb.show();
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
        // 打开串口
        boolean openSerialPort = MyApp.getInstance().getSerialPortManager().setOnOpenSerialPortListener(new OnOpenSerialPortListener() {
            @Override
            public void onSuccess(File device) {
                boolean sendBytes = MyApp.getInstance().getSerialPortManager().sendBytes(check());
                if(sendBytes){
                    showToastMsg("串口打开成功_check发送成功");
                }else{
                    showToastMsg("串口打开成功_check发送失败");
                }
            }

            @Override
            public void onFail(File device, Status status) {
                showToastMsg("串口打开失败");
            }
        })
                .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                    @Override
                    public void onDataReceived(byte[] bytes) {
                        final byte[] finalBytes = bytes;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkResponse(finalBytes);
//                                checkResponse(finalBytes);
//                                MyApp.getInstance().getSerialPortManager().closeSerialPort();
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

    private void checkResponse(byte[] data) {
        showToastMsg("checkResponse");
        String jobCode = null;
        String response_code = null;
        String hexStr = bytesToHexString(data);
        showToastMsg("hexStr_"+hexStr);
        if (hexStr.length() > 3) {
            jobCode = hexStr.substring(62, 64);
            response_code = hexStr.substring(64, 66);
            if (response_code.equals(RESPONSE_SUCCESS)) {
                if (jobCode.equals(JOB_RESPONSE_CODE_CHECK)) {
                    //设备确认解析
                    String hexState = hexStr.substring(hexStr.length() - 16, hexStr.length() - 4);
                    Log.d("==================", hexState);
                    String state = StringToHex.convertHexToString(hexState);
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
    @Override
    public void setLangView(LangTypeBean langTypeBean) {

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

    private void sortDevice(List<Device> devices){
        for(Device device : devices){
            File file = device.getFile();
            if(file.canRead() && file.canWrite() && file.canExecute()){
                mDeviceList.add(device);
                mDeviceNameList.add(device.getName());
            }
        }
    }


    public static class TaskEnPrint implements Runnable {
        Pos pos = null;

        public TaskEnPrint(Pos pos) {
            this.pos = pos;
        }

        public int PrintGBKStr(String StrUtf8, int nOrgx, int nWidthTimes, int nHeightTimes, int nFontType, int nFontStyle) {
            //原字符串函数里有乱码，仅用来设置格式
            pos.POS_S_TextOut("\r\n", nOrgx, nWidthTimes, nHeightTimes, nFontType, nFontStyle);
            //自己将UTF-8字符串转成GBK码，并调用底层函数发送字节
            String t = StrUtf8;
            try {
                int LenGBK;
                String utf8 = new String(t.getBytes("UTF-8"));
                String unicode = new String(utf8.getBytes(), "UTF-8");
                byte[] Bytegbk = unicode.getBytes("GBK");
                LenGBK = unicode.getBytes("GBK").length;
                return pos.GetIO().Write(Bytegbk, 0, LenGBK);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return pos.GetIO().Write(StrUtf8.getBytes(), 0, StrUtf8.length());
            }
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            boolean bPrintResult = false;
            byte[] status = new byte[1];

            if (pos.POS_QueryStatus(status, 2000, 2)) {
                pos.POS_FeedLine();
                pos.POS_S_Align(1);//居中对齐

                PrintGBKStr("[Order No:0001]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
                PrintGBKStr("[PW:0001]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
                pos.POS_FeedLine();
                pos.POS_S_Align(0);//左对齐

                ////点餐内容
                PrintGBKStr(" [Order Time] 2019-07-18[Tue] 13:33:43\r\n", 0, 0, 0, 0, 0);//1倍大
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("Product","Count")+"\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("Americano","1")+"\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("Ice Americano","1")+"\n", 0, 0, 0, 0, 0);//
                pos.POS_S_Align(0);//左对齐
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Smart Green Cafe\n", 0, 0, 0, 0, 0x08);
                PrintGBKStr("282-34-00719 DongJae Lee Tel. 010-2505-8844\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Floor 1, No.396, Guryongdaljeon-ro,( Dungok-dong) Yuseong-gu, Daejeon)\n", 0, 0, 0, 0, 0);
                pos.POS_FeedLine();
                PrintGBKStr("[Date of receipt] 2019-07-16 13:33;32\n", 0, 0, 0, 0, 0);
                PrintGBKStr("[Number of receipt] 190716133321523-0000\n", 0, 0, 0, 0, 0);
                PrintGBKStr("[Sales Details] --------------------------------\n", 0, 0, 0, 0, 0);
                PrintGBKStr(PrintUtils.addSpc("Sales Details:","3,636")+"\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("Tax:","364")+"\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("Total:","4,000")+"\n", 0, 0, 0, 0, 0);//
                pos.POS_S_Align(0);//左对齐
                PrintGBKStr("[Payment Details] ------------------------------\n", 0, 0, 0, 0, 0);
                PrintGBKStr(PrintUtils.addSpc("Credit card:","4,000")+"\n", 0, 0, 0, 0, 0);//
                pos.POS_S_Align(0);//左对齐
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Card #: 6251-20**-****-****\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Card com.: BC card\n", 0, 0, 0, 0, 0);
                PrintGBKStr("instalments: 00\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Approval #: 33593898\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Transaction Time: 2019-07-16 13:33:32\n", 0, 0, 0, 0, 0);
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Koisk ID: 7142962002 – Kiosk01]\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Membership #: 010-2505-8844\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Point        : 4000\n", 0, 0, 0, 0, 0);
                pos.POS_FeedLine();
                pos.POS_S_Align(1);//居中
                PrintGBKStr("*****Thank you*****\n", 0, 0, 0, 0, 0);
                //	pos.POS_S_SetQRcode("https://appsto.re/cn/2KF_bb.i", 8, 0, 3);
                pos.POS_FeedLine();
                pos.POS_FeedLine();
                pos.POS_FeedLine();
                pos.POS_CutPaper();
                bPrintResult = pos.POS_QueryStatus(status, 2000, 2);
            }
            final boolean bIsOpened = pos.GetIO().IsOpened();
            if (bPrintResult) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(mActivity.getApplicationContext(), "打印成功", Toast.LENGTH_SHORT).show();
                        mActivity.btnPrint.setEnabled(bIsOpened);
                    }

                });
            } else {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(mActivity.getApplicationContext(), "打印失败", Toast.LENGTH_SHORT).show();
                        mActivity.btnPrint.setEnabled(bIsOpened);
                    }

                });
            }
        }
    }
}